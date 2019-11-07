package com.deke.mall.core;


import org.aspectj.lang.JoinPoint;

public interface VisitRecord {
    void visitRecord(String traceNo,long spendMs, String status, JoinPoint joinPoint);
}
