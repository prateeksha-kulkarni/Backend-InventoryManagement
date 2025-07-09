//package com.storeInventory.inventory_management.auth.service;
//
//import com.storeInventory.inventory_management.auth.dto.request.LoginRequest;
//import com.storeInventory.inventory_management.auth.dto.response.LoginResponse;
//import com.storeInventory.inventory_management.auth.model.UserEntity;
//import com.storeInventory.inventory_management.auth.repository.UserRepository;
//import com.storeInventory.inventory_management.auth.util.JwtUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//public class AuthService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    public LoginResponse login(LoginRequest request) {
//        // Authenticate user
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
//        );
//
//        // Fetch user from DB
//        UserEntity user = userRepository.findByUsername(request.getUsername())
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//        // Generate JWT token with username and role
//        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
//
//        // Create response object
//        LoginResponse response = new LoginResponse();
//        response.setToken(token);
//        response.setRole(user.getRole().name());
//        response.setUserId(user.getUserId().toString());
//        if (user.getStore() != null) {
//            response.setStoreId(user.getStore().getStoreId().toString());
//            response.setLocation(user.getStore().getName());
//        } else {
//            response.setStoreId(null);
//            response.setLocation(null);
//        }
//        return response;
//    }
////
//////    public LoginResponse login(LoginRequest request) {
//////        // Step 1: Authenticate user (username + password)
//////        Authentication authentication = authenticationManager.authenticate(
//////                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
//////        );
//////
//////        // Step 2: Fetch user details from DB
//////        UserEntity user = userRepository.findByUsername(request.getUsername())
//////                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//////
//////        // Step 3: Check if role from DB matches role sent by user
//////        if (!user.getRole().equalsIgnoreCase(request.getRole())) {
//////            throw new RuntimeException("Role mismatch. Please select correct role.");
//////        }
//////
//////        // Step 4: Generate JWT token with username + role
//////        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
//////
//////        // Step 5: Return token + actual role
//////        LoginResponse response = new LoginResponse();
//////        response.setToken(token);
//////        response.setRole(user.getRole());
//////        return response;
//////    }
//}
////
//
////package com.storeInventory.inventory_management.auth.service;
////
////import com.storeInventory.inventory_management.auth.dto.request.LoginRequest;
////import com.storeInventory.inventory_management.auth.dto.response.LoginResponse;
////import com.storeInventory.inventory_management.auth.model.UserEntity;
////import com.storeInventory.inventory_management.auth.repository.UserRepository;
////import com.storeInventory.inventory_management.auth.util.JwtUtil;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.security.authentication.AuthenticationManager;
////import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
////import org.springframework.security.core.Authentication;
////import org.springframework.security.core.userdetails.UsernameNotFoundException;
////import org.springframework.stereotype.Service;
////
////@Service
////public class AuthService {
////
////    @Autowired
////    private UserRepository userRepository;
////
////    @Autowired
////    private JwtUtil jwtUtil;
////
////    @Autowired
////    private AuthenticationManager authenticationManager;
////
////    public LoginResponse login(LoginRequest request) {
////        // Step 1: Authenticate user (username + password)
////        Authentication authentication = authenticationManager.authenticate(
////                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
////        );
////
////        // Step 2: Fetch user details from DB
////        UserEntity user = userRepository.findByUsername(request.getUsername())
////                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
////
////        // Step 3: Check if role from DB matches role sent by user
////        if (!user.getRole().equalsIgnoreCase(request.getRole())) {
////            throw new RuntimeException("Role mismatch. Please select correct role.");
////        }
////
////        // Step 4: Generate JWT token with username + role
////        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
////
////        // Step 5: Return token + actual role
////        LoginResponse response = new LoginResponse();
////        response.setToken(token);
////        response.setRole(user.getRole());
////        return response;
////    }
////}
//


//package com.storeInventory.inventory_management.auth.service;
//
//import com.storeInventory.inventory_management.auth.dto.request.LoginRequest;
//import com.storeInventory.inventory_management.auth.dto.response.LoginResponse;
//import com.storeInventory.inventory_management.auth.model.UserEntity;
//import com.storeInventory.inventory_management.auth.repository.UserRepository;
//import com.storeInventory.inventory_management.auth.util.JwtUtil;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class AuthService {
//
//    private final UserRepository userRepository;
//    private final JwtUtil jwtUtil;
//    private final AuthenticationManager authenticationManager;
//
//    public LoginResponse login(LoginRequest request) {
//        // Step 1: Authenticate username + password
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
//        );
//
//        // Step 2: Fetch user details from DB
//        UserEntity user = userRepository.findByUsername(request.getUsername())
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//        // Step 3: Check if role matches
//        if (!user.getRole().name().equalsIgnoreCase(request.getRole())) {
//            throw new RuntimeException("Role mismatch. Please select the correct role.");
//        }
//
//        // Step 4: Generate JWT token
//        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
//
//        // Step 5: Prepare and return response
//        LoginResponse response = new LoginResponse();
//        response.setToken(token);
//        response.setRole(user.getRole().name());
//        response.setUserId(user.getUserId().toString());
//
//        if (user.getStore() != null) {
//            response.setStoreId(user.getStore().getStoreId().toString());
//            response.setLocation(user.getStore().getName());
//        } else {
//            response.setStoreId(null);
//            response.setLocation(null);
//        }
//
//        return response;
//    }
//}

//package com.storeInventory.inventory_management.auth.service;
//
//import com.storeInventory.inventory_management.auth.dto.request.LoginRequest;
//import com.storeInventory.inventory_management.auth.dto.response.LoginResponse;
//import com.storeInventory.inventory_management.auth.model.UserEntity;
//import com.storeInventory.inventory_management.auth.repository.UserRepository;
//import com.storeInventory.inventory_management.auth.util.JwtUtil;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class AuthService {
//
//    private final UserRepository userRepository;
//    private final JwtUtil jwtUtil;
//    private final AuthenticationManager authenticationManager;
//    private final EmailService emailService;
//
//
//    // 🔐 Login Logic
//    public LoginResponse login(LoginRequest request) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
//        );
//
//        UserEntity user = userRepository.findByUsername(request.getUsername())
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//        if (!user.getRole().name().equalsIgnoreCase(request.getRole())) {
//            throw new RuntimeException("Role mismatch. Please select the correct role.");
//        }
//
//        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
//
//        LoginResponse response = new LoginResponse();
//        response.setToken(token);
//        response.setRole(user.getRole().name());
//        response.setUserId(user.getUserId().toString());
//
//        if (user.getStore() != null) {
//            response.setStoreId(user.getStore().getStoreId().toString());
//            response.setLocation(user.getStore().getName());
//        }
//
//        return response;
//    }
//
//    // 📧 Send OTP to user’s registered email
//    public String sendOtpToEmail(String email) {
//        UserEntity user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException("Email not registered"));
//
//        return emailService.sendOtpEmail(email);
//    }
//
//    // 🔍 Verify OTP (to be implemented in EmailService)
//    public boolean verifyOtp(String email, String otp) {
//        return emailService.verifyOtp(email, otp);
//    }
//
//    // 🔁 Reset password if OTP valid
//    public String resetPassword(String email, String newPassword) {
//        UserEntity user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//        user.setPassword(newPassword); // You may hash it if using password encoder
//        userRepository.save(user);
//
//        return "Password reset successful";
//    }
//}
//

package com.storeInventory.inventory_management.auth.service;

import com.storeInventory.inventory_management.auth.dto.request.LoginRequest;
import com.storeInventory.inventory_management.auth.dto.response.LoginResponse;
import com.storeInventory.inventory_management.auth.model.UserEntity;
import com.storeInventory.inventory_management.auth.repository.UserRepository;
import com.storeInventory.inventory_management.auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    // 🔐 Login logic with role check and token
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.getRole().name().equalsIgnoreCase(request.getRole())) {
            throw new RuntimeException("Role mismatch. Please select the correct role.");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setRole(user.getRole().name());
        response.setUserId(user.getUserId().toString());

        if (user.getStore() != null) {
            response.setStoreId(user.getStore().getStoreId().toString());
            response.setLocation(user.getStore().getName());
        }

        return response;
    }

    // 📧 Send OTP to registered email
    public String sendOtpToEmail(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email not registered"));

        return emailService.sendOtpEmail(email);
    }

    // ✅ Verify OTP using EmailService
    public boolean verifyOtp(String email, String otp) {
        return emailService.verifyOtp(email, otp);
    }

    // 🔁 Reset password if OTP was verified
    public String resetPassword(String email, String newPassword) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return "Password reset successful";
    }
}

