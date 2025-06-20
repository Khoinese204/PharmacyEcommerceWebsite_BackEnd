package com.example.pharmacywebsite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemDto {
    private String medicineName;
    private String imageUrl;
    private String unit;
    private double unitPrice;
    private double originalPrice;
    private int quantity;
}