package com.example.pharmacywebsite.controller;

import com.example.pharmacywebsite.dto.CreateOrderDTO;
import com.example.pharmacywebsite.dto.OrderDto;
import com.example.pharmacywebsite.dto.OrderItemDto;
import com.example.pharmacywebsite.dto.UpdateOrderStatusRequest;
import com.example.pharmacywebsite.exception.ApiException;
import com.example.pharmacywebsite.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody CreateOrderDTO dto) {
        return ResponseEntity.ok(orderService.createOrder(dto));
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDto>> getOrdersByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(orderService.getOrdersByUser(userId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderDetail(@PathVariable Integer orderId) {
        return ResponseEntity.ok(orderService.getOrderDetail(orderId));
    }

    @GetMapping("/{orderId}/items")
    public ResponseEntity<List<OrderItemDto>> getOrderItems(@PathVariable(required = false) Integer orderId) {
        System.out.println("🎯 Controller hit: /" + orderId + "/items");
        return ResponseEntity.ok(orderService.getOrderItems(orderId));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Integer id) {
        orderService.cancelOrder(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@RequestBody UpdateOrderStatusRequest req) {
        orderService.updateOrderStatus(req);
        return ResponseEntity.ok().build();
    }
}
