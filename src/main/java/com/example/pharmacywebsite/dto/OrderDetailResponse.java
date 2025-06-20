package com.example.pharmacywebsite.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

// dto/OrderDetailResponse.java
@AllArgsConstructor
@Data
public class OrderDetailResponse {
    private Integer orderId;
    private String orderCode;
    private LocalDateTime orderDate;
    private LocalDate expectedDeliveryDate;
    private String status;
    private List<StatusLogDto> statusLogs;
    private List<ItemDto> items;
    private SummaryDto summary;
    private PaymentDto payment;
    private CustomerInfoDto customerInfo;
    private boolean canCancel;
}