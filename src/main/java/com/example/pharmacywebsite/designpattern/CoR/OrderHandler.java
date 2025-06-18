package com.example.pharmacywebsite.designpattern.CoR;

public abstract class OrderHandler {
    protected OrderHandler next;

    public OrderHandler setNext(OrderHandler next) {
        this.next = next;
        return next;
    }

    public abstract void handle(OrderContext context);
}
