package com.example.user_registration_OTP.DTO;

import java.sql.Date;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserModelResponseDto {
 private String name;
    private String email;
    private String password;
    private int age;

    private Boolean isActive;

    private Date createdAt;

}
