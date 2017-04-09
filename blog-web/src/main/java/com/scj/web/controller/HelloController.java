package com.scj.web.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by shengchaojie on 2017/4/7.
 */
@Controller
@EnableAutoConfiguration
public class HelloController {

    @RequestMapping("/hello")
    @ResponseBody
    public String hello(){
        return "HelloWorld";
    }

}
