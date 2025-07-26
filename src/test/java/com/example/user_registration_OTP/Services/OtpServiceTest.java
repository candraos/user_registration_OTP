package com.example.user_registration_OTP.Services;

import com.example.user_registration_OTP.Models.OTP;
import com.example.user_registration_OTP.Models.UserModel;
import com.example.user_registration_OTP.Repositories.OtpRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OtpServiceTest {

    @Mock
    private OtpRepository otpRepository;

    @Mock
    private UserModelService userService;

    @InjectMocks
    private OtpService otpService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveOtp_Success() {
        String email = "test@example.com";
        int otpCode = 123456;

        UserModel user = new UserModel();
        user.setEmail(email);

        OTP otp = OTP.builder()
                .otpCode(otpCode)
                .isVerified(false)
                .expirationDate(LocalDateTime.now().plusMinutes(5))
                .user(user)
                .build();

        when(userService.findByEmail(email)).thenReturn(user);
        when(otpRepository.save(any(OTP.class))).thenReturn(otp);

        OTP savedOtp = otpService.saveOtp(email, otpCode);

        assertNotNull(savedOtp);
        assertEquals(otpCode, savedOtp.getOtpCode());
        assertEquals(user, savedOtp.getUser());
        verify(otpRepository, times(1)).save(any(OTP.class));
    }

    @Test
    void testFindByUser_Success() {
        UserModel user = new UserModel();
        user.setEmail("test@example.com");

        OTP otp = new OTP();
        otp.setOtpCode(123456);
        otp.setUser(user);

        when(otpRepository.findByUser(user)).thenReturn(otp);

        OTP foundOtp = otpService.findByUser(user);

        assertNotNull(foundOtp);
        assertEquals(123456, foundOtp.getOtpCode());
        assertEquals(user, foundOtp.getUser());
        verify(otpRepository, times(1)).findByUser(user);
    }

    @Test
    void testValidateOtp_Success() {
        OTP otp = new OTP();
        otp.setOtpCode(123456);
        otp.setIsVerified(false);

        when(otpRepository.save(otp)).thenReturn(otp);

        otpService.validateOtp(otp);

        assertTrue(otp.getIsVerified());
        verify(otpRepository, times(1)).save(otp);
    }
}
