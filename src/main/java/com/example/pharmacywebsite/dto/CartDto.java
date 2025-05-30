package com.example.pharmacywebsite.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CartDto {
    private Integer id;
    private Integer userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CartItemDto> items;
}
