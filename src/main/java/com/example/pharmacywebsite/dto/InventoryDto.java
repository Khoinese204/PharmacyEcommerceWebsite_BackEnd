package com.example.pharmacywebsite.dto;

import java.util.List;

import lombok.Data;

@Data
public class InventoryDto {
    private int id;
    private String batchNumber;
    private String productName;
    private Integer quantity;
    private String expiryDate;
    private String status;
    private String dateStatus;

}
