package com.scj.service.vo;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/10.
 */

public class UserVO implements Serializable{
    private int id;
    private String username;
    private String password;

    /*@OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
    private List<Talk> talks;*/

    private UserInfoVO userInfoVO;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

   /* public List<Talk> getTalks() {
        return talks;
    }

    public void setTalks(List<Talk> talks) {
        this.talks = talks;
    }*/

    public UserInfoVO getUserInfoVO() {
        return userInfoVO;
    }

    public void setUserInfoVO(UserInfoVO userInfoVO) {
        this.userInfoVO = userInfoVO;
    }


    @Override
    public String toString() {
        return "UserVO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", userInfoVO=" + userInfoVO +
                '}';
    }
}
