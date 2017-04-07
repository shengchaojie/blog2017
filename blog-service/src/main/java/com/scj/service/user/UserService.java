package com.scj.service.user;


import com.scj.service.vo.UserVO;

/**
 * Created by Administrator on 2016/7/11.
 */
public interface UserService {

    boolean register(UserVO userVO);

    UserVO login(String username, String password);

    boolean isUserExisted(String username);

    UserVO getUserByUsername(String username);

    UserVO getUserById(Integer integer);
}
