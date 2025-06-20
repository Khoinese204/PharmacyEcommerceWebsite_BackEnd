package com.example.pharmacywebsite.designpattern.Facade;

import com.example.pharmacywebsite.dto.CreateOrderRequest;
import com.example.pharmacywebsite.dto.OrderResponseDto;
import com.example.pharmacywebsite.service.*;
import com.example.pharmacywebsite.domain.Order;
import com.example.pharmacywebsite.domain.OrderItem;
import com.example.pharmacywebsite.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderPlacementFacade {

    private final OrderService orderService;
    private final InventoryService inventoryService;
    private final OrderStatusLogService orderStatusLogService;
    private final ShipmentService shipmentService;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public OrderResponseDto placeOrder(CreateOrderRequest request) {
        Order order = orderService.createOrder(request);

        List<OrderItem> items = orderItemRepository.findByOrder(order);
        inventoryService.deductStock(items);

        orderStatusLogService.logInitialStatus(order);

        shipmentService.createShipmentForOrder(order);

        return new OrderResponseDto(order.getId(), order.getTotalPrice(), order.getStatus());
    }
}
