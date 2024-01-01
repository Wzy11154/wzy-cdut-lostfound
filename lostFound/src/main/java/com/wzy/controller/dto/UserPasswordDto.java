package com.wzy.controller.dto;

import lombok.Data;

@Data
public class UserPasswordDto {
    private String username;
    private String password;
    private String phone;
    private String newPassword;
}
