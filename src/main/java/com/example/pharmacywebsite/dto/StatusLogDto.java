package com.example.pharmacywebsite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatusLogDto {
    private String status;
    private String time;
}