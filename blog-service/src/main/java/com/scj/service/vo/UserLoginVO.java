package com.scj.service.vo;

import javax.persistence.Transient;

/**
 * Created by Administrator on 2017/5/2 0002.
 */
public class UserLoginVO {

    private String username;

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
