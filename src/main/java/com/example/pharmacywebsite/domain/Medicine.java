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

    @Column(columnDefinition = "TEXT") // Tên thuốc có thể dài hơn 255 kí tự
    private String name;

    private Double price;
    private Double originalPrice;
    private String unit;

    @Column(columnDefinition = "TEXT")
    private String shortDescription;


    
    @Column(columnDefinition = "TEXT")
    private String brandOrigin;

    @Column(columnDefinition = "TEXT")
    private String manufacturer;

    @Column(columnDefinition = "TEXT")
    private String countryOfManufacture;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "medicine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicineDetail> details;
}
