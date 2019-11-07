package com.deke.mall.exception;

import lombok.Data;

@Data
public class ApplicationException extends RuntimeException{
    private String code;
    private String msg;

    public ApplicationException(String code,String msg){
        super(msg);
        this.code = code;
        this.msg = msg;
    }

}
