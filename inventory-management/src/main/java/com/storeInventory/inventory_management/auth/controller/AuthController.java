package com.storeInventory.inventory_management.auth.controller;

import com.storeInventory.inventory_management.auth.dto.request.ForgotPasswordRequest;
import com.storeInventory.inventory_management.auth.dto.request.LoginRequest;
import com.storeInventory.inventory_management.auth.dto.request.ResetPasswordRequest;
import com.storeInventory.inventory_management.auth.dto.request.VerifyOtpRequest;
import com.storeInventory.inventory_management.auth.dto.response.LoginResponse;
import com.storeInventory.inventory_management.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        String message = authService.sendOtpToEmail(request.getEmail());
        return ResponseEntity.ok(message);
    }

//    @PostMapping("/verify-otp")
//    public ResponseEntity<Boolean> verifyOtp(@RequestBody VerifyOtpRequest request) {
//        boolean isValid = authService.verifyOtp(request.getEmail(), request.getOtp());
//        return ResponseEntity.ok(isValid);
//    }

//    @PostMapping("/verify-otp")
//    public ResponseEntity<Map<String, String>> verifyOtp(@RequestBody Map<String, String> request) {
//        String email = request.get("email");
//        String otp = request.get("otp");
//
//        boolean isValid = authService.verifyOtp(email, otp);
//
//        if (isValid) {
//            return ResponseEntity.ok(Map.of("message", "OTP verified successfully"));
//        } else {
//            return ResponseEntity.status(400).body(Map.of("message", "Invalid or expired OTP"));
//        }
//    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, Object>> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");

        boolean isValid = authService.verifyOtp(email, otp);

        if (isValid) {
            return ResponseEntity.ok(Map.of(
                    "valid", true,
                    "message", "OTP verified successfully"
            ));
        } else {
            return ResponseEntity.status(401).body(Map.of(
                    "valid", false,
                    "message", "Invalid or expired OTP"
            ));
        }
    }


    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        String msg = authService.resetPassword(request.getEmail(), request.getNewPassword());
        return ResponseEntity.ok(msg);
    }





}

