package com.deke.mall.aop;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Component;

@Component(value = "log")
@Slf4j
public class LogInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        log.info("自定义注解执行开始");
        Object res = methodInvocation.proceed();
        log.info("自定义注解执行结束");
        return null;
    }
}
