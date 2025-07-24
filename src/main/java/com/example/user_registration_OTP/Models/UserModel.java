package com.example.user_registration_OTP.Models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class UserModel {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="user_id")
    private UUID id;

    private String name;
    private String password;
    private int age;
    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date(System.currentTimeMillis());
    }
    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "created_at")
    @CreatedDate
    private Date createdAt;

    @OneToMany(mappedBy = "user")
    private List<OTP> otps = new ArrayList<>();

    
}