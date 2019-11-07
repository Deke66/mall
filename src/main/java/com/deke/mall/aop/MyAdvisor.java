package com.deke.mall.aop;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MyAdvisor extends AbstractBeanFactoryPointcutAdvisor {

    @PostConstruct
    public void init(){
        super.setAdviceBeanName("log");
    }



    @Override
    public Pointcut getPointcut() {
        return new AnnotationMatchingPointcut(TestAnnotation.class);
    }


}
