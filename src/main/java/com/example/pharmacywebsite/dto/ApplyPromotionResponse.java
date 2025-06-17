package com.example.pharmacywebsite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApplyPromotionResponse {
    private boolean success;
    private double discountAmount;
    private String message;
}