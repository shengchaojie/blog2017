package com.scj.test;

import com.scj.service.user.UserService;
import com.scj.service.vo.UserInfoVO;
import com.scj.service.vo.UserVO;
import com.scj.web.Application;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by Administrator on 2017/4/9 0009.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@Transactional
@Rollback(false)
@SpringBootTest(classes = Application.class)
public class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void testRegister(){
        UserVO userVO =new UserVO();
        userVO.setUsername("scj3");
        userVO.setPassword("123456");
        UserInfoVO userInfoVO =new UserInfoVO();
        userInfoVO.setGender(1);
        userInfoVO.setNickName("xiaopang");
        userInfoVO.setBirth(new DateTime(1992,5,25,0,0).toDate());
        userInfoVO.setAge(25);
        userVO.setUserInfoVO(userInfoVO);
        userService.register(userVO);
    }

    @Test
    public void testLogin(){
        Assert.assertNotNull(userService.login("scj3","1234567"));
    }

    @Test
    public void testGetByName(){
        System.out.println(userService.getUserByUsername("scj3"));
    }

    @Test
    public void testGetById(){
        System.out.println(userService.getUserById(5015L));
    }

    @Test
    public void testChangePassword(){
        userService.changePassword(5015L,"123456","1234567");
    }
}
