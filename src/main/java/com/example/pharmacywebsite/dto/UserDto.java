package com.example.pharmacywebsite.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserDto {
    private Integer id;
    private String email;
    private String fullName;
    private String role;
}
