package com.example.pharmacywebsite.designpattern.State;

public interface OrderStatusState {
    void next(OrderContext ctx);

    void cancel(OrderContext ctx);
}