package com.example.pharmacywebsite.designpattern.State;

import com.example.pharmacywebsite.enums.OrderStatus;

public class DeliveringState implements OrderStatusState {
    @Override
    public void next(OrderContext ctx) {
        ctx.setOrderStatus(OrderStatus.DELIVERED, "Chuyển trạng thái: DELIVERING → DELIVERED");
    }

    @Override
    public void cancel(OrderContext ctx) {
        ctx.setOrderStatus(OrderStatus.CANCELLED, "Huỷ đơn từ trạng thái DELIVERING");
    }
}