package com.scj.service.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by shengcj on 2016/8/2.
 */
public class UserInfoVO implements Serializable{

    private Integer id;

    private Integer age;

    private Date birth;

    private Integer gender;

    private UserVO userVO;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public UserVO getUserVO() {
        return userVO;
    }

    public void setUserVO(UserVO userVO) {
        this.userVO = userVO;
    }
}
