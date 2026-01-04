package com.example.pharmacywebsite.dto;

import lombok.Data;
import java.util.List;

@Data
public class ForecastResultList {
    private List<Item> results;

    @Data
    public static class Item {
        private Integer medicineId;
        private int predictedSales;
        private String status;
        private String reason;
    }
}