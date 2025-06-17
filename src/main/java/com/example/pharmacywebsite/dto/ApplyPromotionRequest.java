package com.example.pharmacywebsite.dto;

import java.util.List;

import lombok.Data;

@Data
public class ApplyPromotionRequest {
    private Integer promotionId;
    private List<CartItemRequest> selectedItems;
}
