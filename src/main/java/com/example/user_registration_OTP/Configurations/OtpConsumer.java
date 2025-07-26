package com.example.user_registration_OTP.Configurations;

import java.time.LocalDateTime;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.user_registration_OTP.Services.OtpService;
import com.example.user_registration_OTP.Services.RedisService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OtpConsumer {

    private final OtpService otpService;

    private final RedisService redisService;

    @RabbitListener(queues = "otpQueue", ackMode = "MANUAL")
    public void consumeOtpEvent(String message) {
        System.out.println("Received OTP Event: " + message);
        String[] parts = message.split(": ");
        if (parts.length == 2) {
            String email = parts[0];
            int otpCode = Integer.parseInt(parts[1]);
            // send otp code by email. here i am just logging it as requested
            // In a real application, you would use an email service to send the OTP
            System.out.println("Email: " + email + ", OTP Code: " + otpCode);
           redisService.save(email, otpCode);
           LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(5);
            redisService.save("TTL", expirationTime.toString()); // Save expiration time in Redis
            
            // otpService.saveOtp(email, otpCode); 
        } else {
            System.out.println("Invalid OTP message format.");
        }
    }
}