package com.example.pharmacywebsite.controller;

import com.example.pharmacywebsite.domain.Role;
import com.example.pharmacywebsite.domain.User;
import com.example.pharmacywebsite.dto.AddUserRequest;
import com.example.pharmacywebsite.dto.UpdateUserRequest;
import com.example.pharmacywebsite.dto.UserDto;
import com.example.pharmacywebsite.dto.UserFullDto;
import com.example.pharmacywebsite.dto.UserUpdateRequest;
import com.example.pharmacywebsite.repository.RoleRepository;
import com.example.pharmacywebsite.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.io.IOException;

import java.nio.file.*;
import java.util.Objects;

import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepo;
    @Autowired
    private RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userRepo.findAll().stream()
                .map(user -> {
                    UserDto dto = new UserDto();
                    dto.setId(user.getId());
                    dto.setEmail(user.getEmail());
                    dto.setFullName(user.getFullName());
                    dto.setRole(user.getRole() != null ? user.getRole().getName() : null);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserFullDto getUserById(@PathVariable("id") Integer id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        UserFullDto dto = new UserFullDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setGender(user.getGender());
        dto.setAddress(user.getAddress());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRole(user.getRole() != null ? user.getRole().getName() : null);
        dto.setBirthDate(user.getBirthDate());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }

    @PostMapping("/{id}/avatar")
    public ResponseEntity<Map<String, String>> uploadAvatar(
            @PathVariable("id") Integer id,
            @RequestParam("avatar") MultipartFile file) throws IOException {

        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Đặt tên file chuẩn hóa
        String filename = "avatar_" + id + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();

        // ✅ Đường lưu vào đúng folder static/images/avatar
        Path path = Paths.get("src/main/resources/static/images/avatar/" + filename);

        // Tạo folder nếu chưa tồn tại
        Files.createDirectories(path.getParent());

        // Lưu file
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        // ✅ Set đường dẫn cho frontend
        String avatarUrl = "/images/avatar/" + filename;
        user.setAvatarUrl(avatarUrl);
        user.setUpdatedAt(LocalDateTime.now());
        userRepo.save(user);

        Map<String, String> response = new HashMap<>();
        response.put("avatarUrl", avatarUrl);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable("id") Integer id,
            @RequestBody UserUpdateRequest request) {

        Optional<User> optionalUser = userRepo.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        User user = optionalUser.get();
        user.setFullName(request.getFullName());
        user.setGender(request.getGender());
        user.setBirthDate(request.getBirthDate());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());

        if (request.getAvatarUrl() != null && !request.getAvatarUrl().isEmpty()) {
            user.setAvatarUrl(request.getAvatarUrl());
        }

        user.setUpdatedAt(LocalDateTime.now());
        userRepo.save(user);

        return ResponseEntity.ok("Cập nhật thông tin thành công");
    }

    @PutMapping("admin/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") Integer id, @RequestBody UpdateUserRequest request) {
        Optional<User> optionalUser = userRepo.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = optionalUser.get();
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());

        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò: " + request.getRole()));

        user.setRole(role);
        user.setAvatarUrl(request.getAvatarUrl());

        userRepo.save(user);

        return ResponseEntity.ok("User updated successfully");
    }

    @PostMapping(value = "/admin/addUser", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addUser(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("address") String address,
            @RequestParam("password") String password,
            @RequestParam("role") String role,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar) {

        if (userRepo.existsByEmail(email)) {
            return ResponseEntity.badRequest().body("Email đã được sử dụng");
        }

        User user = new User();
        user.setFullName(name);
        user.setEmail(email);
        user.setPhoneNumber(phone);
        user.setAddress(address);
        user.setPasswordHash(passwordEncoder.encode(password));

        Role roleEntity = roleRepository.findByName(role)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy role: " + role));
        user.setRole(roleEntity);

        if (avatar != null && !avatar.isEmpty()) {
            try {
                String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(avatar.getOriginalFilename()));
                String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String filename = "avatar_" + System.currentTimeMillis() + extension;

                String uploadDir = "src/main/resources/static/images/avatar";
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(filename);
                Files.copy(avatar.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                user.setAvatarUrl("/images/avatar/" + filename);
            } catch (IOException e) {
                e.printStackTrace(); // ⚠️ Dòng này sẽ in toàn bộ lỗi chi tiết khi có lỗi
                return ResponseEntity.internalServerError().body("Lỗi khi lưu ảnh: " + e.getMessage());
            }
        }

        userRepo.save(user);
        return ResponseEntity.ok("Thêm người dùng thành công");
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Integer id) {
        if (!userRepo.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy người dùng");
        }

        userRepo.deleteById(id); // ❌ Hard delete
        return ResponseEntity.ok("Đã xóa người dùng thành công");
    }

}
