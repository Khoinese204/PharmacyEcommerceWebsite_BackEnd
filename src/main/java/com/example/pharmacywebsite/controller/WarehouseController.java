package com.example.pharmacywebsite.controller;

import com.example.pharmacywebsite.domain.User;
import com.example.pharmacywebsite.dto.ImportOrderSummaryDto;
import com.example.pharmacywebsite.dto.InventoryDto;
import com.example.pharmacywebsite.dto.WarehouseDashboardDto;
import com.example.pharmacywebsite.repository.UserRepository;
import com.example.pharmacywebsite.service.WarehouseService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/warehouse")
@RequiredArgsConstructor
public class WarehouseController {

    private final UserRepository userRepository;
    private final WarehouseService warehouseService;

    @PostMapping("/import/confirm")
    public ResponseEntity<String> confirmImport(@RequestParam Integer importOrderId,
            @RequestParam Integer confirmedById) {
        User confirmedBy = userRepository.findById(confirmedById)
                .orElseThrow(() -> new RuntimeException("User not found"));

        warehouseService.confirmImportOrder(importOrderId, confirmedBy);
        return ResponseEntity.ok("Xác nhận nhập kho thành công.");
    }

    @GetMapping("/inventory")
    public ResponseEntity<List<InventoryDto>> getInventory() {
        List<InventoryDto> inventoryList = warehouseService.getInventoryList();
        return ResponseEntity.ok(inventoryList);
    }

    @GetMapping("/import")
    public ResponseEntity<List<ImportOrderSummaryDto>> getAllImportOrders() {
        List<ImportOrderSummaryDto> orders = warehouseService.getAllImportOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/dashboard-summary")
    public ResponseEntity<WarehouseDashboardDto> getWarehouseDashboardSummary() {
        WarehouseDashboardDto summary = warehouseService.getDashboardSummary();
        return ResponseEntity.ok(summary);
    }

}
