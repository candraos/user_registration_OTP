package com.example.user_registration_OTP.Services;

import com.example.user_registration_OTP.Models.UserModel;
import com.example.user_registration_OTP.Repositories.UserModelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserModelServiceTest {

    @Mock
    private UserModelRepository userModelRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserModelService userModelService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveUser_Success() {
        UserModel user = new UserModel();
        user.setPassword("plainPassword");

        when(passwordEncoder.encode("plainPassword")).thenReturn("hashedPassword");
        when(userModelRepository.save(any(UserModel.class))).thenReturn(user);

        UserModel savedUser = userModelService.saveUser(user);

        assertNotNull(savedUser);
        assertEquals("hashedPassword", savedUser.getPassword());
        assertFalse(savedUser.getIsActive());
        verify(userModelRepository, times(1)).save(user);
    }

    @Test
    void testFindByEmail_Success() {
        UserModel user = new UserModel();
        user.setEmail("test@example.com");

        when(userModelRepository.findByEmail("test@example.com")).thenReturn(user);

        UserModel foundUser = userModelService.findByEmail("test@example.com");

        assertNotNull(foundUser);
        assertEquals("test@example.com", foundUser.getEmail());
        verify(userModelRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void testActivateUser_Success() {
        UserModel user = new UserModel();
        user.setIsActive(false);

        userModelService.activateUser(user);

        assertTrue(user.getIsActive());
        verify(userModelRepository, times(1)).save(user);
    }

    @Test
    void testVerifyPassword_Success() {
        when(passwordEncoder.matches("plainPassword", "hashedPassword")).thenReturn(true);

        boolean isMatch = userModelService.verifyPassword("plainPassword", "hashedPassword");

        assertTrue(isMatch);
        verify(passwordEncoder, times(1)).matches("plainPassword", "hashedPassword");
    }

    @Test
    void testGenerateJwtToken_Success() {
        UserModel user = new UserModel();
        user.setEmail("test@example.com");

        String jwtToken = userModelService.generateJwtToken(user);

        assertNotNull(jwtToken);
        assertTrue(jwtToken.length() > 0);
    }
}
