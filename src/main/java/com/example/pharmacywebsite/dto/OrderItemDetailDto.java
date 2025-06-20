package com.example.pharmacywebsite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemDetailDto {
    private String medicineName;
    private String imageUrl;
    private String unit;
    private Double unitPrice;
    private Double originalPrice;
    private Integer quantity;
}