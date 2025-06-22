package com.example.pharmacywebsite.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AddUserRequest {
    private String name;
    private String email;
    private String phone;
    private String address;
    private String password;
    private String role;
    private MultipartFile avatar;
}