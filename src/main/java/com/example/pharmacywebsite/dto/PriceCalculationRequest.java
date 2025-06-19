package com.example.pharmacywebsite.dto;

import lombok.Data;

@Data
public class PriceCalculationRequest {
    private Double originalPrice;
    private Double discountPercent;
    private Double vatPercent;
}
