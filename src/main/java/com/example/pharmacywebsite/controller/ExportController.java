package com.example.pharmacywebsite.controller;

import com.example.pharmacywebsite.dto.ExportOrderDto;
import com.example.pharmacywebsite.dto.OrderDetailResponse;
import com.example.pharmacywebsite.dto.UpdateOrderStatusRequest;
import com.example.pharmacywebsite.service.ExportService;
import com.example.pharmacywebsite.service.OrderService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exports")
@RequiredArgsConstructor
public class ExportController {

    private final ExportService exportService;

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<ExportOrderDto>> getExportOrders() {
        List<ExportOrderDto> orders = exportService.getAllExportOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponse> getExportOrderDetail(@PathVariable("orderId") Integer orderId) {
        OrderDetailResponse detail = orderService.getOrderById(orderId);
        return ResponseEntity.ok(detail);
    }
}
