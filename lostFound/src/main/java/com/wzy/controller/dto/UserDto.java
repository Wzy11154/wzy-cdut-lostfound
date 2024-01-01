package com.wzy.controller.dto;

import lombok.Data;

@Data
public class UserDto {

    private Integer id;

    private String username;

    private String password;

    private String nickname;

    private String phone;

    private String sex;

    private String token;

    private String role;

}
