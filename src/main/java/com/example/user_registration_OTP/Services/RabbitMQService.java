package com.example.user_registration_OTP.Services;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RabbitMQService {
    private final RabbitTemplate rabbitTemplate;

    public void sendOtpEvent(String email, int otpCode) {
        String message = String.format("%s: %d", email, otpCode);
        rabbitTemplate.convertAndSend("otpQueue", message);
    }
}
