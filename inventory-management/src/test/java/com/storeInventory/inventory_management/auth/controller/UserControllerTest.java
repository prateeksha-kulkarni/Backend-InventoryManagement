package com.storeInventory.inventory_management.auth.controller;

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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@Timeout(10)
public class UserControllerTest {

    @Mock
    private UserService userService;

    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userService);
    }

    @Test
    void testRegisterUserSuccess() throws Exception {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setUsername("testuser");
        request.setPassword("password");
        request.setRole("ADMIN");
        request.setEmail("test@example.com");
        request.setName("Test User");
        UserEntity mockUser = new UserEntity();
        mockUser.setUsername("testuser");
        doReturn(mockUser).when(userService).registerUser(eq(request));
        ResponseEntity<String> response = userController.registerUser(request);
        assertThat(response.getStatusCode().value(), is(equalTo(200)));
        assertThat(response.getBody(), is(equalTo("User testuser registered successfully")));
        verify(userService, atLeast(1)).registerUser(eq(request));
    }

    @Test
    void testRegisterUserException() throws Exception {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setUsername("testuser");
        doThrow(new RuntimeException("Registration error")).when(userService).registerUser(eq(request));
        ResponseEntity<String> response = userController.registerUser(request);
        assertThat(response.getStatusCode().value(), is(equalTo(400)));
        assertThat(response.getBody(), is(equalTo("Registration failed: Registration error")));
        verify(userService, atLeast(1)).registerUser(eq(request));
    }

    @Test
    void testLoginUserSuccess() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");
        loginRequest.setRole("ADMIN");
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testuser");
        userEntity.setPassword("password");
        userEntity.setRole(UserRole.ADMIN);
        doReturn(Optional.of(userEntity)).when(userService).getUserByUsername(eq("testuser"));
        ResponseEntity<String> response = userController.loginUser(loginRequest);
        assertThat(response.getStatusCode().value(), is(equalTo(200)));
        assertThat(response.getBody(), is(equalTo("Login successful")));
        verify(userService, atLeast(1)).getUserByUsername(eq("testuser"));
    }

    @Test
    void testLoginUserNotFound() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("nonexistent");
        loginRequest.setPassword("password");
        loginRequest.setRole("ADMIN");
        doReturn(Optional.empty()).when(userService).getUserByUsername(eq("nonexistent"));
        ResponseEntity<String> response = userController.loginUser(loginRequest);
        assertThat(response.getStatusCode().value(), is(equalTo(404)));
        assertThat(response.getBody(), is(equalTo("User not found")));
        verify(userService, atLeast(1)).getUserByUsername(eq("nonexistent"));
    }

    @Test
    void testLoginUserInvalidPassword() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("wrongpassword");
        loginRequest.setRole("ADMIN");
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testuser");
        userEntity.setPassword("correctpassword");
        userEntity.setRole(UserRole.ADMIN);
        doReturn(Optional.of(userEntity)).when(userService).getUserByUsername(eq("testuser"));
        ResponseEntity<String> response = userController.loginUser(loginRequest);
        assertThat(response.getStatusCode().value(), is(equalTo(401)));
        assertThat(response.getBody(), is(equalTo("Invalid password")));
        verify(userService, atLeast(1)).getUserByUsername(eq("testuser"));
    }

    @Test
    void testLoginUserRoleMismatch() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");
        loginRequest.setRole("MANAGER");
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testuser");
        userEntity.setPassword("password");
        userEntity.setRole(UserRole.ADMIN);
        doReturn(Optional.of(userEntity)).when(userService).getUserByUsername(eq("testuser"));
        ResponseEntity<String> response = userController.loginUser(loginRequest);
        assertThat(response.getStatusCode().value(), is(equalTo(403)));
        assertThat(response.getBody(), is(equalTo("Role mismatch. Please check the selected role.")));
        verify(userService, atLeast(1)).getUserByUsername(eq("testuser"));
    }

    @Test
    void testGetAllUsers() {
        List<UserEntity> userList = new ArrayList<>();
        UserEntity user1 = new UserEntity();
        user1.setUsername("user1");
        UserEntity user2 = new UserEntity();
        user2.setUsername("user2");
        userList.add(user1);
        userList.add(user2);
        doReturn(userList).when(userService).getAllUsers();
        ResponseEntity<List<UserEntity>> response = userController.getAllUsers();
        assertThat(response.getStatusCode().value(), is(equalTo(200)));
        assertNotNull(response.getBody());
        assertThat(response.getBody().size(), is(equalTo(2)));
        verify(userService, atLeast(1)).getAllUsers();
    }

    @Test
    void testGetUserByUsername() {
        String username = "testuser";
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        Optional<UserEntity> userOptional = Optional.of(userEntity);
        doReturn(userOptional).when(userService).getUserByUsername(eq(username));
        ResponseEntity<Optional<UserEntity>> response = userController.getUserByUsername(username);
        assertThat(response.getStatusCode().value(), is(equalTo(200)));
        assertNotNull(response.getBody());
        assertThat(response.getBody().isPresent(), is(equalTo(true)));
        assertThat(response.getBody().get().getUsername(), is(equalTo(username)));
        verify(userService, atLeast(1)).getUserByUsername(eq(username));
    }

    @Test
    void testGetUserByEmail() {
        String email = "test@example.com";
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);
        Optional<UserEntity> userOptional = Optional.of(userEntity);
        doReturn(userOptional).when(userService).getUserByEmail(eq(email));
        ResponseEntity<Optional<UserEntity>> response = userController.getUserByEmail(email);
        assertThat(response.getStatusCode().value(), is(equalTo(200)));
        assertNotNull(response.getBody());
        assertThat(response.getBody().isPresent(), is(equalTo(true)));
        assertThat(response.getBody().get().getEmail(), is(equalTo(email)));
        verify(userService, atLeast(1)).getUserByEmail(eq(email));
    }

    @Test
    void testGetUserById() {
        UUID userId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userId);
        doReturn(userEntity).when(userService).getUserById(eq(userId));
        ResponseEntity<UserEntity> response = userController.getUserById(userId);
        assertThat(response.getStatusCode().value(), is(equalTo(200)));
        assertNotNull(response.getBody());
        assertThat(response.getBody().getUserId(), is(equalTo(userId)));
        verify(userService, atLeast(1)).getUserById(eq(userId));
    }

    @Test
    void testCreateUser() {
        UserEntity inputUser = new UserEntity();
        inputUser.setUsername("newuser");
        inputUser.setEmail("newuser@example.com");
        UserEntity createdUser = new UserEntity();
        createdUser.setUserId(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"));
        createdUser.setUsername("newuser");
        createdUser.setEmail("newuser@example.com");
        doReturn(createdUser).when(userService).createUser(eq(inputUser));
        ResponseEntity<UserEntity> response = userController.createUser(inputUser);
        assertThat(response.getStatusCode().value(), is(equalTo(200)));
        assertNotNull(response.getBody());
        assertThat(response.getBody().getUsername(), is(equalTo("newuser")));
        assertThat(response.getBody().getEmail(), is(equalTo("newuser@example.com")));
        verify(userService, atLeast(1)).createUser(eq(inputUser));
    }
}
