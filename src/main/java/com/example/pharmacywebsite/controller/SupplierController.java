package com.example.pharmacywebsite.controller;

import com.example.pharmacywebsite.domain.Supplier;
import com.example.pharmacywebsite.dto.SupplierRequest;
import com.example.pharmacywebsite.dto.SupplierResponse;
import com.example.pharmacywebsite.service.SupplierService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {
    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public ResponseEntity<List<SupplierResponse>> getAllSuppliers() {
        return ResponseEntity.ok(supplierService.getAllSuppliers());
    }

    @PostMapping
    public ResponseEntity<Supplier> createSupplier(@RequestBody SupplierRequest request) {
        return ResponseEntity.ok(supplierService.createSupplier(request));
    }
}
