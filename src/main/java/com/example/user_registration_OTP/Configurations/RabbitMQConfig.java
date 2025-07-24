package com.example.user_registration_OTP.Configurations;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean
    public Queue otpQueue() {
        return new Queue("otpQueue", true);
    }
}