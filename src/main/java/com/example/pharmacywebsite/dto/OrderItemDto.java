package com.example.pharmacywebsite.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {
    private Integer medicineId;
    private Integer quantity;
    private Double unitPrice;
}
