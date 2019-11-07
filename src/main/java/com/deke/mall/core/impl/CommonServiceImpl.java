package com.deke.mall.core.impl;

import com.deke.mall.core.CommonService;
import com.deke.mall.exception.ApplicationException;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author deke
 */
@Slf4j
@Component
public class CommonServiceImpl implements CommonService {

    private ThreadLocal<String> traceNos = new ThreadLocal<>();

    @Value("${spring.application.id:0}")
    private int applicationId;

    @Value("${spring.snowFlake.beginTimeStamp:1546272000000}")
    private long snowFlakeBeginTimestamp;

    @PostConstruct
    public void init(){
        if (this.applicationId<0 || this.applicationId>1023) {
            throw new ApplicationException("500","applicationId is invalid");
        }
    }

    @Override
    public int getApplicationId() {
        return this.applicationId;
    }

    @Override
    public String createTraceNo() {
        String traceNo = TraceNoWorker.traceNoWorker.nextTraceNo(this.applicationId);
        traceNos.set(traceNo);
        return traceNo;
    }

    @Override
    public void setTraceNo(String traceNo) {
        traceNos.set(traceNo);
    }

    @Override
    public String getTraceNo() {
        String traceNo = this.traceNos.get();
        if (null == traceNo) {
            traceNo = TraceNoWorker.traceNoWorker.nextTraceNo(this.applicationId);
            traceNos.set(traceNo);
        }
        return traceNo;
    }

    @Override
    public long nextId() {
        return IdWorker.idWorker.nextId(this.applicationId,this.snowFlakeBeginTimestamp);
    }

    /**
     * 基于雪花算法的思想生成日志追踪号
     * 固定长度24位形式为时间戳加上10位序号如yyyyMMddHHmmss1234567890
     * 时间戳取生成id的时间戳进行格式化
     * 后面10位是基于32bit得到的序号，1L<<32为4294967296 小于10位
     * 32bit组成
     * 10bit记录时间戳属于1s中的第几毫秒
     * 10bit作为应用id
     * 12bit作为毫秒内的序号
     * 此算法在1ms内可生成4096个不重复的分布式id，效率很高
     */
    private static class TraceNoWorker {
        private final String timeOfPattern = "yyyyMMddHHmmss";
        private long lastTimeStamp = -1L;
        private long sequence;
        private final int msBits = 10;
        private final int applicationIdBits = 10;
        private final int sequenceBits = 12;
        private final int msLeftMoveBits = sequenceBits + applicationIdBits;
        private final int applicationIdLeftMoveBits = sequenceBits;
        private final long maxSequence = (1L << sequenceBits) - 1;
        private final long msMax = (1L << msBits) - 1;
        private static TraceNoWorker traceNoWorker = new TraceNoWorker();

        private TraceNoWorker() {
        }

        /**
         * 获取下一个日志追踪号
         * @param applicationId 应用Id
         * @return
         */
        private String nextTraceNo(long applicationId) {
            long curTimeStamp = System.currentTimeMillis();
            long ms;
            long curSequence;
            synchronized (this) {
                if (lastTimeStamp > curTimeStamp) {
                    log.error("get traceNo occur an event that clock moved backwards");
                } else if (lastTimeStamp == curTimeStamp) {
                    sequence = (sequence + 1) & maxSequence;
                    if (sequence == 0) {
                        while ((curTimeStamp = System.currentTimeMillis()) <= lastTimeStamp + 1) {}
                    }
                } else {
                    sequence = 0;
                }
                lastTimeStamp = curTimeStamp;
                ms = curTimeStamp & msMax;
                curSequence = sequence;
            }

            long id = (ms << msLeftMoveBits) | (applicationId << applicationIdLeftMoveBits) | curSequence;
            LocalDateTime time =
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(curTimeStamp), ZoneId.systemDefault());

            return time.format(DateTimeFormatter.ofPattern(timeOfPattern)) + Strings.padStart(String.valueOf(id),10,'0');
        }


    }

    /**
     * 雪花算法
     * 以long的64bit，long类型的最大值19位，则生成id最大为19位
     * 1bit恒为0，标识正数
     * 41bit记录时间戳，从自定义的开始时间到生成id的时间，最大值2的41次方，可以有76年可用时间
     * 10bit作为应用id，则有2的10次方即1024可用
     * 12bit作为ms内生成id的序号，这每毫秒可生成2的12次方即4096个id
     * 此算法可在1ms内生成不重复的分布式id，并且效率很高
     */
    private static class IdWorker {
        private long lastTimeStamp;//41bit
        private long sequence;//12bit

        private final int timeStampBits = 41;
        private final int systemIdBits = 10;
        private final int sequenceBits = 12;

        private final int timeStampLeftMoveBits = sequenceBits+systemIdBits;
        private final int systemIdLeftMoveBits = sequenceBits;

        private final long maxSequence = (1L<<sequenceBits)-1;

        private static IdWorker idWorker = new IdWorker();

        private IdWorker(){
        }



        /**
         * 雪花算法生成下一个id
         * @return
         */
        public long nextId(long applicationId,long beginTimeStamp){
            long currentTimeStamp = System.currentTimeMillis();
            long timeStamp = currentTimeStamp;
            long curSequence = 0L;

            synchronized (this){
                if (lastTimeStamp>timeStamp){
                    throw new ApplicationException("500","get nextId occur an event that clock moved backwards");
                } else if (lastTimeStamp==timeStamp){
                    sequence = (sequence+1)&maxSequence;
                    if (sequence==0) {
                        while((timeStamp = System.currentTimeMillis())<=lastTimeStamp+1){

                        }
                    }
                }else {
                    sequence = 0L;
                }
                lastTimeStamp = timeStamp;
                curSequence = sequence;
            }

            return ((timeStamp-beginTimeStamp)<<timeStampLeftMoveBits)
                    |(applicationId<<systemIdLeftMoveBits)
                    |curSequence;
        }


    }
}
