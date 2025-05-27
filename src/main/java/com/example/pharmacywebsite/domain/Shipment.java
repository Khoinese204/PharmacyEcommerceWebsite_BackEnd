package com.example.pharmacywebsite.domain;

import com.example.pharmacywebsite.enums.ShipmentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "shipments")
@Getter
@Setter
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private String shipmentCode;
    private String shippedBy;

    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;

    @Enumerated(EnumType.STRING)
    private ShipmentStatus status;
}
