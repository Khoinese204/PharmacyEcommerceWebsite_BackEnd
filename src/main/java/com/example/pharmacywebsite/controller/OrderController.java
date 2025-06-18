// controller/OrderController.java
package com.example.pharmacywebsite.controller;

import com.example.pharmacywebsite.domain.Order;
import com.example.pharmacywebsite.dto.CreateOrderRequest;
import com.example.pharmacywebsite.dto.CreateOrderResponse;
import com.example.pharmacywebsite.service.OrderService;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest request) {
        Order order = orderService.createOrder(request);

        CreateOrderResponse response = new CreateOrderResponse();
        response.setOrderId("DH" + order.getId()); // như FE mong đợi
        response.setExpectedDeliveryDate(LocalDate.now().plusDays(3).toString()); // giả sử là ngày dự kiến giao tới là
                                                                                  // sau 3 ngày tính từ ngày hiện tại

        return ResponseEntity.ok(response);
    }
}
