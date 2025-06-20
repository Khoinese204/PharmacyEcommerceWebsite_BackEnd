package com.example.pharmacywebsite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerInfoDto {
    private String fullName;
    private String phone;
    private String address;
    private String note;
}