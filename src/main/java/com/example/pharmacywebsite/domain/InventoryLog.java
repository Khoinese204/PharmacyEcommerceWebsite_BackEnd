package com.example.pharmacywebsite.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "inventory_logs")
@Getter
@Setter
public class InventoryLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "medicine_id")
    private Medicine medicine;

    private String type; // import/export
    private Integer quantity;
    private Integer relatedOrderId; // ref đơn nhập hoặc đơn bán

    private LocalDateTime createdAt;
}
