package com.example.pharmacywebsite.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TemplateImportOrderRequest {
    private String type; // "supplier" hoặc "return"
    private String source; // Tên nhà cung cấp hoặc lý do hoàn hàng
    private Integer supplierId; // Nếu là nhập từ nhà cung cấp
    private List<ImportOrderItemDto> items; // Danh sách mặt hàng

    // Tổng số lượng tính từ các mặt hàng
    public int getQuantity() {
        return items != null
                ? items.stream().mapToInt(ImportOrderItemDto::getQuantity).sum()
                : 0;
    }
}
