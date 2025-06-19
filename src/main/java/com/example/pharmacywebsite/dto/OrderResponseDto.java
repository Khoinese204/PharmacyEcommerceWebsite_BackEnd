package com.example.pharmacywebsite.dto;

import com.example.pharmacywebsite.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderResponseDto {
    private Integer orderId;
    private Double totalPrice;
    private OrderStatus status;
}
