// 📄 PromotionDTO.java
package com.example.pharmacywebsite.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor // 👈 cần có nếu bạn dùng Jackson hoặc không có constructor tay
@AllArgsConstructor
public class PromotionDTO {
    private Integer id;
    private String name;
    private String description;
    private Double discountPercent;
    private Integer applicableCategoryId;
}
