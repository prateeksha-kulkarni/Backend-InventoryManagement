package com.storeInventory.inventory_management.auth.controller;

import com.storeInventory.inventory_management.auth.dto.request.ForgotPasswordRequest;
import com.storeInventory.inventory_management.auth.dto.request.LoginRequest;
import com.storeInventory.inventory_management.auth.dto.request.ResetPasswordRequest;
import com.storeInventory.inventory_management.auth.dto.response.LoginResponse;
import com.storeInventory.inventory_management.auth.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.atLeast;
import java.lang.reflect.Field;

@Timeout(10)
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    private AuthController authController;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        authController = new AuthController();
        Field authServiceField = AuthController.class.getDeclaredField("authService");
        authServiceField.setAccessible(true);
        authServiceField.set(authController, authService);
    }

    @Test
    public void testLoginWithValidRequest() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");
        loginRequest.setRole("ADMIN");
        LoginResponse expectedResponse = new LoginResponse();
        expectedResponse.setToken("jwt-token-123");
        expectedResponse.setRole("ADMIN");
        expectedResponse.setUserId("user123");
        doReturn(expectedResponse).when(authService).login(loginRequest);
        LoginResponse actualResponse = authController.login(loginRequest);
        assertNotNull(actualResponse);
        assertThat(actualResponse.getToken(), is(equalTo("jwt-token-123")));
        assertThat(actualResponse.getRole(), is(equalTo("ADMIN")));
        assertThat(actualResponse.getUserId(), is(equalTo("user123")));
        verify(authService, atLeast(1)).login(loginRequest);
    }

    @Test
    public void testLoginWithDifferentRole() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("manager");
        loginRequest.setPassword("pass456");
        loginRequest.setRole("MANAGER");
        LoginResponse expectedResponse = new LoginResponse();
        expectedResponse.setToken("jwt-token-456");
        expectedResponse.setRole("MANAGER");
        expectedResponse.setStoreId("store123");
        expectedResponse.setLocation("New York");
        doReturn(expectedResponse).when(authService).login(loginRequest);
        LoginResponse actualResponse = authController.login(loginRequest);
        assertNotNull(actualResponse);
        assertThat(actualResponse.getToken(), is(equalTo("jwt-token-456")));
        assertThat(actualResponse.getRole(), is(equalTo("MANAGER")));
        assertThat(actualResponse.getStoreId(), is(equalTo("store123")));
        assertThat(actualResponse.getLocation(), is(equalTo("New York")));
        verify(authService, atLeast(1)).login(loginRequest);
    }

    @Test
    public void testForgotPasswordWithValidEmail() {
        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
        forgotPasswordRequest.setEmail("test@example.com");
        String expectedMessage = "OTP sent successfully to your email";
        doReturn(expectedMessage).when(authService).sendOtpToEmail("test@example.com");
        ResponseEntity<String> response = authController.forgotPassword(forgotPasswordRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody(), is(equalTo(expectedMessage)));
        verify(authService, atLeast(1)).sendOtpToEmail("test@example.com");
    }

    @Test
    public void testForgotPasswordWithDifferentEmail() {
        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
        forgotPasswordRequest.setEmail("user@domain.com");
        String expectedMessage = "Password reset OTP has been sent";
        doReturn(expectedMessage).when(authService).sendOtpToEmail("user@domain.com");
        ResponseEntity<String> response = authController.forgotPassword(forgotPasswordRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody(), is(equalTo(expectedMessage)));
        verify(authService, atLeast(1)).sendOtpToEmail("user@domain.com");
    }

    @Test
    public void testVerifyOtpWithValidCredentials() {
        Map<String, String> request = new HashMap<>();
        request.put("email", "test@example.com");
        request.put("otp", "123456");
        doReturn(true).when(authService).verifyOtp("test@example.com", "123456");
        ResponseEntity<Map<String, Object>> response = authController.verifyOtp(request);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody(), is(notNullValue()));
        Map<String, Object> responseBody = response.getBody();
        assertThat(responseBody.get("valid"), is(equalTo(true)));
        assertThat(responseBody.get("message"), is(equalTo("OTP verified successfully")));
        verify(authService, atLeast(1)).verifyOtp("test@example.com", "123456");
    }

    @Test
    public void testVerifyOtpWithInvalidCredentials() {
        Map<String, String> request = new HashMap<>();
        request.put("email", "test@example.com");
        request.put("otp", "wrong-otp");
        doReturn(false).when(authService).verifyOtp("test@example.com", "wrong-otp");
        ResponseEntity<Map<String, Object>> response = authController.verifyOtp(request);
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertThat(response.getBody(), is(notNullValue()));
        Map<String, Object> responseBody = response.getBody();
        assertThat(responseBody.get("valid"), is(equalTo(false)));
        assertThat(responseBody.get("message"), is(equalTo("Invalid or expired OTP")));
        verify(authService, atLeast(1)).verifyOtp("test@example.com", "wrong-otp");
    }

    @Test
    public void testVerifyOtpWithDifferentEmailAndOtp() {
        Map<String, String> request = new HashMap<>();
        request.put("email", "another@test.com");
        request.put("otp", "654321");
        doReturn(true).when(authService).verifyOtp("another@test.com", "654321");
        ResponseEntity<Map<String, Object>> response = authController.verifyOtp(request);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody(), is(notNullValue()));
        Map<String, Object> responseBody = response.getBody();
        assertThat(responseBody.get("valid"), is(equalTo(true)));
        assertThat(responseBody.get("message"), is(equalTo("OTP verified successfully")));
        verify(authService, atLeast(1)).verifyOtp("another@test.com", "654321");
    }

    @Test
    public void testResetPasswordWithValidRequest() {
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setEmail("test@example.com");
        resetPasswordRequest.setNewPassword("newPassword123");
        String expectedMessage = "Password reset successfully";
        doReturn(expectedMessage).when(authService).resetPassword("test@example.com", "newPassword123");
        ResponseEntity<String> response = authController.resetPassword(resetPasswordRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody(), is(equalTo(expectedMessage)));
        verify(authService, atLeast(1)).resetPassword("test@example.com", "newPassword123");
    }

    @Test
    public void testResetPasswordWithDifferentCredentials() {
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setEmail("user@domain.com");
        resetPasswordRequest.setNewPassword("strongPassword456");
        String expectedMessage = "Your password has been updated successfully";
        doReturn(expectedMessage).when(authService).resetPassword("user@domain.com", "strongPassword456");
        ResponseEntity<String> response = authController.resetPassword(resetPasswordRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody(), is(equalTo(expectedMessage)));
        verify(authService, atLeast(1)).resetPassword("user@domain.com", "strongPassword456");
    }
}
