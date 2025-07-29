package com.storeInventory.inventory_management.auth.service;

import com.storeInventory.inventory_management.auth.model.UserEntity;
import com.storeInventory.inventory_management.auth.model.Enum.UserRole;
import com.storeInventory.inventory_management.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.atLeast;
import java.lang.reflect.Field;

@Timeout(10)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        customUserDetailsService = new CustomUserDetailsService();
        Field userRepositoryField = CustomUserDetailsService.class.getDeclaredField("userRepository");
        userRepositoryField.setAccessible(true);
        userRepositoryField.set(customUserDetailsService, userRepository);
    }

    @Test
    void loadUserByUsernameReturnsUserDetailsWhenUserFound() {
        String username = "testUser";
        String password = "testPassword";
        UserRole role = UserRole.MANAGER;
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(password);
        userEntity.setRole(role);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        UserDetails result = customUserDetailsService.loadUserByUsername(username);
        assertThat(result, notNullValue());
        assertEquals(username, result.getUsername());
        assertEquals(password, result.getPassword());
        assertThat(result.getAuthorities().size(), equalTo(1));
        assertThat(result.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MANAGER")), equalTo(true));
        verify(userRepository, atLeast(1)).findByUsername(username);
    }

    @Test
    void loadUserByUsernameReturnsUserDetailsWhenUserFoundWithAssociateRole() {
        String username = "associateUser";
        String password = "associatePassword";
        UserRole role = UserRole.ASSOCIATE;
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(password);
        userEntity.setRole(role);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        UserDetails result = customUserDetailsService.loadUserByUsername(username);
        assertThat(result, notNullValue());
        assertEquals(username, result.getUsername());
        assertEquals(password, result.getPassword());
        assertThat(result.getAuthorities().size(), equalTo(1));
        assertThat(result.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ASSOCIATE")), equalTo(true));
        verify(userRepository, atLeast(1)).findByUsername(username);
    }

    @Test
    void loadUserByUsernameReturnsUserDetailsWhenUserFoundWithAnalystRole() {
        String username = "analystUser";
        String password = "analystPassword";
        UserRole role = UserRole.ANALYST;
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(password);
        userEntity.setRole(role);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        UserDetails result = customUserDetailsService.loadUserByUsername(username);
        assertThat(result, notNullValue());
        assertEquals(username, result.getUsername());
        assertEquals(password, result.getPassword());
        assertThat(result.getAuthorities().size(), equalTo(1));
        assertThat(result.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ANALYST")), equalTo(true));
        verify(userRepository, atLeast(1)).findByUsername(username);
    }

    @Test
    void loadUserByUsernameReturnsUserDetailsWhenUserFoundWithAdminRole() {
        String username = "adminUser";
        String password = "adminPassword";
        UserRole role = UserRole.ADMIN;
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(password);
        userEntity.setRole(role);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        UserDetails result = customUserDetailsService.loadUserByUsername(username);
        assertThat(result, notNullValue());
        assertEquals(username, result.getUsername());
        assertEquals(password, result.getPassword());
        assertThat(result.getAuthorities().size(), equalTo(1));
        assertThat(result.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")), equalTo(true));
        verify(userRepository, atLeast(1)).findByUsername(username);
    }

    @Test
    void loadUserByUsernameThrowsUsernameNotFoundExceptionWhenUserNotFound() {
        String username = "nonExistentUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername(username));
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, atLeast(1)).findByUsername(username);
    }

    @Test
    void loadUserByUsernameHandlesEmptyUsername() {
        String username = "";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername(username));
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, atLeast(1)).findByUsername(username);
    }

    @Test
    void loadUserByUsernameHandlesSpecialCharactersInUsername() {
        String username = "user@domain.com";
        String password = "password123";
        UserRole role = UserRole.MANAGER;
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(password);
        userEntity.setRole(role);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        UserDetails result = customUserDetailsService.loadUserByUsername(username);
        assertThat(result, notNullValue());
        assertEquals(username, result.getUsername());
        assertEquals(password, result.getPassword());
        assertThat(result.getAuthorities().size(), equalTo(1));
        assertThat(result.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MANAGER")), equalTo(true));
        verify(userRepository, atLeast(1)).findByUsername(username);
    }
}
