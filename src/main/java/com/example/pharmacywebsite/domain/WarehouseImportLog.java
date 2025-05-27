package com.example.pharmacywebsite.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "warehouse_import_logs")
@Getter
@Setter
public class WarehouseImportLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "import_order_id")
    private ImportOrder importOrder;

    @ManyToOne
    @JoinColumn(name = "medicine_id")
    private Medicine medicine;

    private Integer quantityExpected;
    private Integer quantityReceived;

    @ManyToOne
    @JoinColumn(name = "confirmed_by")
    private User confirmedBy;

    private LocalDateTime confirmedAt;
    private String note;
}
