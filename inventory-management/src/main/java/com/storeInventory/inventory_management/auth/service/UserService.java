////package com.storeInventory.inventory_management.auth.service;
////
////import com.storeInventory.inventory_management.auth.model.UserEntity;
////import com.storeInventory.inventory_management.auth.repository.UserRepository;
////import com.storeInventory.inventory_management.auth.dto.request.UserRegistrationRequest;
////import com.storeInventory.inventory_management.auth.model.Enum.UserRole;
////import lombok.RequiredArgsConstructor;
////import org.springframework.security.crypto.password.PasswordEncoder;
////import org.springframework.stereotype.Service;
////import com.storeInventory.inventory_management.auth.repository.StoreRepository;
////import com.storeInventory.inventory_management.auth.model.StoreEntity;
////
////import java.util.List;
////import java.util.Optional;
////import java.util.UUID;
////
////@Service
////@RequiredArgsConstructor
////public class UserService {
////    private final UserRepository userRepository;
////    private final PasswordEncoder passwordEncoder;
////    private final StoreRepository storeRepository;
////
////    public List<UserEntity> getAllUsers() {
////        return userRepository.findAll();
////    }
////
////    public Optional<UserEntity> getUserByUsername(String username) {
////        return userRepository.findByUsername(username);
////    }
////
////    public Optional<UserEntity> getUserByEmail(String email) {
////        return userRepository.findByEmail(email);
////    }
////
////    public UserEntity getUserById(UUID userId) {
////        return userRepository.findById(userId)
////                .orElseThrow(() -> new RuntimeException("User not found"));
////    }
////
////    public UserEntity createUser(UserEntity user) {
////        return userRepository.save(user);
////    }
////
////    public UserEntity registerUser(UserRegistrationRequest request) {
////        UserEntity user = new UserEntity();
////        user.setUsername(request.getUsername());
////        user.setName(request.getName());
////        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
////        user.setRole(UserRole.valueOf(request.getRole().toUpperCase()));
////        user.setEmail(request.getEmail());
////        if (request.getLocation() != null && !request.getLocation().isEmpty()) {
////            StoreEntity store = storeRepository.findByName(request.getLocation())
////                .orElseThrow(() -> new RuntimeException("Store not found for name: " + request.getLocation()));
////            user.setStore(store);
////        }
////        return userRepository.save(user);
////    }
////}
////
//
//package com.storeInventory.inventory_management.auth.service;
//
//import com.storeInventory.inventory_management.auth.dto.request.UserRegistrationRequest;
//import com.storeInventory.inventory_management.auth.model.Enum.UserRole;
//import com.storeInventory.inventory_management.auth.model.StoreEntity;
//import com.storeInventory.inventory_management.auth.model.UserEntity;
//import com.storeInventory.inventory_management.auth.repository.StoreRepository;
//import com.storeInventory.inventory_management.auth.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class UserService {
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final StoreRepository storeRepository;
//
//    // Get all users
//    public List<UserEntity> getAllUsers() {
//        return userRepository.findAll();
//    }
//
//    // Get user by username
//    public Optional<UserEntity> getUserByUsername(String username) {
//        return userRepository.findByUsername(username);
//    }
//
//    // Get user by email
//    public Optional<UserEntity> getUserByEmail(String email) {
//        return userRepository.findByEmail(email);
//    }
//
//    // Get user by ID
//    public UserEntity getUserById(UUID userId) {
//        return userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//    }
//
//    // Create user directly (not recommended without encoding)
//    public UserEntity createUser(UserEntity user) {
//        return userRepository.save(user);
//    }
//
//    // Registration logic
//    public UserEntity registerUser(UserRegistrationRequest request) {
//        UserEntity user = new UserEntity();
//        user.setUsername(request.getUsername());
//        user.setName(request.getName());
//
//        // Hash the password
//        //user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//
//
//
//        // Convert role string to enum
//        user.setRole(UserRole.valueOf(request.getRole().toUpperCase()));
//
//        user.setEmail(request.getEmail());
//
//        // Optional: assign store if provided
//        if (request.getLocation() != null && !request.getLocation().isEmpty()) {
//            StoreEntity store = storeRepository.findByName(request.getLocation())
//                    .orElseThrow(() -> new RuntimeException("Store not found for name: " + request.getLocation()));
//            user.setStore(store);
//        }
//
//        return userRepository.save(user);
//    }
//
//    // Login check logic (optional helper)
//    public boolean isLoginValid(String username, String rawPassword, String role) {
//        Optional<UserEntity> userOpt = userRepository.findByUsername(username);
//
//        if (userOpt.isEmpty()) return false;
//
//        UserEntity user = userOpt.get();
//
//        boolean passwordMatches = passwordEncoder.matches(rawPassword, user.getPasswordHash());
//        boolean roleMatches = user.getRole().name().equalsIgnoreCase(role);
//
//        return passwordMatches && roleMatches;
//    }
//}
//


package com.storeInventory.inventory_management.auth.service;

import com.storeInventory.inventory_management.auth.dto.request.UserRegistrationRequest;
import com.storeInventory.inventory_management.auth.model.Enum.UserRole;
import com.storeInventory.inventory_management.auth.model.StoreEntity;
import com.storeInventory.inventory_management.auth.model.UserEntity;
import com.storeInventory.inventory_management.auth.repository.StoreRepository;
import com.storeInventory.inventory_management.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StoreRepository storeRepository;

    // Get all users
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by username
    public Optional<UserEntity> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Get user by email
    public Optional<UserEntity> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Get user by ID
    public UserEntity getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Create user directly
    public UserEntity createUser(UserEntity user) {
        return userRepository.save(user);
    }

    // Registration logic
    public UserEntity registerUser(UserRegistrationRequest request) {
        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // 🔐 Hashed
        user.setRole(UserRole.valueOf(request.getRole().toUpperCase()));
        user.setEmail(request.getEmail());

        if (request.getLocation() != null && !request.getLocation().isEmpty()) {
            StoreEntity store = storeRepository.findByName(request.getLocation())
                    .orElseThrow(() -> new RuntimeException("Store not found for name: " + request.getLocation()));
            user.setStore(store);
        }

        return userRepository.save(user);
    }

    // Login check logic (optional)
    public boolean isLoginValid(String username, String rawPassword, String role) {
        Optional<UserEntity> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty()) return false;

        UserEntity user = userOpt.get();

        boolean passwordMatches = passwordEncoder.matches(rawPassword, user.getPassword()); // ✅ Fix here
        boolean roleMatches = user.getRole().name().equalsIgnoreCase(role);

        return passwordMatches && roleMatches;
    }
}

