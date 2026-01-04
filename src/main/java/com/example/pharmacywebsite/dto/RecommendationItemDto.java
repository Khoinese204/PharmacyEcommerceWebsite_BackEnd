package com.example.pharmacywebsite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecommendationItemDto {
    private Integer id;
    private String name;
    private String imageUrl;
    private Double price;
    private Double score;
    private String reason;
}