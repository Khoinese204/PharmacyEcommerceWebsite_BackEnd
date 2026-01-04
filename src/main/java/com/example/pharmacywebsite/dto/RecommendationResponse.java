package com.example.pharmacywebsite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RecommendationResponse {
    private Integer productId;
    private String type; // "SIMILAR" | "CROSS_SELL"
    private List<RecommendationItemDto> items;
}
