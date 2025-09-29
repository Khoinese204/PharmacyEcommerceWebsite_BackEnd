package com.example.pharmacywebsite.designpattern.State;

import com.example.pharmacywebsite.enums.OrderStatus;

public class PackingState implements OrderStatusState {
    @Override
    public void next(OrderContext ctx) {
        ctx.setOrderStatus(OrderStatus.DELIVERING, "Chuyển trạng thái: PACKING → DELIVERING");
    }

    @Override
    public void cancel(OrderContext ctx) {
        ctx.setOrderStatus(OrderStatus.CANCELLED, "Huỷ đơn từ trạng thái PACKING");
    }
}