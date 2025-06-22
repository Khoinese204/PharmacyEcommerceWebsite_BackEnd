package com.example.pharmacywebsite.dto;

import lombok.Data;

@Data
public class ExportItemDto {
    private String medicineName;
    private int quantity;
    private double unitPrice;
}
