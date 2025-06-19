package com.example.pharmacywebsite.designpattern.State;

import com.example.pharmacywebsite.enums.OrderStatus;

public class PendingState implements OrderStatusState {
    @Override
    public void next(OrderContext ctx) {
        ctx.setOrderStatus(OrderStatus.PACKING, "Chuyển trạng thái: PENDING → PACKING");
    }

    @Override
    public void cancel(OrderContext ctx) {
        ctx.setOrderStatus(OrderStatus.CANCELLED, "Huỷ đơn từ trạng thái PENDING");
    }
}
