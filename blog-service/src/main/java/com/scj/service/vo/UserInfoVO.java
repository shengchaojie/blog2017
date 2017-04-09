package com.scj.service.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by shengcj on 2016/8/2.
 */
public class UserInfoVO implements Serializable{

    private Integer age;

    private Date birth;

    private Integer gender;

    private long userId;

    private String nickName;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return "UserInfoVO{" +
                "age=" + age +
                ", birth=" + birth +
                ", gender=" + gender +
                ", userId=" + userId +
                ", nickName='" + nickName + '\'' +
                '}';
    }
}
