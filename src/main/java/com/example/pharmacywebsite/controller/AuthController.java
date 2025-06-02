package com.example.pharmacywebsite.controller;

import com.example.pharmacywebsite.domain.User;
import com.example.pharmacywebsite.dto.*;
import com.example.pharmacywebsite.service.AuthService;
import com.example.pharmacywebsite.service.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest req) {
        authService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Register success"));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(HttpServletRequest request) {
        return ResponseEntity.ok(authService.getCurrentUser(request));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest req) {
        authService.resetPassword(req);
        return ResponseEntity.ok(Map.of("message", "Password reset successfully"));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest req, HttpServletRequest request) {
        authService.changePassword(req, request);
        return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        User user = jwtService.extractUserFromRequest(request);
        return ResponseEntity.ok(Map.of("message", "Logout success"));
    }

}
