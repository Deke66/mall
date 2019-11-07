package com.deke.mall.exception;

import com.deke.mall.core.entity.Payload;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdviceEx {

    @ExceptionHandler(value = Exception.class)
    public Payload exceptionHandler(Exception ex){
        return new Payload("500",ex.getMessage(),null);
    }

    @ExceptionHandler(value = ApplicationException.class)
    public Payload applicationExceptionHandler(ApplicationException ex){
        return new Payload(ex.getCode(),ex.getMsg(),null);
    }

}
