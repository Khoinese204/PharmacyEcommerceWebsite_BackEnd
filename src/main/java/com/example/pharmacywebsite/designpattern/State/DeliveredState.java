package com.example.pharmacywebsite.designpattern.State;

import com.example.pharmacywebsite.enums.OrderStatus;

public class DeliveredState implements OrderStatusState {
    @Override
    public void next(OrderContext ctx) {
        ctx.setOrderStatus(OrderStatus.DELIVERED, "Đơn hàng đã hoàn tất. Không thể chuyển tiếp.");
    }

    @Override
    public void cancel(OrderContext ctx) {
        ctx.setOrderStatus(OrderStatus.DELIVERED, "Không thể huỷ đơn đã giao.");
    }
}