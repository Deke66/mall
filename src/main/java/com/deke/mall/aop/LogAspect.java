package com.deke.mall.aop;

import com.alibaba.fastjson.JSON;
import com.deke.mall.core.CommonService;
import com.deke.mall.core.VisitRecord;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;


@Component
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@Aspect
public class LogAspect {

    @Autowired
    private VisitRecord visitRecord;

    @Autowired
    private CommonService commonService;

    @Pointcut("execution( * com.deke.mall.controller.*.*(..))")
    public void log(){

    }

    @Around("log()")
    public Object aroundLog(ProceedingJoinPoint point) throws Throwable{
        Object response = null;
        LocalDateTime startAt = LocalDateTime.now();
        String status = "S";
        String className = point.getSignature().getDeclaringTypeName();
        String methodName = point.getSignature().getName();
        try {
            log.info("start exec class:{} and method:{}()=====,args:{}", className, methodName, argsToString(point.getArgs()));
            response = point.proceed();
        } catch (Exception e) {
            status = "F";
            log.error("",e);
            throw e;
        } finally {
            long spendMs = Duration.between(startAt,LocalDateTime.now()).toMillis();
            //异步执行
            visitRecord.visitRecord(commonService.getTraceNo(),spendMs,status,point);
            log.info("{}.{}() end =====,spend time:{}ns,response:{}", className, methodName, spendMs,argsToString(response));
        }
        return response;
    }

    private String argsToString(Object object) {
        try {
            return JSON.toJSONString(object);
        } catch (Exception e) {
            log.error("", e);
        }
        return String.valueOf(object);
    }


}
