package com.example.pharmacywebsite.designpattern.State;

import com.example.pharmacywebsite.enums.OrderStatus;

public class CancelledState implements OrderStatusState {
    @Override
    public void next(OrderContext ctx) {
        ctx.setOrderStatus(OrderStatus.CANCELLED, "Đơn đã huỷ. Không thể chuyển tiếp.");
    }

    @Override
    public void cancel(OrderContext ctx) {
        ctx.setOrderStatus(OrderStatus.CANCELLED, "Đơn đã huỷ.");
    }
}
