package com.example.pharmacywebsite.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private String role;
    private String avatarUrl;
}
