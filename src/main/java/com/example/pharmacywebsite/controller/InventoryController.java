package com.example.pharmacywebsite.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pharmacywebsite.dto.InventoryDto;
import com.example.pharmacywebsite.dto.InventoryLogDto;
import com.example.pharmacywebsite.service.InventoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping
    public List<InventoryDto> getAllInventory() {
        return inventoryService.getAllInventory();
    }

    @GetMapping("/history")
    public List<InventoryLogDto> getInventoryLogs() {
        return inventoryService.getInventoryLogs();
    }

    // @PatchMapping("/{id}")
    // public ResponseEntity<?> updateInventoryQuantity(
    // @PathVariable String batchNumber,
    // @RequestBody Map<String, Integer> body) {
    // Integer quantity = body.get("quantity");
    // inventoryService.updateQuantity(batchNumber, quantity);
    // return ResponseEntity.ok().build();
    // }

}
