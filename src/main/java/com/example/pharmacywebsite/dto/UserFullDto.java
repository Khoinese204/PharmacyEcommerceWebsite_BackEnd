package com.example.pharmacywebsite.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserFullDto {
    private Integer id;
    private String email;
    private String fullName;
    private String gender;
    private String address;
    private String avatarUrl;
    private String role;
    private String phoneNumber;
    private LocalDate birthDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
