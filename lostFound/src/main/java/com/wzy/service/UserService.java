package com.wzy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wzy.controller.dto.UserDto;
import com.wzy.controller.dto.UserPasswordDto;
import com.wzy.entity.Users;

public interface UserService extends IService<Users> {

    UserDto login(UserDto userDto);

    Users register(UserDto userDto);

    void updatePassword(UserPasswordDto userPasswordDto);
}
