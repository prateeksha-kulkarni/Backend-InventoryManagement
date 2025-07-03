package com.storeInventory.inventory_management.auth.service;

import com.storeInventory.inventory_management.auth.model.UserEntity;
import com.storeInventory.inventory_management.auth.repository.UserRepository;
import com.storeInventory.inventory_management.auth.dto.request.UserRegistrationRequest;
import com.storeInventory.inventory_management.auth.model.Enum.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.storeInventory.inventory_management.auth.repository.StoreRepository;
import com.storeInventory.inventory_management.auth.model.StoreEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StoreRepository storeRepository;

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserEntity> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<UserEntity> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserEntity getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserEntity createUser(UserEntity user) {
        return userRepository.save(user);
    }

    public UserEntity registerUser(UserRegistrationRequest request) {
        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setName(request.getName());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.valueOf(request.getRole().toUpperCase()));
        user.setEmail(request.getEmail());
        if (request.getLocation() != null && !request.getLocation().isEmpty()) {
            StoreEntity store = storeRepository.findByName(request.getLocation())
                .orElseThrow(() -> new RuntimeException("Store not found for name: " + request.getLocation()));
            user.setStore(store);
        }
        return userRepository.save(user);
    }
}

