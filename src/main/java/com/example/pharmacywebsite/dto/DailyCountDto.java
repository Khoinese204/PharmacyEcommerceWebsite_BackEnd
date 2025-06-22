package com.example.pharmacywebsite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DailyCountDto {
    private int day;
    private long count;
}
