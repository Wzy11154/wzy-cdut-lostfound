package com.wzy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzy.common.Constants;
import com.wzy.controller.dto.UserDto;
import com.wzy.controller.dto.UserPasswordDto;
import com.wzy.entity.Users;
import com.wzy.exception.ServiceException;
import com.wzy.mapper.UserMapper;
import com.wzy.service.UserService;
import com.wzy.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, Users> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDto login(UserDto userDto) {
        Users one = getUserInfo(userDto);
        if (one != null){
            BeanUtil.copyProperties(one,userDto,true);
            //设置token
            String token = TokenUtils.getToken(one.getId().toString(),one.getPassword());
            userDto.setToken(token);
            return userDto;
        }
        else{
            throw new ServiceException(Constants.CODE_600,"用户名或密码错误");
        }

    }

    @Override
    public Users register(UserDto userDto) {
        Users one = getUserInfo(userDto);
        if (one == null){
            one = new Users();
            BeanUtil.copyProperties(userDto,one,true);
            save(one);
        }else {
            throw new ServiceException(Constants.CODE_600,"用户已存在");
        }
        return one;
    }

    @Override
    public void updatePassword(UserPasswordDto userPasswordDto) {
        int update = userMapper.updatePassword(userPasswordDto);
        if (update < 1) {
            throw new ServiceException(Constants.CODE_600, "密码错误");
        }
    }

    private Users getUserInfo(UserDto userDto){
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",userDto.getUsername());
        queryWrapper.eq("password",userDto.getPassword());
        Users one;
        try{
            one = getOne(queryWrapper);//从数据库查询用户信息

        }catch (Exception e){
            throw new ServiceException(Constants.CODE_500,"系统错误");
        }
        return one;
    }
}
