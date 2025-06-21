package com.example.pharmacywebsite.seed;

import com.example.pharmacywebsite.domain.Order;
import com.example.pharmacywebsite.domain.Shipment;
import com.example.pharmacywebsite.enums.ShipmentStatus;
import com.example.pharmacywebsite.repository.OrderRepository;
import com.example.pharmacywebsite.repository.ShipmentRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class ShipmentSeeder {

    private final ShipmentRepository shipmentRepository;
    private final OrderRepository orderRepository;

    private final Random random = new Random();

    @PostConstruct
    public void seedShipments() {
        if (shipmentRepository.count() > 0)
            return;

        List<Order> orders = orderRepository.findAll();

        for (int i = 0; i < Math.min(10, orders.size()); i++) {
            Order order = orders.get(i);

            Shipment shipment = new Shipment();
            shipment.setOrder(order);
            shipment.setShipmentCode("SHP" + String.format("%03d", i + 1));
            shipment.setShippedBy("Nhân viên " + (i + 1));

            LocalDateTime shippedAt = LocalDateTime.now().minusDays(random.nextInt(10));
            shipment.setShippedAt(shippedAt);

            // Random status: DELIVERED hoặc SHIPPING
            if (i % 2 == 0) {
                shipment.setStatus(ShipmentStatus.DELIVERED);
                shipment.setDeliveredAt(shippedAt.plusDays(1));
            } else {
                shipment.setStatus(ShipmentStatus.SHIPPING);
                shipment.setDeliveredAt(null);
            }

            shipmentRepository.save(shipment);
        }

        System.out.println("✅ Seeded shipments successfully.");
    }
}
