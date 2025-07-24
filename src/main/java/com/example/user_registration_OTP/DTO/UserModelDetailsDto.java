package com.example.user_registration_OTP.DTO;

import com.example.user_registration_OTP.Models.UserModel;

public class UserModelDetailsDto {
 private String email;
    private String name;

    public UserModelDetailsDto(UserModel user) {
        this.email = user.getEmail();
        this.name = user.getName();
    }
}
