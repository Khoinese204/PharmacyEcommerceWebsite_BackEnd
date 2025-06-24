
package com.example.pharmacywebsite.dto;

import com.example.pharmacywebsite.enums.ShippingProvider;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShipmentUpdateRequest {
    private ShippingProvider shippedBy; // GHN, J_AND_T_EXPRESS, VTP
}
