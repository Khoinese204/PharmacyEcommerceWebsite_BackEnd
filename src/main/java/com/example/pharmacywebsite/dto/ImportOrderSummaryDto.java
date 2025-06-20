package com.example.pharmacywebsite.dto;

import java.time.LocalDateTime;

import com.example.pharmacywebsite.enums.ImportOrderStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImportOrderSummaryDto {
    private Integer id;
    private String supplierName;
    private Double totalPrice;
    private LocalDateTime createdAt;
    private ImportOrderStatus status;
}
