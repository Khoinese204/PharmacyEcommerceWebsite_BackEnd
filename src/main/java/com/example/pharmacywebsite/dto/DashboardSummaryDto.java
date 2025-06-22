package com.example.pharmacywebsite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardSummaryDto {
    private Double totalRevenue;
    private Long totalOrders;
    private Long totalCustomers;
    private Long lowStockProductTypes;
}
