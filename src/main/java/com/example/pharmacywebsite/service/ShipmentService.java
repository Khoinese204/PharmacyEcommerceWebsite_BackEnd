package com.example.pharmacywebsite.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.pharmacywebsite.domain.Order;
import com.example.pharmacywebsite.domain.Shipment;
import com.example.pharmacywebsite.dto.ShipmentCreateRequest;
import com.example.pharmacywebsite.dto.ShipmentResponse;
import com.example.pharmacywebsite.dto.ShipmentStatusUpdateRequest;
import com.example.pharmacywebsite.enums.ShipmentStatus;
import com.example.pharmacywebsite.repository.OrderRepository;
import com.example.pharmacywebsite.repository.ShipmentRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final ShipmentRepository shipmentRepo;
    private final OrderRepository orderRepo;

    public ShipmentResponse createShipment(ShipmentCreateRequest request) {
        Order order = orderRepo.findById(request.getOrderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        Shipment shipment = new Shipment();
        shipment.setOrder(order);
        shipment.setShipmentCode(request.getShipmentCode());
        shipment.setShippedBy(request.getShippedBy());
        shipment.setStatus(ShipmentStatus.WAITING);
        shipment.setShippedAt(LocalDateTime.now());

        shipment = shipmentRepo.save(shipment);

        return toResponse(shipment);
    }

    public List<ShipmentResponse> getAllShipments() {
        return shipmentRepo.findAll().stream().map(this::toResponse).toList();
    }

    public ShipmentResponse updateShipmentStatus(Integer shipmentId, ShipmentStatusUpdateRequest request) {
        Shipment shipment = shipmentRepo.findById(shipmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shipment not found"));

        shipment.setStatus(request.getStatus());

        if (request.getStatus() == ShipmentStatus.DELIVERED) {
            shipment.setDeliveredAt(LocalDateTime.now());
        }

        return toResponse(shipmentRepo.save(shipment));
    }

    private ShipmentResponse toResponse(Shipment shipment) {
        ShipmentResponse res = new ShipmentResponse();
        res.setId(shipment.getId());
        res.setOrderId(shipment.getOrder().getId());
        res.setShipmentCode(shipment.getShipmentCode());
        res.setShippedBy(shipment.getShippedBy());
        res.setShippedAt(shipment.getShippedAt());
        res.setDeliveredAt(shipment.getDeliveredAt());
        res.setStatus(shipment.getStatus());
        return res;
    }
}
