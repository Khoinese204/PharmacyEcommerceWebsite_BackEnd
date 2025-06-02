package com.example.pharmacywebsite.dto;

import com.example.pharmacywebsite.enums.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderDTO {
    private Integer userId;
    private PaymentMethod paymentMethod;
    private String recipientName;
    private String phone;
    private String province;
    private String district;
    private String ward;
    private String addressDetail;
    private String note;
    private Boolean requiresInvoice;
}
