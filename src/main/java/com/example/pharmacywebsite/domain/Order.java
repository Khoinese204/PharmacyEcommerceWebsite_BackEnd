package com.example.pharmacywebsite.domain;

import com.example.pharmacywebsite.enums.OrderStatus;
import com.example.pharmacywebsite.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
}
