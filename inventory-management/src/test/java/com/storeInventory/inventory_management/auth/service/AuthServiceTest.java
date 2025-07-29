package com.storeInventory.inventory_management.auth.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.storeInventory.inventory_management.auth.dto.request.LoginRequest;
import com.storeInventory.inventory_management.auth.dto.response.LoginResponse;
import com.storeInventory.inventory_management.auth.model.Enum.UserRole;
import com.storeInventory.inventory_management.auth.model.StoreEntity;
import com.storeInventory.inventory_management.auth.model.UserEntity;
import com.storeInventory.inventory_management.auth.repository.UserRepository;
import com.storeInventory.inventory_management.auth.util.JwtUtil;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@Timeout(10)
public class AuthServiceTest {

    private UserRepository userRepository;

    private JwtUtil jwtUtil;

    private AuthenticationManager authenticationManager;

    private EmailService emailService;

    private PasswordEncoder passwordEncoder;

    private AuthService authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userRepository = mock(UserRepository.class);
        jwtUtil = mock(JwtUtil.class);
        authenticationManager = mock(AuthenticationManager.class);
        emailService = mock(EmailService.class);
        passwordEncoder = mock(PasswordEncoder.class);
        authService = new AuthService(userRepository, jwtUtil, authenticationManager, emailService, passwordEncoder);
    }

    @Test
    public void testLoginWithValidCredentialsAndMatchingRole() throws Exception {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");
        loginRequest.setRole("MANAGER");
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        UUID userId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userId);
        userEntity.setUsername("testuser");
        userEntity.setRole(UserRole.MANAGER);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(userEntity));
        when(jwtUtil.generateToken("testuser", "MANAGER")).thenReturn("jwt-token");
        // Act
        LoginResponse result = authService.login(loginRequest);
        // Assert
        assertThat(result, is(notNullValue()));
        assertEquals("jwt-token", result.getToken());
        assertEquals("MANAGER", result.getRole());
        assertEquals(userId.toString(), result.getUserId());
        verify(authenticationManager, atLeast(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, atLeast(1)).findByUsername("testuser");
        verify(jwtUtil, atLeast(1)).generateToken("testuser", "MANAGER");
    }

    @Test
    public void testLoginWithValidCredentialsAndStore() throws Exception {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("storeuser");
        loginRequest.setPassword("password");
        loginRequest.setRole("ASSOCIATE");
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        UUID userId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        UUID storeId = UUID.fromString("550e8400-e29b-41d4-a716-446655440002");
        StoreEntity storeEntity = new StoreEntity();
        storeEntity.setStoreId(storeId);
        storeEntity.setName("Main Store");
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userId);
        userEntity.setUsername("storeuser");
        userEntity.setRole(UserRole.ASSOCIATE);
        userEntity.setStore(storeEntity);
        when(userRepository.findByUsername("storeuser")).thenReturn(Optional.of(userEntity));
        when(jwtUtil.generateToken("storeuser", "ASSOCIATE")).thenReturn("jwt-token-store");
        // Act
        LoginResponse result = authService.login(loginRequest);
        // Assert
        assertThat(result, is(notNullValue()));
        assertEquals("jwt-token-store", result.getToken());
        assertEquals("ASSOCIATE", result.getRole());
        assertEquals(userId.toString(), result.getUserId());
        assertEquals(storeId.toString(), result.getStoreId());
        assertEquals("Main Store", result.getLocation());
        verify(authenticationManager, atLeast(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, atLeast(1)).findByUsername("storeuser");
        verify(jwtUtil, atLeast(1)).generateToken("storeuser", "ASSOCIATE");
    }

    @Test
    public void testLoginWithUserNotFound() throws Exception {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("nonexistent");
        loginRequest.setPassword("password");
        loginRequest.setRole("MANAGER");
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> authService.login(loginRequest));
        verify(authenticationManager, atLeast(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, atLeast(1)).findByUsername("nonexistent");
    }

    @Test
    public void testLoginWithRoleMismatch() throws Exception {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");
        loginRequest.setRole("ADMIN");
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        UUID userId = UUID.fromString("550e8400-e29b-41d4-a716-446655440003");
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userId);
        userEntity.setUsername("testuser");
        userEntity.setRole(UserRole.MANAGER);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(userEntity));
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
        assertEquals("Role mismatch. Please select the correct role.", exception.getMessage());
        verify(authenticationManager, atLeast(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, atLeast(1)).findByUsername("testuser");
    }

    @Test
    public void testSendOtpToEmailWithValidEmail() throws Exception {
        // Arrange
        String email = "test@example.com";
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
        when(emailService.sendOtpEmail(email)).thenReturn("OTP sent successfully");
        // Act
        String result = authService.sendOtpToEmail(email);
        // Assert
        assertEquals("OTP sent successfully", result);
        verify(userRepository, atLeast(1)).findByEmail(email);
        verify(emailService, atLeast(1)).sendOtpEmail(email);
    }

    @Test
    public void testSendOtpToEmailWithUnregisteredEmail() throws Exception {
        // Arrange
        String email = "unregistered@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> authService.sendOtpToEmail(email));
        verify(userRepository, atLeast(1)).findByEmail(email);
    }

    @Test
    public void testVerifyOtpWithValidOtp() throws Exception {
        // Arrange
        String email = "test@example.com";
        String otp = "123456";
        when(emailService.verifyOtp(email, otp)).thenReturn(true);
        // Act
        boolean result = authService.verifyOtp(email, otp);
        // Assert
        assertTrue(result);
        verify(emailService, atLeast(1)).verifyOtp(email, otp);
    }

    @Test
    public void testVerifyOtpWithInvalidOtp() throws Exception {
        // Arrange
        String email = "test@example.com";
        String otp = "wrong-otp";
        when(emailService.verifyOtp(email, otp)).thenReturn(false);
        // Act
        boolean result = authService.verifyOtp(email, otp);
        // Assert
        assertFalse(result);
        verify(emailService, atLeast(1)).verifyOtp(email, otp);
    }

    @Test
    public void testResetPasswordWithValidEmail() throws Exception {
        // Arrange
        String email = "test@example.com";
        String newPassword = "newPassword123";
        String encodedPassword = "encodedPassword123";
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        // Act
        String result = authService.resetPassword(email, newPassword);
        // Assert
        assertEquals("Password reset successful", result);
        verify(userRepository, atLeast(1)).findByEmail(email);
        verify(passwordEncoder, atLeast(1)).encode(newPassword);
        verify(userRepository, atLeast(1)).save(userEntity);
    }

    @Test
    public void testResetPasswordWithUnregisteredEmail() throws Exception {
        // Arrange
        String email = "unregistered@example.com";
        String newPassword = "newPassword123";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> authService.resetPassword(email, newPassword));
        verify(userRepository, atLeast(1)).findByEmail(email);
    }
}
