package com.example.pharmacywebsite.domain;

import com.example.pharmacywebsite.enums.PaymentMethod;
import com.example.pharmacywebsite.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment_transactions")
@Getter
@Setter
public class PaymentTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private String providerTransactionId;
    private Double amount;

    @Enumerated(EnumType.STRING) // mặc định là success vì khi ấn nút đã chuyển khoản thì success luôn
    private PaymentStatus status;

    private LocalDateTime paidAt;
    private LocalDateTime createdAt;
}
