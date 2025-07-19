package com.storeInventory.inventory_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.storeInventory.inventory_management.auth.controller.UserController;
import com.storeInventory.inventory_management.auth.dto.request.LoginRequest;
import com.storeInventory.inventory_management.auth.dto.request.UserRegistrationRequest;
import com.storeInventory.inventory_management.auth.model.Enum.UserRole;
import com.storeInventory.inventory_management.auth.model.UserEntity;
import com.storeInventory.inventory_management.auth.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
    controllers = UserController.class,
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfiguration.class)
)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserEntity getSampleUser() {
        UserEntity user = new UserEntity();
        user.setUserId(UUID.randomUUID());
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setRole(UserRole.ADMIN);
        return user;
    }

    @Test
    void testRegisterUser_success() throws Exception {
        UserRegistrationRequest req = new UserRegistrationRequest();
        req.setUsername("testuser");
        req.setEmail("test@example.com");
        req.setPassword("password123");
        req.setRole("ADMIN");
        UserEntity user = getSampleUser();
        when(userService.registerUser(any(UserRegistrationRequest.class))).thenReturn(user);
        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().string("User testuser registered successfully"));
    }

    @Test
    void testRegisterUser_failure() throws Exception {
        UserRegistrationRequest req = new UserRegistrationRequest();
        req.setUsername("testuser");
        req.setEmail("test@example.com");
        req.setPassword("password123");
        req.setRole("ADMIN");
        when(userService.registerUser(any(UserRegistrationRequest.class))).thenThrow(new RuntimeException("Registration failed"));
        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Registration failed: Registration failed"));
    }

    @Test
    void testLoginUser_success() throws Exception {
        LoginRequest req = new LoginRequest();
        req.setUsername("testuser");
        req.setPassword("password123");
        req.setRole("ADMIN");
        UserEntity user = getSampleUser();
        when(userService.getUserByUsername(eq("testuser"))).thenReturn(Optional.of(user));
        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful"));
    }

    @Test
    void testLoginUser_notFound() throws Exception {
        LoginRequest req = new LoginRequest();
        req.setUsername("nouser");
        req.setPassword("password123");
        req.setRole("ADMIN");
        when(userService.getUserByUsername(eq("nouser"))).thenReturn(Optional.empty());
        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }

    @Test
    void testLoginUser_invalidPassword() throws Exception {
        LoginRequest req = new LoginRequest();
        req.setUsername("testuser");
        req.setPassword("wrongpass");
        req.setRole("ADMIN");
        UserEntity user = getSampleUser();
        when(userService.getUserByUsername(eq("testuser"))).thenReturn(Optional.of(user));
        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid password"));
    }

    @Test
    void testLoginUser_roleMismatch() throws Exception {
        LoginRequest req = new LoginRequest();
        req.setUsername("testuser");
        req.setPassword("password123");
        req.setRole("USER");
        UserEntity user = getSampleUser();
        when(userService.getUserByUsername(eq("testuser"))).thenReturn(Optional.of(user));
        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Role mismatch. Please check the selected role."));
    }

    @Test
    void testGetAllUsers() throws Exception {
        UserEntity user = getSampleUser();
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(user));
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username", is("testuser")))
                .andExpect(jsonPath("$[0].email", is("test@example.com")));
    }

    @Test
    void testGetUserByUsername() throws Exception {
        UserEntity user = getSampleUser();
        when(userService.getUserByUsername(eq("testuser"))).thenReturn(Optional.of(user));
        mockMvc.perform(get("/api/users/username/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.get().username", is("testuser")));
    }

    @Test
    void testGetUserByEmail() throws Exception {
        UserEntity user = getSampleUser();
        when(userService.getUserByEmail(eq("test@example.com"))).thenReturn(Optional.of(user));
        mockMvc.perform(get("/api/users/email/test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.get().email", is("test@example.com")));
    }

    @Test
    void testGetUserById() throws Exception {
        UserEntity user = getSampleUser();
        when(userService.getUserById(eq(user.getUserId()))).thenReturn(user);
        mockMvc.perform(get("/api/users/" + user.getUserId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("testuser")));
    }

    @Test
    void testCreateUser() throws Exception {
        UserEntity user = getSampleUser();
        when(userService.createUser(any(UserEntity.class))).thenReturn(user);
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("testuser")));
    }
} 