package com.example.pharmacywebsite.designpattern.State;

import com.example.pharmacywebsite.domain.Order;
import com.example.pharmacywebsite.domain.OrderStatusLog;
import com.example.pharmacywebsite.domain.User;
import com.example.pharmacywebsite.enums.OrderStatus;
import com.example.pharmacywebsite.repository.OrderRepository;
import com.example.pharmacywebsite.repository.OrderStatusLogRepository;

import java.time.LocalDateTime;

public class OrderContext {
    private final Order order;
    private final User updatedBy;
    private final OrderRepository orderRepo;
    private final OrderStatusLogRepository logRepo;

    public OrderContext(Order order, User updatedBy,
            OrderRepository orderRepo,
            OrderStatusLogRepository logRepo) {
        this.order = order;
        this.updatedBy = updatedBy;
        this.orderRepo = orderRepo;
        this.logRepo = logRepo;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrderStatus(OrderStatus newStatus, String note) {
        OrderStatus oldStatus = order.getStatus();
        order.setStatus(newStatus);
        orderRepo.save(order);

        OrderStatusLog log = new OrderStatusLog();
        log.setOrder(order);
        log.setStatus(newStatus);
        log.setUpdatedAt(LocalDateTime.now());
        log.setUpdatedBy(updatedBy);
        log.setNote(note + " (from " + oldStatus + " to " + newStatus + ")");
        logRepo.save(log);
    }

    public void next() {
        getState().next(this);
    }

    public void cancel() {
        getState().cancel(this);
    }

    private OrderStatusState getState() {
        return switch (order.getStatus()) {
            case PENDING -> new PendingState();
            case PACKING -> new PackingState();
            case DELIVERING -> new DeliveringState();
            case DELIVERED -> new DeliveredState();
            case CANCELLED -> new CancelledState();
        };
    }
}
