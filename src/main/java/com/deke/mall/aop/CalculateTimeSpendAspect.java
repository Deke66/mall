package com.deke.mall.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
@Slf4j
@Order(0)
public class CalculateTimeSpendAspect {

    @Pointcut("@annotation(com.deke.mall.aop.annotation.CalculateMethodExecTime)")
    public void countTime(){

    }

    @Around("countTime()")
    public Object aroundApi(ProceedingJoinPoint point) throws Throwable {
        Object response = null;
        LocalDateTime startAt = LocalDateTime.now();
        String className = point.getSignature().getDeclaringTypeName();
        String methodName = point.getSignature().getName();

        try {
            response = point.proceed();
        } catch (Exception e) {
            throw e;
        } finally {
            long spendMs = Duration.between(startAt,LocalDateTime.now()).toMillis();
            log.info("exec {}.{}() end =====,spend time:{}ms", className, methodName, spendMs);
        }
        return response;
    }
}
