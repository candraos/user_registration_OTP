package com.example.user_registration_OTP.Services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.user_registration_OTP.Models.OTP;
import com.example.user_registration_OTP.Models.UserModel;
import com.example.user_registration_OTP.Repositories.OtpRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final OtpRepository otpRepository;

    private final UserModelService userService;

    public OTP saveOtp(String email, int otpCode) {
        OTP otp = OTP.builder()
            .otpCode(otpCode)
            .isVerified(false)
            .expirationDate(LocalDateTime.now().plusMinutes(5)) // 5 minutes expiration
            .user(userService.findByEmail(email)) 
            .build();
        return otpRepository.save(otp);
    }

    public OTP findByUser(UserModel user) {
        return otpRepository.findByUser(user);
    }

    public void validateOtp(OTP otp) {
        otp.setIsVerified(true);
        otpRepository.save(otp);
    }
}
