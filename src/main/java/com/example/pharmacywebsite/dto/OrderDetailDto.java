package com.example.pharmacywebsite.dto;

import java.util.List;

import lombok.Data;

@Data
public class OrderDetailDto {
    private String orderCode;
    private String orderDate;
    private String expectedDeliveryDate;
    private String status;
    private List<StatusLogDto> statusLogs;
    private List<OrderItemDetailDto> items;
    private SummaryDto summary;
    private PaymentDto payment;
    private CustomerInfoDto customerInfo;
    private boolean canCancel;
}
