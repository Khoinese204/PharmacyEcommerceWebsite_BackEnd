package com.example.pharmacywebsite.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImportOrderItemRequest {
    private Integer medicineId;
    private Integer quantity;
    private Double unitPrice;
    private LocalDate expiredAt; // ✅ Thêm trường này để nhận ngày hết hạn từ frontend
}
