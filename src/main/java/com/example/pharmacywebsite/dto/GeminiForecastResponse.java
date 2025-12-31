package com.example.pharmacywebsite.dto;
import lombok.Data;

@Data
public class GeminiForecastResponse {
    private int predictedSales;
    private String status;      
    private String reason;    
}