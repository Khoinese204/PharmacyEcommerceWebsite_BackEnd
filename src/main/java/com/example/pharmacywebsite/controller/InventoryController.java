package com.example.pharmacywebsite.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("/quantity/{medicineId}")
    public ResponseEntity<Integer> getInventoryQuantity(@PathVariable("medicineId") Integer medicineId) {
        int quantity = inventoryService.getQuantityByMedicineId(medicineId);
        return ResponseEntity.ok(quantity);
    }

    @PostMapping("/quantities")
    public ResponseEntity<Map<Integer, Integer>> getQuantitiesForMedicines(@RequestBody List<Integer> medicineIds) {
        Map<Integer, Integer> map = inventoryService.getTotalQuantitiesByMedicineIds(medicineIds);
        return ResponseEntity.ok(map);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateInventoryQuantity(
            @PathVariable("id") int inventoryId,
            @RequestBody Map<String, Integer> body) {

        if (!body.containsKey("quantity")) {
            return ResponseEntity.badRequest().body("Missing 'quantity' field in request body");
        }

        Integer newQuantity = body.get("quantity");
        inventoryService.updateInventoryQuantity(inventoryId, newQuantity);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryDto> getInventoryById(@PathVariable("id") int inventoryId) {
        InventoryDto inventory = inventoryService.getInventoryById(inventoryId);
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/alerts")
    public ResponseEntity<Map<String, List<InventoryDto>>> getInventoryAlerts() {
        Map<String, List<InventoryDto>> alerts = inventoryService.getInventoryAlerts();
        return ResponseEntity.ok(alerts);
    }

}
