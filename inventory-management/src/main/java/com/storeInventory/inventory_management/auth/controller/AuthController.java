package com.storeInventory.inventory_management.auth.controller;

import com.storeInventory.inventory_management.auth.dto.request.LoginRequest;
import com.storeInventory.inventory_management.auth.dto.response.LoginResponse;
import com.storeInventory.inventory_management.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173") // React Vite default
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}

