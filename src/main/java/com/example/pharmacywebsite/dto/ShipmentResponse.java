package com.example.pharmacywebsite.dto;

import com.example.pharmacywebsite.enums.ShipmentStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ShipmentResponse {
    private Integer id;
    private Integer orderId;
    private String shipmentCode;
    private String shippedBy;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;
    private ShipmentStatus status;
}
