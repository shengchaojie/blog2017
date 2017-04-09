package com.scj.common;

import com.scj.common.exception.BusinessException;
import com.scj.common.exception.StatusCode;

/**
 * Created by shengchaojie on 2016/8/2.
 */
public class ResponseResult<T> {
    private int code;
    private String message;
    private T data;

    public ResponseResult() {
    }

    public ResponseResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResponseResult(BusinessException ex)
    {
        this(ex.getCode(),ex.getMessage());
    }

    public ResponseResult(StatusCode statusCode)
    {
        this(statusCode.getCode(),statusCode.getMessage());
    }

    public ResponseResult(StatusCode statusCode, T data)
    {
        this(statusCode.getCode(),statusCode.getMessage(),data);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getObject() {
        return data;
    }

    public void setObject(T object) {
        this.data = object;
    }
}
