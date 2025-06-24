package com.example.pharmacywebsite.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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

    public void createShipmentForOrder(Order order) {
        Shipment shipment = new Shipment();
        shipment.setOrder(order);

        // Sinh mã vận đơn: SHP001, SHP002,...
        long shipmentCount = shipmentRepo.count();
        String shipmentCode = String.format("SHP%03d", shipmentCount + 1);
        shipment.setShipmentCode(shipmentCode);

        // Đơn hàng mới tạo nên chưa phân công giao hàng
        shipment.setStatus(ShipmentStatus.WAITING);
        shipment.setShippedBy(null);
        shipment.setShippedAt(null);
        shipment.setDeliveredAt(null);

        shipmentRepo.save(shipment);
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
