package com.deke.mall.core.impl;

import com.deke.mall.core.CommonService;
import com.deke.mall.core.VisitRecord;
import com.deke.mall.entity.RequestInfo;
import com.deke.mall.service.IRequestInfoService;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Async
public class VisitRecordImpl implements VisitRecord {

    @Autowired
    private CommonService commonService;

    @Autowired
    private IRequestInfoService requestInfoService;

    @Override
    public void visitRecord(String traceNo, long spendMs, String status, JoinPoint joinPoint) {
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setApplicationId(commonService.getApplicationId());
        requestInfo.setClassName(joinPoint.getSignature().getDeclaringTypeName());
        requestInfo.setMethodName(joinPoint.getSignature().getName());
        requestInfo.setSpendMs(spendMs);
        requestInfo.setStatus(status);
        requestInfo.setTraceNo(traceNo);
        requestInfoService.save(requestInfo);
    }
}
