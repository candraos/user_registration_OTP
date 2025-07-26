package com.example.user_registration_OTP.Repositories;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.user_registration_OTP.Models.UserModel;


@Repository
public interface AppUserRepository extends JpaRepository<UserModel, UUID> {

    Optional<UserModel> findByEmail(String email);

}
