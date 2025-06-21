package com.example.pharmacywebsite.controller;

import com.example.pharmacywebsite.dto.ShipmentCreateRequest;
import com.example.pharmacywebsite.dto.ShipmentResponse;
import com.example.pharmacywebsite.dto.ShipmentStatusUpdateRequest;
import com.example.pharmacywebsite.service.ShipmentService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipments")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;

    // @PostMapping
    // public ResponseEntity<ShipmentResponse> createShipment(@RequestBody
    // ShipmentCreateRequest request) {
    // return ResponseEntity.ok(shipmentService.createShipment(request));
    // }

    @GetMapping
    public ResponseEntity<List<ShipmentResponse>> getAllShipments() {
        return ResponseEntity.ok(shipmentService.getAllShipments());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ShipmentResponse> updateShipmentStatus(
            @PathVariable Integer id,
            @RequestBody ShipmentStatusUpdateRequest request) {
        return ResponseEntity.ok(shipmentService.updateShipmentStatus(id, request));
    }
}
