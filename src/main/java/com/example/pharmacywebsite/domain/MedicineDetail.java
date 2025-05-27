package com.example.pharmacywebsite.domain;

import com.example.pharmacywebsite.enums.MedicineDetailType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "medicine_details")
@Getter
@Setter
public class MedicineDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "medicine_id")
    private Medicine medicine;

    @Enumerated(EnumType.STRING)
    private MedicineDetailType type;

    private String content;
}
