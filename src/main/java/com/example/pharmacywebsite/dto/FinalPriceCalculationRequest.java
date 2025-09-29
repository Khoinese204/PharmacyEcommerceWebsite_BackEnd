package com.example.pharmacywebsite.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FinalPriceCalculationRequest {
    private List<OrderItemDto> items;
}