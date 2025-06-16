package com.example.pharmacywebsite.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImportOrderItemResponse {
    private Integer id;
    private String medicineName;
    private Integer quantity;
    private Double unitPrice;
}
