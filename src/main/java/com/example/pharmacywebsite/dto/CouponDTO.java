package com.example.pharmacywebsite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor

public class CouponDTO {
    private Integer id;
    private String name;
    private String description;
    private Double discountPercent;
    private String applicableCategoryName;
    private LocalDate startDate;
    private LocalDate endDate;
}
