////package com.storeInventory.inventory_management.auth.controller;
////
////public class UserController {
////}
//
//package com.storeInventory.inventory_management.auth.controller;
//
//import com.storeInventory.inventory_management.auth.dto.request.UserRegistrationRequest;
//import com.storeInventory.inventory_management.auth.model.UserEntity;
//import com.storeInventory.inventory_management.auth.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.security.access.prepost.PreAuthorize;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api/users")
//@CrossOrigin(origins = "*")  // You can restrict this if needed
//@RequiredArgsConstructor
//public class UserController {
//
//    private final UserService userService;
//
//    @PostMapping("/register")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationRequest request) {
//        try {
//            UserEntity user = userService.registerUser(request);
//            return ResponseEntity.ok("User " + user.getUsername() + " registered successfully");
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
//        }
//    }
//
//    @GetMapping
//    public ResponseEntity<List<UserEntity>> getAllUsers() {
//        return ResponseEntity.ok(userService.getAllUsers());
//    }
//
//    @GetMapping("/username/{username}")
//    public ResponseEntity<Optional<UserEntity>> getUserByUsername(@PathVariable String username) {
//        return ResponseEntity.ok(userService.getUserByUsername(username));
//    }
//
//    @GetMapping("/email/{email}")
//    public ResponseEntity<Optional<UserEntity>> getUserByEmail(@PathVariable String email) {
//        return ResponseEntity.ok(userService.getUserByEmail(email));
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<UserEntity> getUserById(@PathVariable UUID id) {
//        return ResponseEntity.ok(userService.getUserById(id));
//    }
//
//    @PostMapping
//    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity user) {
//        return ResponseEntity.ok(userService.createUser(user));
//    }
//}
//
//


package com.storeInventory.inventory_management.auth.controller;

import com.storeInventory.inventory_management.auth.dto.request.LoginRequest;
import com.storeInventory.inventory_management.auth.dto.request.UserRegistrationRequest;
import com.storeInventory.inventory_management.auth.model.UserEntity;
import com.storeInventory.inventory_management.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")  // You can restrict this to your frontend origin
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ========== Register ==========
    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationRequest request) {
        try {
            UserEntity user = userService.registerUser(request);
            return ResponseEntity.ok("User " + user.getUsername() + " registered successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    // ========== Login ==========
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest loginRequest) {
        Optional<UserEntity> userOpt = userService.getUserByUsername(loginRequest.getUsername());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }

        UserEntity user = userOpt.get();

        // Compare plain-text password to stored hash (if it's not hashed)
        if (!user.getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.status(401).body("Invalid password");
        }

        // Check role from enum
        if (!user.getRole().name().equalsIgnoreCase(loginRequest.getRole())) {
            return ResponseEntity.status(403).body("Role mismatch. Please check the selected role.");
        }

        return ResponseEntity.ok("Login successful");
    }

    // ========== Get All Users ==========
    @GetMapping
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // ========== Get by Username ==========
    @GetMapping("/username/{username}")
    public ResponseEntity<Optional<UserEntity>> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    // ========== Get by Email ==========
    @GetMapping("/email/{email}")
    public ResponseEntity<Optional<UserEntity>> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    // ========== Get by ID ==========
    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // ========== Create Raw User ==========
    @PostMapping
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity user) {
        return ResponseEntity.ok(userService.createUser(user));
    }
}
