package com.example.user_registration_OTP.Configurations;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.user_registration_OTP.DTO.OTPRequestDto;
import com.example.user_registration_OTP.Models.OTP;
import com.example.user_registration_OTP.Services.OtpService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OtpConsumer {

    private final OtpService otpService;

    @RabbitListener(queues = "otpQueue")
    public void consumeOtpEvent(String message) {
        System.out.println("Received OTP Event: " + message);
        String[] parts = message.split(": ");
        if (parts.length == 2) {
            String email = parts[0];
            int otpCode = Integer.parseInt(parts[1]);
            // send otp code by email. here i am just logging it as requested
            // In a real application, you would use an email service to send the OTP
            System.out.println("Email: " + email + ", OTP Code: " + otpCode);
           
            otpService.saveOtp(email, otpCode); 
        } else {
            System.out.println("Invalid OTP message format.");
        }
    }
}