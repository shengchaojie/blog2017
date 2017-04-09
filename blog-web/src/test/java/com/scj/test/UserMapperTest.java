package com.scj.test;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.scj.dal.mapper.UserInfoMapper;
import com.scj.dal.mapper.UserMapper;
import com.scj.dal.ro.UserInfoRo;
import com.scj.dal.ro.UserRO;
import com.scj.web.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
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
@SpringBootTest(classes = Application.class)
public class UserMapperTest {

    @Resource
    UserMapper userMapper;

    @Resource
    UserInfoMapper userInfoMapper;

    @Test
    @Rollback(false)
    public void testInsert(){
        for(int i=0;i<1000;i++) {
            UserRO userRO = new UserRO();
            userRO.setUsername("scj"+i);
            userRO.setPassword("123");
            int key =userMapper.insertSelective(userRO);
            UserInfoRo userInfoRo =new UserInfoRo();
            userInfoRo.setAge(12);
            userInfoRo.setBirth(new Date());
            userInfoRo.setGender(1);
            userInfoRo.setNickName("xiaopang");
            userInfoRo.setUserId(userRO.getId());
            userInfoMapper.insertSelective(userInfoRo);
        }
    }

    @Test
    @Rollback(false)
    public void testSelect(){
        UserRO userRO = new UserRO();
        PageHelper.startPage(2,10);//默认从1开始
        PageInfo<UserRO> userROPageInfo =new PageInfo<>(userMapper.select(userRO));
        System.out.println(userROPageInfo.getList());
    }


}
