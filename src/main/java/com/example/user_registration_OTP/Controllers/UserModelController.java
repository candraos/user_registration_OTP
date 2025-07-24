package com.example.user_registration_OTP.Controllers;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;

import com.example.user_registration_OTP.DTO.UserModelDetailsDto;
import com.example.user_registration_OTP.DTO.UserModelRequestDto;
import com.example.user_registration_OTP.DTO.UserModelResponseDto;
import com.example.user_registration_OTP.Models.UserModel;
import com.example.user_registration_OTP.Services.RabbitMQService;
import com.example.user_registration_OTP.Services.UserModelService;



@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserModelController {

    private final  UserModelService userModelService;

    private final  ModelMapper modelMapper;

private final RabbitMQService rabbitMQService;


@PostMapping("/login")
public ResponseEntity<?> loginUser(@RequestBody UserModelRequestDto userModel) {
    if (userModel == null || userModel.getEmail() == null || userModel.getPassword() == null) {
        return ResponseEntity.badRequest().body("Email and password are required.");
    }

    try {
        UserModel existingUser = userModelService.findByEmail(userModel.getEmail());
        if (existingUser == null) {
            return ResponseEntity.status(404).body("User not found.");
        }

        // Verify password
        boolean isPasswordMatch = userModelService.verifyPassword(userModel.getPassword(), existingUser.getPassword());
        if (!isPasswordMatch) {
            return ResponseEntity.status(401).body("Invalid credentials.");
        }

        // Generate JWT token
        String jwtToken = userModelService.generateJwtToken(existingUser);
        return ResponseEntity.ok(jwtToken);
    } catch (Exception e) {
        return ResponseEntity.status(500).body("An error occurred during login.");
    }
}

    @PostMapping("/create")
    public ResponseEntity<UserModelResponseDto> RegisterUser(@RequestBody UserModelRequestDto userModel) {
        if (userModel == null || userModel.getEmail() == null || userModel.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        UserModel savedUser = null;
    // Check if the user already exists by email
    UserModel existingUser = userModelService.findByEmail(userModel.getEmail());
    if (existingUser != null) {
        return ResponseEntity.badRequest().body(null);
    }

       try{
        UserModel userToSave = modelMapper.map(userModel, UserModel.class);
         savedUser = userModelService.saveUser(userToSave);
          // Generate a 6-digit OTP
        int otpCode = (int) (Math.random() * 900000) + 100000;

        // Send OTP event to RabbitMQ
        rabbitMQService.sendOtpEvent(savedUser.getEmail(), otpCode);
    
       }catch (Exception e) {
           return ResponseEntity.status(500).body(null);
       }
        
        return ResponseEntity.ok(modelMapper.map(savedUser, UserModelResponseDto.class));
    }



    @GetMapping("/details")
    public ResponseEntity<UserModelResponseDto> getMyDetails(Authentication authentication) {
        String email = authentication.getName(); // email stored as subject in token

        UserModel user = userModelService.findByEmail(email);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        UserModelResponseDto responseDto = modelMapper.map(user, UserModelResponseDto.class);
        return ResponseEntity.ok(responseDto);
    }
    
}
