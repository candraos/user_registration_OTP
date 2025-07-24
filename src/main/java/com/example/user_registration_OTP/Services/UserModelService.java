package com.example.user_registration_OTP.Services;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.jwt.JwsHeader;

import com.example.user_registration_OTP.DTO.UserModelRequestDto;
import com.example.user_registration_OTP.Models.UserModel;
import com.example.user_registration_OTP.Repositories.UserModelRepository;
import com.nimbusds.jose.jwk.source.ImmutableSecret;

import java.security.Key;
import java.time.Instant;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserModelService {
    private final  UserModelRepository userModelRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserModel saveUser(UserModel userModel) {
        userModel.setIsActive(false);
        String hashedPassword = passwordEncoder.encode(userModel.getPassword());
        userModel.setPassword(hashedPassword);
        return userModelRepository.save(userModel);
    }

    public UserModel findByEmail(String email) {
        return userModelRepository.findByEmail(email);
    }

    public void activateUser(UserModel user) {
       user.setIsActive(true);
         userModelRepository.save(user);
    }

    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private final String jwtSecret = "G2m1IbUu8Z3x8n5F9UsY+JL7o/VzEjP9rQ7gOjaGUKg="; // Example secret key
    private final long jwtExpiration = 7 * 24 * 60 * 60; // 7 days in seconds

    public String generateJwtToken(UserModel user) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("user_registration_otp")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(jwtExpiration))
                .subject(user.getEmail())
                .build();

				var encoder = new NimbusJwtEncoder(
				new ImmutableSecret<>(jwtSecret.getBytes())
		);
        var params = JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS256).build(),
                claims
        );
		return encoder.encode(params).getTokenValue();
    }
    
}
