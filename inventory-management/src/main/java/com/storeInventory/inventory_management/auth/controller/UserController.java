//package com.storeInventory.inventory_management.auth.controller;
//
//public class UserController {
//}

package com.storeInventory.inventory_management.auth.controller;

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
@CrossOrigin(origins = "*")  // You can restrict this if needed
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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

    @GetMapping
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Optional<UserEntity>> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Optional<UserEntity>> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity user) {
        return ResponseEntity.ok(userService.createUser(user));
    }
}

