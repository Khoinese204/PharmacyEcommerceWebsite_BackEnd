package com.example.pharmacywebsite.dto;

import java.util.List;

import lombok.Data;

@Data
public class MedicineDto {
    private Integer id;
    private String name;
    private Double price;
    private Double originalPrice;
    private String unit;
    private String shortDescription;
    private String brandOrigin;
    private String manufacturer;
    private String countryOfManufacture;
    private String imageUrl;
    private Integer categoryId;

    private List<MedicineDetailDto> details; // thêm dòng này
}
