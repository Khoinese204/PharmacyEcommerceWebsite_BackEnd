package com.example.pharmacywebsite.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pharmacywebsite.dto.ImportOrderRequest;
import com.example.pharmacywebsite.dto.ImportOrderResponse;
import com.example.pharmacywebsite.dto.TemplateImportOrderRequest;
import com.example.pharmacywebsite.service.ImportOrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
public class ImportOrderController {
    private final ImportOrderService importOrderService;

    @PostMapping
    public ResponseEntity<?> createImportOrder(@RequestBody ImportOrderRequest request) {
        return ResponseEntity.ok(importOrderService.createImportOrder(request));
    }

    @GetMapping
    public ResponseEntity<List<ImportOrderResponse>> getAllImportOrders() {
        return ResponseEntity.ok(importOrderService.getAllImportOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImportOrderResponse> getImportOrderById(@PathVariable Integer id) {
        return ResponseEntity.ok(importOrderService.getImportOrderById(id));
    }

    @PostMapping("/process")
    public ResponseEntity<String> processImportOrder(@RequestBody TemplateImportOrderRequest request) {
        String result = importOrderService.processImportOrderWithTemplate(request);
        return ResponseEntity.ok(result);
    }

}
