package com.example.pharmacywebsite.dto;

import lombok.Data;

@Data
public class AddToCartRequest {
    private Integer medicineId;
    private Integer quantity;
}
