package com.storeInventory.inventory_management.controller;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.storeInventory.inventory_management.auth.controller.AuthController;
import com.storeInventory.inventory_management.auth.dto.request.ForgotPasswordRequest;
import com.storeInventory.inventory_management.auth.dto.request.LoginRequest;
import com.storeInventory.inventory_management.auth.dto.request.ResetPasswordRequest;
import com.storeInventory.inventory_management.auth.dto.response.LoginResponse;
import com.storeInventory.inventory_management.auth.exception.GlobalExceptionHandler;
import com.storeInventory.inventory_management.auth.service.AuthService;

import java.util.HashMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {AuthController.class, GlobalExceptionHandler.class})
@DisabledInAotMode
@ExtendWith(SpringExtension.class)
class AuthControllerTest {
    @Autowired
    private AuthController authController;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;


    @Test
    @DisplayName("Test forgotPassword(ForgotPasswordRequest)")
    void testForgotPassword() throws Exception {
        // Arrange
        when(authService.sendOtpToEmail(Mockito.<String>any())).thenReturn("jane.doe@example.org");
        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
        forgotPasswordRequest.setEmail("jane.doe@example.org");
        String content = new ObjectMapper().writeValueAsString(forgotPasswordRequest);
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post("/api/auth/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("jane.doe@example.org"));
    }


    @Test
    @DisplayName("Test login(LoginRequest)")
    void testLogin() throws Exception {
        // Arrange
        when(authService.login(Mockito.<LoginRequest>any()))
                .thenReturn(new LoginResponse("ABC123", "Role"));
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("iloveyou");
        loginRequest.setRole("Role");
        loginRequest.setUsername("janedoe");
        String content = new ObjectMapper().writeValueAsString(loginRequest);
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content()
                                .string(
                                        "{\"token\":\"ABC123\",\"role\":\"Role\",\"storeId\":null,\"location\":null,\"userId\":null}"));
    }


    @Test
    @DisplayName(
            "Test verifyOtp(Map); given AuthService verifyOtp(String, String) return 'false'; then status isUnauthorized()")
    void testVerifyOtp_givenAuthServiceVerifyOtpReturnFalse_thenStatusIsUnauthorized()
            throws Exception {
        // Arrange
        when(authService.verifyOtp(Mockito.<String>any(), Mockito.<String>any())).thenReturn(false);

        HashMap<String, String> stringStringMap = new HashMap<>();
        stringStringMap.put("email", "foo");
        stringStringMap.put("otp", "foo");
        String content = new ObjectMapper().writeValueAsString(stringStringMap);
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post("/api/auth/verify-otp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content()
                                .string("{\"message\":\"Invalid or expired OTP\",\"valid\":false}"));
    }


    @Test
    @DisplayName(
            "Test verifyOtp(Map); given AuthService verifyOtp(String, String) return 'true'; then status isOk()")
    void testVerifyOtp_givenAuthServiceVerifyOtpReturnTrue_thenStatusIsOk() throws Exception {
        // Arrange
        when(authService.verifyOtp(Mockito.<String>any(), Mockito.<String>any())).thenReturn(true);
        HashMap<String, String> stringStringMap = new HashMap<>();
        stringStringMap.put("email", "foo");
        stringStringMap.put("otp", "foo");
        String content = new ObjectMapper().writeValueAsString(stringStringMap);
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post("/api/auth/verify-otp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content()
                                .string("{\"message\":\"OTP verified successfully\",\"valid\":true}"));
    }


    @Test
    @DisplayName("Test resetPassword(ResetPasswordRequest)")
    void testResetPassword() throws Exception {
        // Arrange
        when(authService.resetPassword(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn("iloveyou");
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setEmail("jane.doe@example.org");
        resetPasswordRequest.setNewPassword("iloveyou");
        String content = new ObjectMapper().writeValueAsString(resetPasswordRequest);
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post("/api/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("iloveyou"));
    }
}
