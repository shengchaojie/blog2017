package com.scj.common.exception;

/**
 * Created by Administrator on 2016/7/11.
 */
public enum StatusCode {

    OK(200,"处理成功"),
    USER_NOT_EXISTED(1,"该用户不存在"),
    USER_REGISTERED_ALREADY(2,"用户已注册"),
    USERNAME_PASSWORD_WRONG(3,"用户名或密码错误"),
    NOTE_TAG_NOT_EXISTED(4,"该标签不存在"),
    USER_NOT_LOGIN(5,"用户未登录"),
    PASSWORD_WRONG(6,"密码错误"),
    FAILED(500,"处理失败")
    ;

    private  int code;
    private  String message;

    StatusCode(int code,String message)
    {
        this.code=code;
        this.message=message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
