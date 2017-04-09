package com.scj.web.exception;

import com.alibaba.fastjson.JSON;
import com.scj.common.ResponseResult;
import com.scj.common.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by shengchaojie on 2016/8/2.
 * 返回json用于前后端异构架构，只能跳转到异常页面
 */
public class GlobalExceptionHandler implements HandlerExceptionResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        ModelAndView mv =new ModelAndView();

        httpServletResponse.setStatus(HttpStatus.OK.value());
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setHeader("Cache-control","no-cache,must-revalidate");

        ResponseResult<String> responseResult =null;
        if(e instanceof BusinessException)
        {
            responseResult =new ResponseResult<>((BusinessException) e);

        }else
        {
            responseResult =new ResponseResult<>(1,e.getMessage());
        }

        String outputJson = JSON.toJSONString(responseResult);
        LOGGER.debug("exception outputJson :{}",outputJson);

        try {
            httpServletResponse.getWriter().write(outputJson);
        } catch (IOException e1) {
            LOGGER.error("输出json异常 :"+e1.getMessage(),e1);
        }

        LOGGER.debug("捕获异常:"+e.getMessage(),e);
        return mv;
    }
}
