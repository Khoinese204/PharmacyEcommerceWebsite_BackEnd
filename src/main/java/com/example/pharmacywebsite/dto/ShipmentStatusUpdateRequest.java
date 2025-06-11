package com.example.pharmacywebsite.dto;

import com.example.pharmacywebsite.enums.ShipmentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShipmentStatusUpdateRequest {
    private ShipmentStatus status;
}
