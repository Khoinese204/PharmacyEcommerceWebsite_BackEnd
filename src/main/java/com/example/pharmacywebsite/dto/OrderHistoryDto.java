package com.example.pharmacywebsite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderHistoryDto {
    private String orderCode;
    private Double totalPrice;
    private String orderDate;
    private String status;
}