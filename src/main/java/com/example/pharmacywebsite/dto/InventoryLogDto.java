package com.example.pharmacywebsite.dto;

import lombok.Data;

@Data
public class InventoryLogDto {
    private String medicineName;
    private String type; // import hoặc export
    private Integer quantity;
    private Integer relatedOrderId;
    private String createdAt;
}
