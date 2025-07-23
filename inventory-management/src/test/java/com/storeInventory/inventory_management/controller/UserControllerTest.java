package com.storeInventory.inventory_management.controller;

import com.storeInventory.inventory_management.auth.controller.UserController;
import com.storeInventory.inventory_management.auth.dto.request.LoginRequest;
import com.storeInventory.inventory_management.auth.dto.request.UserRegistrationRequest;
import com.storeInventory.inventory_management.auth.model.UserEntity;
import com.storeInventory.inventory_management.auth.model.Enum.UserRole;
import com.storeInventory.inventory_management.auth.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@Timeout(10)
public class UserControllerTest {

    @Mock
    private UserService userService;

    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userService);
    }

    @Test
    public void testRegisterUserSuccess() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setUsername("testuser");
        request.setPassword("password");
        request.setRole("ADMIN");
        UserEntity mockUser = new UserEntity();
        mockUser.setUsername("testuser");
        doReturn(mockUser).when(userService).registerUser(any(UserRegistrationRequest.class));
        ResponseEntity<String> response = userController.registerUser(request);
        assertThat(response.getStatusCodeValue(), is(equalTo(200)));
        assertThat(response.getBody(), is(equalTo("User testuser registered successfully")));
        verify(userService, atLeast(1)).registerUser(any(UserRegistrationRequest.class));
    }

    @Test
    public void testRegisterUserFailure() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setUsername("testuser");
        request.setPassword("password");
        request.setRole("ADMIN");
        doThrow(new RuntimeException("Registration error")).when(userService).registerUser(any(UserRegistrationRequest.class));
        ResponseEntity<String> response = userController.registerUser(request);
        assertThat(response.getStatusCodeValue(), is(equalTo(400)));
        assertThat(response.getBody(), is(equalTo("Registration failed: Registration error")));
        verify(userService, atLeast(1)).registerUser(any(UserRegistrationRequest.class));
    }

    @Test
    public void testLoginUserSuccess() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");
        loginRequest.setRole("ADMIN");
        UserEntity mockUser = new UserEntity();
        mockUser.setUsername("testuser");
        mockUser.setPassword("password");
        mockUser.setRole(UserRole.ADMIN);
        doReturn(Optional.of(mockUser)).when(userService).getUserByUsername(anyString());
        ResponseEntity<String> response = userController.loginUser(loginRequest);
        assertThat(response.getStatusCodeValue(), is(equalTo(200)));
        assertThat(response.getBody(), is(equalTo("Login successful")));
        verify(userService, atLeast(1)).getUserByUsername(anyString());
    }

    @Test
    public void testLoginUserNotFound() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("nonexistentuser");
        loginRequest.setPassword("password");
        loginRequest.setRole("ADMIN");
        doReturn(Optional.empty()).when(userService).getUserByUsername(anyString());
        ResponseEntity<String> response = userController.loginUser(loginRequest);
        assertThat(response.getStatusCodeValue(), is(equalTo(404)));
        assertThat(response.getBody(), is(equalTo("User not found")));
        verify(userService, atLeast(1)).getUserByUsername(anyString());
    }

    @Test
    public void testLoginUserInvalidPassword() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("wrongpassword");
        loginRequest.setRole("ADMIN");
        UserEntity mockUser = new UserEntity();
        mockUser.setUsername("testuser");
        mockUser.setPassword("correctpassword");
        mockUser.setRole(UserRole.ADMIN);
        doReturn(Optional.of(mockUser)).when(userService).getUserByUsername(anyString());
        ResponseEntity<String> response = userController.loginUser(loginRequest);
        assertThat(response.getStatusCodeValue(), is(equalTo(401)));
        assertThat(response.getBody(), is(equalTo("Invalid password")));
        verify(userService, atLeast(1)).getUserByUsername(anyString());
    }

    @Test
    public void testLoginUserRoleMismatch() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");
        loginRequest.setRole("MANAGER");
        UserEntity mockUser = new UserEntity();
        mockUser.setUsername("testuser");
        mockUser.setPassword("password");
        mockUser.setRole(UserRole.ADMIN);
        doReturn(Optional.of(mockUser)).when(userService).getUserByUsername(anyString());
        ResponseEntity<String> response = userController.loginUser(loginRequest);
        assertThat(response.getStatusCodeValue(), is(equalTo(403)));
        assertThat(response.getBody(), is(equalTo("Role mismatch. Please check the selected role.")));
        verify(userService, atLeast(1)).getUserByUsername(anyString());
    }

    @Test
    public void testGetAllUsers() {
        List<UserEntity> mockUsers = new ArrayList<>();
        UserEntity user1 = new UserEntity();
        user1.setUsername("user1");
        UserEntity user2 = new UserEntity();
        user2.setUsername("user2");
        mockUsers.add(user1);
        mockUsers.add(user2);
        doReturn(mockUsers).when(userService).getAllUsers();
        ResponseEntity<List<UserEntity>> response = userController.getAllUsers();
        assertThat(response.getStatusCodeValue(), is(equalTo(200)));
        assertNotNull(response.getBody());
        assertThat(response.getBody().size(), is(equalTo(2)));
        verify(userService, atLeast(1)).getAllUsers();
    }

    @Test
    public void testGetUserByUsername() {
        String username = "testuser";
        UserEntity mockUser = new UserEntity();
        mockUser.setUsername(username);
        doReturn(Optional.of(mockUser)).when(userService).getUserByUsername(anyString());
        ResponseEntity<Optional<UserEntity>> response = userController.getUserByUsername(username);
        assertThat(response.getStatusCodeValue(), is(equalTo(200)));
        assertNotNull(response.getBody());
        assertThat(response.getBody().isPresent(), is(equalTo(true)));
        assertThat(response.getBody().get().getUsername(), is(equalTo(username)));
        verify(userService, atLeast(1)).getUserByUsername(anyString());
    }

    @Test
    public void testGetUserByEmail() {
        String email = "test@example.com";
        UserEntity mockUser = new UserEntity();
        mockUser.setEmail(email);
        doReturn(Optional.of(mockUser)).when(userService).getUserByEmail(anyString());
        ResponseEntity<Optional<UserEntity>> response = userController.getUserByEmail(email);
        assertThat(response.getStatusCodeValue(), is(equalTo(200)));
        assertNotNull(response.getBody());
        assertThat(response.getBody().isPresent(), is(equalTo(true)));
        assertThat(response.getBody().get().getEmail(), is(equalTo(email)));
        verify(userService, atLeast(1)).getUserByEmail(anyString());
    }

    @Test
    public void testGetUserById() {
        UUID userId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        UserEntity mockUser = new UserEntity();
        mockUser.setUserId(userId);
        doReturn(mockUser).when(userService).getUserById(any(UUID.class));
        ResponseEntity<UserEntity> response = userController.getUserById(userId);
        assertThat(response.getStatusCodeValue(), is(equalTo(200)));
        assertNotNull(response.getBody());
        assertThat(response.getBody().getUserId(), is(equalTo(userId)));
        verify(userService, atLeast(1)).getUserById(any(UUID.class));
    }

    @Test
    public void testCreateUser() {
        UserEntity user = new UserEntity();
        user.setUsername("newuser");
        user.setEmail("newuser@example.com");
        UserEntity createdUser = new UserEntity();
        createdUser.setUsername("newuser");
        createdUser.setEmail("newuser@example.com");
        createdUser.setUserId(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"));
        doReturn(createdUser).when(userService).createUser(any(UserEntity.class));
        ResponseEntity<UserEntity> response = userController.createUser(user);
        assertThat(response.getStatusCodeValue(), is(equalTo(200)));
        assertNotNull(response.getBody());
        assertThat(response.getBody().getUsername(), is(equalTo("newuser")));
        assertThat(response.getBody().getEmail(), is(equalTo("newuser@example.com")));
        verify(userService, atLeast(1)).createUser(any(UserEntity.class));
    }

    @Test
    public void testLoginUserRoleMismatchCaseInsensitive() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");
        loginRequest.setRole("admin");
        UserEntity mockUser = new UserEntity();
        mockUser.setUsername("testuser");
        mockUser.setPassword("password");
        mockUser.setRole(UserRole.ADMIN);
        doReturn(Optional.of(mockUser)).when(userService).getUserByUsername(anyString());
        ResponseEntity<String> response = userController.loginUser(loginRequest);
        assertThat(response.getStatusCodeValue(), is(equalTo(200)));
        assertThat(response.getBody(), is(equalTo("Login successful")));
        verify(userService, atLeast(1)).getUserByUsername(anyString());
    }

    @Test
    public void testGetUserByUsernameEmpty() {
        String username = "nonexistentuser";
        doReturn(Optional.empty()).when(userService).getUserByUsername(anyString());
        ResponseEntity<Optional<UserEntity>> response = userController.getUserByUsername(username);
        assertThat(response.getStatusCodeValue(), is(equalTo(200)));
        assertNotNull(response.getBody());
        assertThat(response.getBody().isPresent(), is(equalTo(false)));
        verify(userService, atLeast(1)).getUserByUsername(anyString());
    }

    @Test
    public void testGetUserByEmailEmpty() {
        String email = "nonexistent@example.com";
        doReturn(Optional.empty()).when(userService).getUserByEmail(anyString());
        ResponseEntity<Optional<UserEntity>> response = userController.getUserByEmail(email);
        assertThat(response.getStatusCodeValue(), is(equalTo(200)));
        assertNotNull(response.getBody());
        assertThat(response.getBody().isPresent(), is(equalTo(false)));
        verify(userService, atLeast(1)).getUserByEmail(anyString());
    }

    @Test
    public void testGetAllUsersEmptyList() {
        List<UserEntity> emptyList = new ArrayList<>();
        doReturn(emptyList).when(userService).getAllUsers();
        ResponseEntity<List<UserEntity>> response = userController.getAllUsers();
        assertThat(response.getStatusCodeValue(), is(equalTo(200)));
        assertNotNull(response.getBody());
        assertThat(response.getBody().size(), is(equalTo(0)));
        verify(userService, atLeast(1)).getAllUsers();
    }
}
