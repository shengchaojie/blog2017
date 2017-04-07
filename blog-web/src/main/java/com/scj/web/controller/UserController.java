package com.scj.web.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by shengchaojie on 2017/4/7.
 */
@Controller
@RequestMapping("/user")
@EnableAutoConfiguration
public class UserController {

    @RequestMapping("/login")
    @ResponseBody
    public String login(){
        return "login success";
    }
}
