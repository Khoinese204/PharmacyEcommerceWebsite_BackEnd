package com.example.pharmacywebsite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PriceCalculationResponse {
    private double finalPrice;
}
