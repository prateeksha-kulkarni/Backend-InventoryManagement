package com.storeInventory.inventory_management.controller;

import com.storeInventory.inventory_management.auth.controller.AuthController;
import com.storeInventory.inventory_management.auth.dto.request.ForgotPasswordRequest;
import com.storeInventory.inventory_management.auth.dto.request.LoginRequest;
import com.storeInventory.inventory_management.auth.dto.request.ResetPasswordRequest;
import com.storeInventory.inventory_management.auth.dto.response.LoginResponse;
import com.storeInventory.inventory_management.auth.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(
    controllers = AuthController.class,
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfiguration.class)
)
public class AuthControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthService authService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testLogin() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("password123");
        request.setRole("ADMIN");
        String requestBody = objectMapper.writeValueAsString(request);

        LoginResponse response = new LoginResponse("jwt-token-123", "ADMIN");
        response.setUserId("user-123");

        when(authService.login(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token")
                        .value(response.getToken()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role")
                        .value(response.getRole()));
    }

    @Test
    public void testForgotPassword() throws Exception {
        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setEmail("test@example.com");
        String requestBody = objectMapper.writeValueAsString(request);

        when(authService.sendOtpToEmail(anyString())).thenReturn("OTP sent successfully");

        mockMvc.perform(post("/api/auth/forgot-password")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("OTP sent successfully"));
    }

    @Test
    public void testVerifyOtp_Success() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("email", "test@example.com");
        request.put("otp", "123456");
        String requestBody = objectMapper.writeValueAsString(request);

        when(authService.verifyOtp(anyString(), anyString())).thenReturn(true);

        mockMvc.perform(post("/api/auth/verify-otp")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.valid").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("OTP verified successfully"));
    }

    @Test
    public void testVerifyOtp_Failure() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("email", "test@example.com");
        request.put("otp", "123456");
        String requestBody = objectMapper.writeValueAsString(request);

        when(authService.verifyOtp(anyString(), anyString())).thenReturn(false);

        mockMvc.perform(post("/api/auth/verify-otp")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.valid").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Invalid or expired OTP"));
    }

    @Test
    public void testResetPassword() throws Exception {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setEmail("test@example.com");
        request.setNewPassword("newpassword123");
        String requestBody = objectMapper.writeValueAsString(request);

        when(authService.resetPassword(anyString(), anyString())).thenReturn("Password reset successfully");

        mockMvc.perform(post("/api/auth/reset-password")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Password reset successfully"));
    }
} 