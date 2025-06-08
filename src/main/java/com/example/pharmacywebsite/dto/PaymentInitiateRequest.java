package com.example.pharmacywebsite.dto;

import com.example.pharmacywebsite.enums.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentInitiateRequest {
    private Integer orderId;
    private Double amount;
    private PaymentMethod paymentMethod;
}
