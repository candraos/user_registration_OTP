package com.example.user_registration_OTP.Controllers;

import com.example.user_registration_OTP.DTO.UserModelRequestDto;
import com.example.user_registration_OTP.DTO.UserModelResponseDto;
import com.example.user_registration_OTP.Models.UserModel;
import com.example.user_registration_OTP.Services.RabbitMQService;
import com.example.user_registration_OTP.Services.UserModelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UserModelControllerTest {

    @Mock
    private UserModelService userModelService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private RabbitMQService rabbitMQService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserModelController userModelController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        UserModelRequestDto requestDto = new UserModelRequestDto();
        requestDto.setEmail("test@example.com");
        requestDto.setName("Test User");
        requestDto.setPassword("password123");
        requestDto.setAge(25);

        UserModel userModel = new UserModel();
        userModel.setEmail("test@example.com");

        when(modelMapper.map(requestDto, UserModel.class)).thenReturn(userModel);
        when(userModelService.saveUser(userModel)).thenReturn(userModel);
        when(modelMapper.map(userModel, UserModelResponseDto.class)).thenReturn(new UserModelResponseDto());

        ResponseEntity<UserModelResponseDto> response = userModelController.RegisterUser(requestDto);

        assertEquals(200, response.getStatusCodeValue());
        verify(rabbitMQService, times(1)).sendOtpEvent(eq("test@example.com"), anyInt());
    }

    @Test
    void testRegisterUser_UserAlreadyExists() {
        UserModelRequestDto requestDto = new UserModelRequestDto();
        requestDto.setEmail("test@example.com");

        when(userModelService.findByEmail("test@example.com")).thenReturn(new UserModel());

        ResponseEntity<UserModelResponseDto> response = userModelController.RegisterUser(requestDto);

        assertEquals(400, response.getStatusCodeValue());
        verify(rabbitMQService, never()).sendOtpEvent(anyString(), anyInt());
    }

    @Test
    void testLoginUser_Success() {
        UserModelRequestDto requestDto = new UserModelRequestDto();
        requestDto.setEmail("test@example.com");
        requestDto.setPassword("password123");

        UserModel userModel = new UserModel();
        userModel.setEmail("test@example.com");
        userModel.setPassword("hashedPassword");

        when(userModelService.findByEmail("test@example.com")).thenReturn(userModel);
        when(userModelService.verifyPassword("password123", "hashedPassword")).thenReturn(true);
        when(userModelService.generateJwtToken(userModel)).thenReturn("jwtToken");
        when(authenticationManager.authenticate(any())).thenReturn(mock(Authentication.class));

        ResponseEntity<?> response = userModelController.loginUser(requestDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("jwtToken", response.getBody());
    }

    @Test
    void testLoginUser_InvalidCredentials() {
        UserModelRequestDto requestDto = new UserModelRequestDto();
        requestDto.setEmail("test@example.com");
        requestDto.setPassword("wrongPassword");

        UserModel userModel = new UserModel();
        userModel.setEmail("test@example.com");
        userModel.setPassword("hashedPassword");

        when(userModelService.findByEmail("test@example.com")).thenReturn(userModel);
        when(userModelService.verifyPassword("wrongPassword", "hashedPassword")).thenReturn(false);

        ResponseEntity<?> response = userModelController.loginUser(requestDto);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Invalid credentials.", response.getBody());
    }

    @Test
    void testGetMyDetails_Success() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@example.com");

        UserModel userModel = new UserModel();
        userModel.setEmail("test@example.com");

        UserModelResponseDto responseDto = new UserModelResponseDto();
        responseDto.setEmail("test@example.com");

        when(userModelService.findByEmail("test@example.com")).thenReturn(userModel);
        when(modelMapper.map(userModel, UserModelResponseDto.class)).thenReturn(responseDto);

        ResponseEntity<UserModelResponseDto> response = userModelController.getMyDetails(authentication);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseDto, response.getBody());
    }

    @Test
    void testGetMyDetails_UserNotFound() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@example.com");

        when(userModelService.findByEmail("test@example.com")).thenReturn(null);

        ResponseEntity<UserModelResponseDto> response = userModelController.getMyDetails(authentication);

        assertEquals(404, response.getStatusCodeValue());
    }
}
