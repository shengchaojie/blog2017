package com.scj.web.controller;

import com.scj.common.CommonConstants;
import com.scj.common.ResponseResult;
import com.scj.common.exception.StatusCode;
import com.scj.service.user.UserService;
import com.scj.service.vo.UserLoginVO;
import com.scj.service.vo.UserVO;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by shengchaojie on 2017/4/7.
 */
@Controller
@RequestMapping("/user")
@EnableAutoConfiguration
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserService userService;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult<UserVO> login(@RequestBody UserLoginVO userLoginVO, HttpSession session, HttpServletRequest request, HttpServletResponse response){

        String username =userLoginVO.getUsername();
        String password =userLoginVO.getPassword();

        UserVO user =userService.login(username,password);
        if(user!=null){
            //uid=登录名|有效时间Expires|hash值。
            // hash值可以由"登录名+有效时间Expires+用户密码（加密后的）的前几位 +salt"
            String expireTime =  DateTime.now().plusHours(2).toString("yyyy-MM-dd HH:mm:ss");
            int hash =(username+expireTime+password.substring(0,3)+ "scj").hashCode();
            String uid = null;
            try {
                uid = URLEncoder.encode(username+"|"+expireTime+"|"+hash,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            Cookie cookie =new Cookie(CommonConstants.USER_ID_ENCODE,uid);
            cookie.setPath("/");
            cookie.setDomain(CommonConstants.DOMAIN);
            logger.debug("设置cookie Domain:{}",request.getServerName());
            response.addCookie(cookie);
            session.setAttribute(CommonConstants.USER_ID_ENCODE,uid);
            session.setAttribute(CommonConstants.USER_ID,user.getId());
            //session.setAttribute(CommonConstants.USER_NAME,user.getNickname());
        }

        return new ResponseResult<>(StatusCode.OK,user);
    }

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult<Boolean>  register(@RequestBody UserVO userVO){

        return new ResponseResult<>(StatusCode.OK,userService.register(userVO));
    }

    @RequestMapping(path="/logout",method = RequestMethod.GET)
    @ResponseBody
    public ResponseResult<Boolean> logout(HttpServletResponse response,HttpSession session)
    {
        if(session!=null)
        {
            session.invalidate();
        }
        //让cookie失效
        Cookie cookie =new Cookie(CommonConstants.USER_ID_ENCODE,"");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return new ResponseResult<>(StatusCode.OK);
    }
}
