package com.example.pharmacywebsite.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.pharmacywebsite.domain.Order;
import com.example.pharmacywebsite.domain.Shipment;
import com.example.pharmacywebsite.dto.ShipmentResponse;
import com.example.pharmacywebsite.dto.ShipmentStatusUpdateRequest;
import com.example.pharmacywebsite.dto.ShipmentUpdateRequest;
import com.example.pharmacywebsite.enums.OrderStatus;
import com.example.pharmacywebsite.enums.ShipmentStatus;
import com.example.pharmacywebsite.repository.OrderRepository;
import com.example.pharmacywebsite.repository.ShipmentRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final ShipmentRepository shipmentRepo;
    private final OrderRepository orderRepo;

    // ... (Giữ nguyên các hàm createShipmentForOrder và getAllShipments) ...

    public void createShipmentForOrder(Order order) {
        Shipment shipment = new Shipment();
        shipment.setOrder(order);
        long shipmentCount = shipmentRepo.count();
        String shipmentCode = String.format("SHP%03d", shipmentCount + 1);
        shipment.setShipmentCode(shipmentCode);
        shipment.setStatus(ShipmentStatus.WAITING);
        shipmentRepo.save(shipment);
    }

    public List<ShipmentResponse> getAllShipments() {
        return shipmentRepo.findAll().stream().map(this::toResponse).toList();
    }

    // --- CẬP NHẬT TRẠNG THÁI (Thủ công) ---
    @Transactional
    public ShipmentResponse updateShipmentStatus(Integer shipmentId, ShipmentStatusUpdateRequest request) {
        Shipment shipment = shipmentRepo.findById(shipmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shipment not found"));

        // 1. Cập nhật trạng thái Vận đơn
        shipment.setStatus(request.getStatus());

        Order order = shipment.getOrder();

        // 2. Đồng bộ sang Đơn hàng (Dựa trên Enum của bạn)
        if (request.getStatus() == ShipmentStatus.DELIVERED) {
            shipment.setDeliveredAt(LocalDateTime.now());
            order.setStatus(OrderStatus.DELIVERED); // Đồng bộ: Đã giao
        } else if (request.getStatus() == ShipmentStatus.SHIPPING) {
            order.setStatus(OrderStatus.DELIVERING); // Đồng bộ: Đang giao
        }
        // Lưu ý: Nếu bạn muốn hủy đơn, cần làm ở quy trình khác hoặc thêm CANCELLED vào
        // Enum ShipmentStatus

        orderRepo.save(order); // Lưu Order
        return toResponse(shipmentRepo.save(shipment)); // Lưu Shipment
    }

    // --- GÁN SHIPPER (Bắt đầu giao hàng) ---
    @Transactional
    public ShipmentResponse assignShipment(Integer shipmentId, ShipmentUpdateRequest request) {
        Shipment shipment = shipmentRepo.findById(shipmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy shipment"));

        // 1. Cập nhật thông tin Shipper
        shipment.setShippedBy(request.getShippedBy().name());
        shipment.setShippedAt(LocalDateTime.now());

        // Chuyển trạng thái vận đơn sang SHIPPING
        shipment.setStatus(ShipmentStatus.SHIPPING);

        // 2. QUAN TRỌNG: Đồng bộ Đơn hàng sang DELIVERING
        Order order = shipment.getOrder();
        order.setStatus(OrderStatus.DELIVERING);
        orderRepo.save(order);

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