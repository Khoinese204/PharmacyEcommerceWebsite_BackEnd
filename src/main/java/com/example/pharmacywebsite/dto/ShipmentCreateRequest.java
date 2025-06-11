package com.example.pharmacywebsite.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShipmentCreateRequest {
    private Integer orderId;
    private String shipmentCode;
    private String shippedBy;
}
