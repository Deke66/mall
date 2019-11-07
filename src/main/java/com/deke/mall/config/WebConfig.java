package com.deke.mall.config;

import com.deke.mall.core.CommonService;
import com.deke.mall.interceptor.TraceNoInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private CommonService commonService;
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new TraceNoInterceptor(commonService));
    }
}
