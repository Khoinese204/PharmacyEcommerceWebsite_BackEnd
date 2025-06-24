// dto/WarehouseDashboardSummaryDto.java
package com.example.pharmacywebsite.dto;

import lombok.Data;

@Data
public class WarehouseDashboardDto {
    private long totalMedicineTypes;
    private long lowStockMedicines;
    private long expiredMedicines;
}
