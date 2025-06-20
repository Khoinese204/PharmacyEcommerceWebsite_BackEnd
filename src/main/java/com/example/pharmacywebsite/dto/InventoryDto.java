package com.example.pharmacywebsite.dto;

import lombok.Data;

@Data
public class InventoryDto {
    private String batchNumber;
    private String productName;
    private Integer quantity;
    private String expiryDate;
    private String status;
}
