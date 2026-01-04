package com.example.pharmacywebsite.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "product_cross_sell_rule")
@Getter
@Setter
public class ProductCrossSellRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "cross_sell_id", nullable = false)
    private Integer crossSellId;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;
}
