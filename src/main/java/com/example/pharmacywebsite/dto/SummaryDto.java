package com.example.pharmacywebsite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SummaryDto {
    private Double totalPrice;
    private Double discount;
    private Double voucherDiscount;
    private Double shippingFee;
    private Double finalTotal;
}
