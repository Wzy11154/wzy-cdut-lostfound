package com.wzy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wzy.controller.dto.UserPasswordDto;
import com.wzy.entity.Users;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper extends BaseMapper<Users> {

    @Update("update users set password = #{newPassword} where username = #{username} and password = #{password}")
    int updatePassword(UserPasswordDto userPasswordDto);
}
