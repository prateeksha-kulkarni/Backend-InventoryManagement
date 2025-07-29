package com.storeInventory.inventory_management.auth.service;

import com.storeInventory.inventory_management.auth.dto.request.UserRegistrationRequest;
import com.storeInventory.inventory_management.auth.model.Enum.UserRole;
import com.storeInventory.inventory_management.auth.model.StoreEntity;
import com.storeInventory.inventory_management.auth.model.UserEntity;
import com.storeInventory.inventory_management.auth.repository.StoreRepository;
import com.storeInventory.inventory_management.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Timeout(10)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private StoreRepository storeRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, passwordEncoder, storeRepository);
    }

    @Test
    void testGetAllUsers() {
        UserEntity user1 = new UserEntity();
        user1.setUsername("user1");
        UserEntity user2 = new UserEntity();
        user2.setUsername("user2");
        List<UserEntity> expectedUsers = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(expectedUsers);
        List<UserEntity> result = userService.getAllUsers();
        assertThat(result, is(sameInstance(expectedUsers)));
        verify(userRepository, atLeast(1)).findAll();
    }

    @Test
    void testGetUserByUsernameFound() {
        String username = "testuser";
        UserEntity expectedUser = new UserEntity();
        expectedUser.setUsername(username);
        Optional<UserEntity> expectedOptional = Optional.of(expectedUser);
        when(userRepository.findByUsername(username)).thenReturn(expectedOptional);
        Optional<UserEntity> result = userService.getUserByUsername(username);
        assertThat(result, is(sameInstance(expectedOptional)));
        verify(userRepository, atLeast(1)).findByUsername(eq(username));
    }

    @Test
    void testGetUserByUsernameNotFound() {
        String username = "nonexistent";
        Optional<UserEntity> expectedOptional = Optional.empty();
        when(userRepository.findByUsername(username)).thenReturn(expectedOptional);
        Optional<UserEntity> result = userService.getUserByUsername(username);
        assertThat(result, is(sameInstance(expectedOptional)));
        verify(userRepository, atLeast(1)).findByUsername(eq(username));
    }

    @Test
    void testGetUserByEmailFound() {
        String email = "test@example.com";
        UserEntity expectedUser = new UserEntity();
        expectedUser.setEmail(email);
        Optional<UserEntity> expectedOptional = Optional.of(expectedUser);
        when(userRepository.findByEmail(email)).thenReturn(expectedOptional);
        Optional<UserEntity> result = userService.getUserByEmail(email);
        assertThat(result, is(sameInstance(expectedOptional)));
        verify(userRepository, atLeast(1)).findByEmail(eq(email));
    }

    @Test
    void testGetUserByEmailNotFound() {
        String email = "nonexistent@example.com";
        Optional<UserEntity> expectedOptional = Optional.empty();
        when(userRepository.findByEmail(email)).thenReturn(expectedOptional);
        Optional<UserEntity> result = userService.getUserByEmail(email);
        assertThat(result, is(sameInstance(expectedOptional)));
        verify(userRepository, atLeast(1)).findByEmail(eq(email));
    }

    @Test
    void testGetUserByIdFound() {
        UUID userId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UserEntity expectedUser = new UserEntity();
        expectedUser.setUserId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));
        UserEntity result = userService.getUserById(userId);
        assertThat(result, is(sameInstance(expectedUser)));
        verify(userRepository, atLeast(1)).findById(eq(userId));
    }

    @Test
    void testGetUserByIdNotFound() {
        UUID userId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getUserById(userId));
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, atLeast(1)).findById(eq(userId));
    }

    @Test
    void testCreateUser() {
        UserEntity user = new UserEntity();
        user.setUsername("testuser");
        UserEntity savedUser = new UserEntity();
        savedUser.setUsername("testuser");
        when(userRepository.save(user)).thenReturn(savedUser);
        UserEntity result = userService.createUser(user);
        assertThat(result, is(sameInstance(savedUser)));
        verify(userRepository, atLeast(1)).save(eq(user));
    }

    @Test
    void testRegisterUserWithoutLocation() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setUsername("testuser");
        request.setName("Test User");
        request.setPassword("password123");
        request.setRole("MANAGER");
        request.setEmail("test@example.com");
        request.setLocation(null);
        String encodedPassword = "encodedPassword123";
        when(passwordEncoder.encode("password123")).thenReturn(encodedPassword);
        UserEntity savedUser = new UserEntity();
        savedUser.setUsername("testuser");
        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUser);
        UserEntity result = userService.registerUser(request);
        assertThat(result, is(sameInstance(savedUser)));
        verify(passwordEncoder, atLeast(1)).encode(eq("password123"));
        verify(userRepository, atLeast(1)).save(any(UserEntity.class));
    }

    @Test
    void testRegisterUserWithEmptyLocation() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setUsername("testuser");
        request.setName("Test User");
        request.setPassword("password123");
        request.setRole("ASSOCIATE");
        request.setEmail("test@example.com");
        request.setLocation("");
        String encodedPassword = "encodedPassword123";
        when(passwordEncoder.encode("password123")).thenReturn(encodedPassword);
        UserEntity savedUser = new UserEntity();
        savedUser.setUsername("testuser");
        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUser);
        UserEntity result = userService.registerUser(request);
        assertThat(result, is(sameInstance(savedUser)));
        verify(passwordEncoder, atLeast(1)).encode(eq("password123"));
        verify(userRepository, atLeast(1)).save(any(UserEntity.class));
    }

    @Test
    void testRegisterUserWithValidLocation() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setUsername("testuser");
        request.setName("Test User");
        request.setPassword("password123");
        request.setRole("ANALYST");
        request.setEmail("test@example.com");
        request.setLocation("Store1");
        String encodedPassword = "encodedPassword123";
        when(passwordEncoder.encode("password123")).thenReturn(encodedPassword);
        StoreEntity store = new StoreEntity();
        store.setName("Store1");
        when(storeRepository.findByName("Store1")).thenReturn(Optional.of(store));
        UserEntity savedUser = new UserEntity();
        savedUser.setUsername("testuser");
        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUser);
        UserEntity result = userService.registerUser(request);
        assertThat(result, is(sameInstance(savedUser)));
        verify(passwordEncoder, atLeast(1)).encode(eq("password123"));
        verify(storeRepository, atLeast(1)).findByName(eq("Store1"));
        verify(userRepository, atLeast(1)).save(any(UserEntity.class));
    }

    @Test
    void testRegisterUserWithInvalidLocation() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setUsername("testuser");
        request.setName("Test User");
        request.setPassword("password123");
        request.setRole("ADMIN");
        request.setEmail("test@example.com");
        request.setLocation("NonExistentStore");
        String encodedPassword = "encodedPassword123";
        when(passwordEncoder.encode("password123")).thenReturn(encodedPassword);
        when(storeRepository.findByName("NonExistentStore")).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.registerUser(request));
        assertEquals("Store not found for name: NonExistentStore", exception.getMessage());
        verify(passwordEncoder, atLeast(1)).encode(eq("password123"));
        verify(storeRepository, atLeast(1)).findByName(eq("NonExistentStore"));
    }

    @Test
    void testIsLoginValidUserNotFound() {
        String username = "nonexistent";
        String rawPassword = "password123";
        String role = "MANAGER";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        boolean result = userService.isLoginValid(username, rawPassword, role);
        assertFalse(result);
        verify(userRepository, atLeast(1)).findByUsername(eq(username));
    }

    @Test
    void testIsLoginValidPasswordMismatch() {
        String username = "testuser";
        String rawPassword = "wrongpassword";
        String role = "MANAGER";
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword("encodedCorrectPassword");
        user.setRole(UserRole.MANAGER);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, "encodedCorrectPassword")).thenReturn(false);
        boolean result = userService.isLoginValid(username, rawPassword, role);
        assertFalse(result);
        verify(userRepository, atLeast(1)).findByUsername(eq(username));
        verify(passwordEncoder, atLeast(1)).matches(eq(rawPassword), eq("encodedCorrectPassword"));
    }

    @Test
    void testIsLoginValidRoleMismatch() {
        String username = "testuser";
        String rawPassword = "password123";
        String role = "ASSOCIATE";
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword("encodedPassword");
        user.setRole(UserRole.MANAGER);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, "encodedPassword")).thenReturn(true);
        boolean result = userService.isLoginValid(username, rawPassword, role);
        assertFalse(result);
        verify(userRepository, atLeast(1)).findByUsername(eq(username));
        verify(passwordEncoder, atLeast(1)).matches(eq(rawPassword), eq("encodedPassword"));
    }

    @Test
    void testIsLoginValidSuccess() {
        String username = "testuser";
        String rawPassword = "password123";
        String role = "MANAGER";
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword("encodedPassword");
        user.setRole(UserRole.MANAGER);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, "encodedPassword")).thenReturn(true);
        boolean result = userService.isLoginValid(username, rawPassword, role);
        assertTrue(result);
        verify(userRepository, atLeast(1)).findByUsername(eq(username));
        verify(passwordEncoder, atLeast(1)).matches(eq(rawPassword), eq("encodedPassword"));
    }

    @Test
    void testIsLoginValidSuccessWithCaseInsensitiveRole() {
        String username = "testuser";
        String rawPassword = "password123";
        String role = "manager";
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword("encodedPassword");
        user.setRole(UserRole.MANAGER);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, "encodedPassword")).thenReturn(true);
        boolean result = userService.isLoginValid(username, rawPassword, role);
        assertTrue(result);
        verify(userRepository, atLeast(1)).findByUsername(eq(username));
        verify(passwordEncoder, atLeast(1)).matches(eq(rawPassword), eq("encodedPassword"));
    }

    @Test
    void testRegisterUserWithUppercaseRole() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setUsername("testuser");
        request.setName("Test User");
        request.setPassword("password123");
        request.setRole("MANAGER");
        request.setEmail("test@example.com");
        String encodedPassword = "encodedPassword123";
        when(passwordEncoder.encode("password123")).thenReturn(encodedPassword);
        UserEntity savedUser = new UserEntity();
        savedUser.setUsername("testuser");
        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUser);
        UserEntity result = userService.registerUser(request);
        assertNotNull(result);
        verify(passwordEncoder, atLeast(1)).encode(eq("password123"));
        verify(userRepository, atLeast(1)).save(any(UserEntity.class));
    }

    @Test
    void testRegisterUserWithLowercaseRole() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setUsername("testuser");
        request.setName("Test User");
        request.setPassword("password123");
        request.setRole("associate");
        request.setEmail("test@example.com");
        String encodedPassword = "encodedPassword123";
        when(passwordEncoder.encode("password123")).thenReturn(encodedPassword);
        UserEntity savedUser = new UserEntity();
        savedUser.setUsername("testuser");
        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUser);
        UserEntity result = userService.registerUser(request);
        assertNotNull(result);
        verify(passwordEncoder, atLeast(1)).encode(eq("password123"));
        verify(userRepository, atLeast(1)).save(any(UserEntity.class));
    }
}
