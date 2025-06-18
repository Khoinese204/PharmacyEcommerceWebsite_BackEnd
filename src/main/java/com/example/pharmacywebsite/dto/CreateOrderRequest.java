package com.example.pharmacywebsite.dto;

import com.example.pharmacywebsite.enums.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateOrderRequest {
    private Integer userId;
    private List<OrderItemDto> items;
    private Double totalPrice;
    private Double voucherDiscount;
    private Double shippingDiscount;
    private PaymentMethod paymentMethod;

    private ShippingInfoDto shippingInfo;

    private List<Integer> promotionIds;
    private Integer shippingPromotionId;
}
