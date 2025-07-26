package com.example.user_registration_OTP.Controllers;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.user_registration_OTP.DTO.OTPRequestDto;
import com.example.user_registration_OTP.Models.OTP;
import com.example.user_registration_OTP.Models.UserModel;
import com.example.user_registration_OTP.Services.OtpService;
import com.example.user_registration_OTP.Services.UserModelService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/otp")
@RequiredArgsConstructor
@Tag(name = "OTP Management", description = "Endpoints for managing OTPs")

public class OtpController {

    private final UserModelService userService;
    private final OtpService otpService;
    
    @PostMapping("/verify")
    public ResponseEntity<Object> verify(@RequestBody OTPRequestDto otpRequestDto) {
        if (otpRequestDto == null || otpRequestDto.getEmail() == null || otpRequestDto.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid request");
        }

        // Validate that the email corresponds to a user
        UserModel user = userService.findByEmail(otpRequestDto.getEmail());
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found for the provided email");
        }

        // Get the current OTP code assigned to the user
        OTP otp = otpService.findByUser(user);
        if (otp == null || otp.getOtpCode() != otpRequestDto.getOtpCode()) {
            return ResponseEntity.badRequest().body("Wrong OTP code");
        }

        // Check if the OTP code is expired
        if (otp.getExpirationDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("OTP code is expired");
        }

       
        userService.activateUser(user);
        otpService.validateOtp(otp);

        return ResponseEntity.ok("OTP verified successfully for email: " + otpRequestDto.getEmail());
    }
    
    
}
