package com.example.user_registration_OTP.Controllers;

import com.example.user_registration_OTP.DTO.OTPRequestDto;
import com.example.user_registration_OTP.Models.OTP;
import com.example.user_registration_OTP.Models.UserModel;
import com.example.user_registration_OTP.Services.OtpService;
import com.example.user_registration_OTP.Services.UserModelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OtpControllerTest {

    @Mock
    private UserModelService userService;

    @Mock
    private OtpService otpService;

    @InjectMocks
    private OtpController otpController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testVerify_Success() {
        OTPRequestDto otpRequestDto = new OTPRequestDto();
        otpRequestDto.setEmail("test@example.com");
        otpRequestDto.setOtpCode(123456);

        UserModel user = new UserModel();
        user.setEmail("test@example.com");

        OTP otp = new OTP();
        otp.setOtpCode(123456);
        otp.setExpirationDate(LocalDateTime.now().plusMinutes(5));

        when(userService.findByEmail("test@example.com")).thenReturn(user);
        when(otpService.findByUser(user)).thenReturn(otp);

        ResponseEntity<Object> response = otpController.verify(otpRequestDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("OTP verified successfully for email: test@example.com", response.getBody());
        verify(userService, times(1)).activateUser(user);
        verify(otpService, times(1)).validateOtp(otp);
    }

    @Test
    void testVerify_InvalidRequest() {
        OTPRequestDto otpRequestDto = new OTPRequestDto();

        ResponseEntity<Object> response = otpController.verify(otpRequestDto);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid request", response.getBody());
    }

    @Test
    void testVerify_UserNotFound() {
        OTPRequestDto otpRequestDto = new OTPRequestDto();
        otpRequestDto.setEmail("test@example.com");

        when(userService.findByEmail("test@example.com")).thenReturn(null);

        ResponseEntity<Object> response = otpController.verify(otpRequestDto);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("User not found for the provided email", response.getBody());
    }

    @Test
    void testVerify_WrongOtpCode() {
        OTPRequestDto otpRequestDto = new OTPRequestDto();
        otpRequestDto.setEmail("test@example.com");
        otpRequestDto.setOtpCode(123456);

        UserModel user = new UserModel();
        user.setEmail("test@example.com");

        OTP otp = new OTP();
        otp.setOtpCode(654321);

        when(userService.findByEmail("test@example.com")).thenReturn(user);
        when(otpService.findByUser(user)).thenReturn(otp);

        ResponseEntity<Object> response = otpController.verify(otpRequestDto);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Wrong OTP code", response.getBody());
    }

    @Test
    void testVerify_ExpiredOtpCode() {
        OTPRequestDto otpRequestDto = new OTPRequestDto();
        otpRequestDto.setEmail("test@example.com");
        otpRequestDto.setOtpCode(123456);

        UserModel user = new UserModel();
        user.setEmail("test@example.com");

        OTP otp = new OTP();
        otp.setOtpCode(123456);
        otp.setExpirationDate(LocalDateTime.now().minusMinutes(5));

        when(userService.findByEmail("test@example.com")).thenReturn(user);
        when(otpService.findByUser(user)).thenReturn(otp);

        ResponseEntity<Object> response = otpController.verify(otpRequestDto);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("OTP code is expired", response.getBody());
    }
}
