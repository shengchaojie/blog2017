package com.scj.common.exception;

/**
 * Created by Administrator on 2016/7/11.
 */
public class BusinessException extends  RuntimeException{
    private  int code;
    private  String message;

    public BusinessException() {
    }

    public BusinessException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BusinessException(StatusCode statusCode)
    {
        this.code =statusCode.getCode();
        this.message=statusCode.getMessage();
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
