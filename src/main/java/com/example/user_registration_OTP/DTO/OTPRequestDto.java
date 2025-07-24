package com.example.user_registration_OTP.DTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class OTPRequestDto {
private String email;
private int otpCode;
}
