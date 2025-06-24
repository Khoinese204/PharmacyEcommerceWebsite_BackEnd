package com.example.pharmacywebsite.dto;

import java.time.LocalDateTime;

import com.example.pharmacywebsite.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExportOrderDto {
    private Integer id; // ID của OrderShippingInfo
    private Integer orderId; // mã đơn hàng từ Order
    private String recipientName;
    private String phone;
    private String province;
    private String district;
    private String ward;
    private String addressDetail;
    private OrderStatus status;
}
