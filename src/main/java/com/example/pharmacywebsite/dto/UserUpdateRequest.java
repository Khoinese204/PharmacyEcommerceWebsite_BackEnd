package com.example.pharmacywebsite.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserUpdateRequest {
    private String fullName;
    private String gender;
    private LocalDate birthDate;
    private String phoneNumber;
    private String address;
    private String avatarUrl; // nếu có update ảnh trước đó
}
