//package com.storeInventory.inventory_management.auth.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Random;
//
//@Service
//public class EmailService {
//
//    @Autowired
//    private JavaMailSender mailSender;
//
//    // Store email -> OTP + expiry
//    private final Map<String, OtpData> otpStore = new HashMap<>();
//
//    public String sendOtpEmail(String to) {
//        String otp = String.valueOf(100000 + new Random().nextInt(900000));
//        LocalDateTime expiry = LocalDateTime.now().plusMinutes(5);
//        otpStore.put(to, new OtpData(otp, expiry));
//
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(to);
//        message.setSubject("Your OTP Code");
//        message.setText("Your OTP code is: " + otp + "\nIt is valid for 5 minutes.");
//
//        mailSender.send(message);
//        return "OTP sent successfully to " + to;
//    }
//
//    public boolean verifyOtp(String email, String otp) {
//        OtpData data = otpStore.get(email);
//        if (data == null) return false;
//
//        // Check expiry
//        if (data.getExpiry().isBefore(LocalDateTime.now())) {
//            otpStore.remove(email);
//            return false;
//        }
//
//        boolean isValid = data.getOtp().equals(otp);
//        if (isValid) otpStore.remove(email); // Optional: OTP is single-use
//        return isValid;
//    }
//
//    private static class OtpData {
//        private final String otp;
//        private final LocalDateTime expiry;
//
//        public OtpData(String otp, LocalDateTime expiry) {
//            this.otp = otp;
//            this.expiry = expiry;
//        }
//
//        public String getOtp() {
//            return otp;
//        }
//
//        public LocalDateTime getExpiry() {
//            return expiry;
//        }
//    }
//}


package com.storeInventory.inventory_management.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Stores: email (lowercased) -> OTP + expiry time
    private final Map<String, OtpData> otpStore = new HashMap<>();

    // ✅ Send OTP to email
    public String sendOtpEmail(String to) {
        String normalizedEmail = to.toLowerCase(); // Normalize email
        String otp = String.valueOf(100000 + new Random().nextInt(900000)); // 6-digit OTP
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(5); // 5 minutes validity

        otpStore.put(normalizedEmail, new OtpData(otp, expiry));

        // Create email message
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code is: " + otp + "\nIt is valid for 5 minutes.");

        // Send email
        mailSender.send(message);

        System.out.println("✅ OTP sent to: " + to + " | OTP: " + otp); // Optional debug

        return "OTP sent successfully to " + to;
    }

    // ✅ Verify OTP
    public boolean verifyOtp(String email, String otp) {
        String normalizedEmail = email.toLowerCase(); // Normalize email
        OtpData data = otpStore.get(normalizedEmail);

        if (data == null) {
            System.out.println("❌ No OTP found for email: " + email);
            return false;
        }

        if (data.getExpiry().isBefore(LocalDateTime.now())) {
            otpStore.remove(normalizedEmail);
            System.out.println("❌ OTP expired for email: " + email);
            return false;
        }

        boolean isValid = data.getOtp().equals(otp);

        if (isValid) {
            otpStore.remove(normalizedEmail); // OTP is single-use
            System.out.println("✅ OTP verified for email: " + email);
        } else {
            System.out.println("❌ Invalid OTP for email: " + email);
        }

        return isValid;
    }

    // Inner class to store OTP and expiry time
    private static class OtpData {
        private final String otp;
        private final LocalDateTime expiry;

        public OtpData(String otp, LocalDateTime expiry) {
            this.otp = otp;
            this.expiry = expiry;
        }

        public String getOtp() {
            return otp;
        }

        public LocalDateTime getExpiry() {
            return expiry;
        }
    }
}
