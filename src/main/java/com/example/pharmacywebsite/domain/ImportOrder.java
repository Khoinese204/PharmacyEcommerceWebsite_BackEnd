package com.example.pharmacywebsite.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "import_orders")
@Getter
@Setter
public class ImportOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    private LocalDateTime createdAt;
    private Double totalPrice;

    @OneToMany(mappedBy = "importOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImportOrderItem> importOrderItems;
}
