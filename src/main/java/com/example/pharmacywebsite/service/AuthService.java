package com.example.pharmacywebsite.service;

import com.example.pharmacywebsite.domain.Role;
import com.example.pharmacywebsite.domain.User;
import com.example.pharmacywebsite.dto.*;
import com.example.pharmacywebsite.exception.ApiException;
import com.example.pharmacywebsite.repository.RoleRepository;
import com.example.pharmacywebsite.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public void register(RegisterRequest req) {
        if (userRepo.existsByEmail(req.getEmail())) {
            throw new ApiException("Email already exists", HttpStatus.BAD_REQUEST);
        }

        if (req.getPassword() == null || req.getPassword().length() < 6) {
            throw new ApiException("Password must be at least 6 characters", HttpStatus.BAD_REQUEST);
        }

        Role role = roleRepo.findByName("Customer")
                .orElseThrow(() -> new ApiException("Role not found", HttpStatus.INTERNAL_SERVER_ERROR));

        User user = new User();
        user.setEmail(req.getEmail());
        user.setFullName(req.getFullName());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setRole(role);
        user = userRepo.save(user); // cập nhật lại sau khi save để đảm bảo user có ID + Role

        // String token = jwtService.generateToken(user);

        // return ResponseEntity.status(HttpStatus.CREATED)
        // .body(Map.of("message", "Register success"));
    }

    public AuthResponse login(LoginRequest req) {
        User user = userRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> new ApiException("Invalid email or password", HttpStatus.UNAUTHORIZED));

        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        } catch (Exception ex) {
            throw new ApiException("Invalid email or password", HttpStatus.UNAUTHORIZED);
        }

        String token = jwtService.generateToken(user);

        AuthResponse res = new AuthResponse();
        res.setToken(token);
        res.setUser(toDto(user));
        return res;
    }

    public UserDto getCurrentUser(HttpServletRequest request) {
        User user = jwtService.extractUserFromRequest(request);
        return toDto(user);
    }

    public void resetPassword(ForgotPasswordRequest req) {
        if (req.getNewPassword() == null || req.getNewPassword().length() < 6) {
            throw new ApiException("Password must be at least 6 characters", HttpStatus.BAD_REQUEST);
        }
        if (!req.getNewPassword().equals(req.getConfirmPassword())) {
            throw new ApiException("Password confirmation does not match", HttpStatus.BAD_REQUEST);
        }

        User user = userRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> new ApiException("Email not found", HttpStatus.BAD_REQUEST));

        user.setPasswordHash(passwordEncoder.encode(req.getNewPassword()));
        userRepo.save(user);
    }

    public void changePassword(ChangePasswordRequest req, HttpServletRequest request) {
        User user = jwtService.extractUserFromRequest(request);

        if (!passwordEncoder.matches(req.getOldPassword(), user.getPasswordHash())) {
            throw new ApiException("Old password is incorrect", HttpStatus.BAD_REQUEST);
        }

        if (req.getNewPassword() == null || req.getNewPassword().length() < 6) {
        throw new ApiException("New password must be at least 6 characters", HttpStatus.BAD_REQUEST);
    }

        user.setPasswordHash(passwordEncoder.encode(req.getNewPassword()));
        userRepo.save(user);
    }

    private UserDto toDto(User u) {
        UserDto dto = new UserDto();
        dto.setId(u.getId());
        dto.setEmail(u.getEmail());
        dto.setFullName(u.getFullName());
        dto.setRole(u.getRole().getName());
        return dto;
    }
}
