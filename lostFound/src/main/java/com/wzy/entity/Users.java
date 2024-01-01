package com.wzy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Users {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String username;

    private String nickname;

    private String password;

    private String email;

    private String phone;

    private String sex;

    private String role;
}
