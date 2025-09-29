package com.example.pharmacywebsite.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImportOrderItemDto {
    private Integer medicineId; // ID của thuốc
    private Integer quantity; // Số lượng nhập
    private Double importPrice; // Giá nhập từng đơn vị
}
