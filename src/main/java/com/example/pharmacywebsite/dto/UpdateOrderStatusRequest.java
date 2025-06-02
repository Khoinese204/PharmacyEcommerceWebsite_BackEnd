package com.example.pharmacywebsite.dto;

import com.example.pharmacywebsite.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderStatusRequest {
    private Integer orderId;
    private OrderStatus newStatus;
    private Integer updatedByUserId;
    private String note;
}
