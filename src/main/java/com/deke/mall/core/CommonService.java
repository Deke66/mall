package com.deke.mall.core;

public interface CommonService {
    int getApplicationId();

    String createTraceNo();

    /**
     *
     * @param traceNo
     */
    void setTraceNo(String traceNo);
    String getTraceNo();
    long nextId();
}
