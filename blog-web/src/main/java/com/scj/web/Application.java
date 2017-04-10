package com.scj.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by Administrator on 2017/4/9 0009.
 */
@Controller
@EnableWebMvc
@SpringBootApplication
@MapperScan(basePackages = "com.scj.dal.mapper")
@ComponentScan(basePackages = "com.scj.service,com.scj.web")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
