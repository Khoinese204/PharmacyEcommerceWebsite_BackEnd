package com.example.pharmacywebsite.dto;

import com.example.pharmacywebsite.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderDto {
    private Integer orderId;
    private LocalDateTime orderDate;
    private String customer;
    private OrderStatus status;
    private Double totalPrice;
}
