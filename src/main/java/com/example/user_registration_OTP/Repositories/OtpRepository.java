package com.example.user_registration_OTP.Repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.user_registration_OTP.Models.OTP;
import com.example.user_registration_OTP.Models.UserModel;

@Repository
public interface OtpRepository extends JpaRepository<OTP, UUID> {

    OTP findByUser(UserModel user);

}
