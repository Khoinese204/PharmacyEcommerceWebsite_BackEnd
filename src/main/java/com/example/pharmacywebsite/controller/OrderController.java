// controller/OrderController.java
package com.example.pharmacywebsite.controller;

import com.example.pharmacywebsite.designpattern.Facade.OrderPlacementFacade;
import com.example.pharmacywebsite.domain.Order;
import com.example.pharmacywebsite.dto.CreateOrderRequest;
import com.example.pharmacywebsite.dto.CreateOrderResponse;
import com.example.pharmacywebsite.dto.OrderDetailDto;
import com.example.pharmacywebsite.dto.OrderDetailResponse;
import com.example.pharmacywebsite.dto.OrderDto;
import com.example.pharmacywebsite.dto.OrderHistoryDto;
import com.example.pharmacywebsite.dto.OrderResponseDto;
import com.example.pharmacywebsite.dto.UpdateOrderStatusRequest;
import com.example.pharmacywebsite.service.OrderService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderPlacementFacade orderPlacementFacade;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest request) {
        OrderResponseDto dto = orderPlacementFacade.placeOrder(request); // ✅ Gọi Facade

        CreateOrderResponse response = new CreateOrderResponse();
        response.setOrderId("DH" + dto.getOrderId()); // FE mong đợi format mã đơn
        response.setExpectedDeliveryDate(LocalDate.now().plusDays(3).toString()); // Tạm hardcode 3 ngày

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailResponse> getOrderDetail(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getOrdersByUserId(@PathVariable("userId") Integer userId) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateOrderStatus(
            @PathVariable("id") Integer id,
            @RequestBody UpdateOrderStatusRequest request) {
        orderService.updateOrderStatus(id, request);
        return ResponseEntity.ok("Order status updated successfully");
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getOrderStats() {
        Map<String, Long> stats = orderService.getOrderStatusCounts();
        return ResponseEntity.ok(stats);
    }

}
