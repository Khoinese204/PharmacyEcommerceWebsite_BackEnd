package com.example.pharmacywebsite.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImportOrderResponse {
    private Integer id;
    private String supplierName;
    private Double totalPrice;
    private LocalDateTime createdAt;
    private List<ImportOrderItemResponse> items;
}
