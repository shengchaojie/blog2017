package com.scj.service.user.impl;

import com.scj.common.exception.BusinessException;
import com.scj.common.exception.StatusCode;
import com.scj.common.utils.EncryptUtil;
import com.scj.dal.mapper.UserInfoMapper;
import com.scj.dal.mapper.UserMapper;
import com.scj.dal.ro.UserInfoRo;
import com.scj.dal.ro.UserRO;
import com.scj.service.user.UserService;
import com.scj.service.vo.UserInfoVO;
import com.scj.service.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/4/9 0009.
 */
@Service
public class UserServiceImpl implements UserService{

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    @Transactional
    public boolean register(UserVO userVO) {
        if(isUserExisted(userVO.getUsername())){
            throw new BusinessException(StatusCode.USER_REGISTERED_ALREADY);
        }
        userVO.setPassword(EncryptUtil.encodeWithMD5(userVO.getPassword()));

        UserRO userRO =new UserRO();
        BeanUtils.copyProperties(userVO,userRO);
        userMapper.insertSelective(userRO);

        if(userVO.getUserInfoVO()!=null){
            UserInfoRo userInfoRo =new UserInfoRo();
            BeanUtils.copyProperties(userVO.getUserInfoVO(),userInfoRo);
            userInfoRo.setUserId(userRO.getId());
            userInfoMapper.insertSelective(userInfoRo);
        }

        return true;
    }

    @Override
    public UserVO login(String username, String password) {
        if(!isUserExisted(username)){
            throw new BusinessException(StatusCode.USER_NOT_EXISTED);
        }

        UserRO userRO =new UserRO();
        userRO.setUsername(username);
        userRO.setPassword(EncryptUtil.encodeWithMD5(password));
        userRO =userMapper.selectOne(userRO);
        if(userRO==null){
            throw new BusinessException(StatusCode.USERNAME_PASSWORD_WRONG);
        }

        UserVO userVO =new UserVO();
        BeanUtils.copyProperties(userRO,userVO);
        return userVO;
    }

    @Override
    public boolean isUserExisted(String username) {
        UserRO userRO =new UserRO();
        userRO.setUsername(username);
        return userMapper.select(userRO).size()>0;
    }

    @Override
    public UserVO getUserByUsername(String username) {
        UserRO userRO =new UserRO();
        userRO.setUsername(username);
        userRO =userMapper.selectOne(userRO);
        if(userRO==null){
            throw new BusinessException(StatusCode.USER_NOT_EXISTED);
        }
        UserInfoRo userInfoRo =new UserInfoRo();
        userInfoRo.setUserId(userRO.getId());
        userInfoRo =userInfoMapper.selectOne(userInfoRo);

        UserVO user =new UserVO();
        BeanUtils.copyProperties(userRO,user);
        if(userInfoRo!=null){
            UserInfoVO userInfoVO =new UserInfoVO();
            BeanUtils.copyProperties(userInfoRo,userInfoVO);
            user.setUserInfoVO(userInfoVO);
        }

        return user;
    }

    @Override
    public UserVO getUserById(Long userId) {
        UserRO userRO =new UserRO();
        userRO.setId(userId);
        userRO =userMapper.selectOne(userRO);
        if(userRO==null){
            throw new BusinessException(StatusCode.USER_NOT_EXISTED);
        }
        UserInfoRo userInfoRo =new UserInfoRo();
        userInfoRo.setUserId(userRO.getId());
        userInfoRo =userInfoMapper.selectOne(userInfoRo);

        UserVO user =new UserVO();
        BeanUtils.copyProperties(userRO,user);
        if(userInfoRo!=null){
            UserInfoVO userInfoVO =new UserInfoVO();
            BeanUtils.copyProperties(userInfoRo,userInfoVO);
            user.setUserInfoVO(userInfoVO);
        }

        return user;
    }

    @Override
    public boolean changePassword(Long id, String passwordBefore, String passwordAfter) {
        UserRO userRO =new UserRO();
        userRO.setId(id);//id从session拿
        userRO.setPassword(EncryptUtil.encodeWithMD5(passwordBefore));
        userRO =userMapper.selectOne(userRO);
        if(userRO ==null){
            throw new BusinessException(StatusCode.PASSWORD_WRONG);
        }

        userRO =new UserRO();
        userRO.setId(id);
        userRO.setPassword(EncryptUtil.encodeWithMD5(passwordAfter));
        int count =userMapper.updateByPrimaryKeySelective(userRO);

        return count == 1;
    }
}
