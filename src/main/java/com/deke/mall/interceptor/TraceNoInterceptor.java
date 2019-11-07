package com.deke.mall.interceptor;

import com.deke.mall.common.ParamKeys;
import com.deke.mall.core.CommonService;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class TraceNoInterceptor implements HandlerInterceptor {

    private CommonService commonService;

    public TraceNoInterceptor(@NonNull CommonService commonService){
        this.commonService = commonService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String traceNo = request.getParameter(ParamKeys.TRACE_NO);
        if (traceNo==null) {
            traceNo = commonService.createTraceNo();
        }else {
            commonService.setTraceNo(traceNo);
        }
        MDC.put(ParamKeys.TRACE_NO,traceNo);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           @Nullable ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) throws Exception {
        MDC.remove(ParamKeys.TRACE_NO);
    }
}
