package com.example.pharmacywebsite.domain;

import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "medicines")
@Getter
@Setter
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private Double price;
    private Double originalPrice;
    private String unit;
    private String shortDescription;
    private String brandOrigin;
    private String manufacturer;
    private String countryOfManufacture;
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "medicine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicineDetail> details;

}
