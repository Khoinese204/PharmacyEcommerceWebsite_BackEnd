package com.example.pharmacywebsite.dto;

import java.time.LocalDateTime;

import com.example.pharmacywebsite.enums.PaymentMethod;
import com.example.pharmacywebsite.enums.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponse {
    private Integer transactionId;
    private Integer orderId;
    private Double amount;
    private PaymentStatus status;
    private LocalDateTime paidAt;
    private PaymentMethod paymentMethod;
}
