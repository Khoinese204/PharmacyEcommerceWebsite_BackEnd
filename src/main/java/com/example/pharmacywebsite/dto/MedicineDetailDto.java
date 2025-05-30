package com.example.pharmacywebsite.dto;

import com.example.pharmacywebsite.enums.MedicineDetailType;
import lombok.Data;

@Data
public class MedicineDetailDto {
    private MedicineDetailType type;
    private String content;
}
