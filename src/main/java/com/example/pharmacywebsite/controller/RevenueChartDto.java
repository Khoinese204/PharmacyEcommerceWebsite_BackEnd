package com.example.pharmacywebsite.controller;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RevenueChartDto {
    private int day;
    private Double revenue;
}
