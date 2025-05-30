package com.example.pharmacywebsite.dto;

import lombok.Data;

@Data
public class CartItemDto {
    private Integer id;
    private Integer medicineId;
    private String medicineName;
    private Integer quantity;
}
