package com.example.user_registration_OTP.Models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Builder
public class OTP {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private UUID id;
    private int otpCode;

    private LocalDateTime expirationDate;
    private Boolean isVerified;

    @ManyToOne
    @JoinColumn(name="user_id")
    private UserModel user;
    
}