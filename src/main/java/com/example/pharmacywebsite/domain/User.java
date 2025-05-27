package com.example.pharmacywebsite.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String email;

    private String fullName;

    @Column(nullable = false)
    private String passwordHash;

    private String gender;
    private LocalDate birthDate;
    private String address;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    private String avatarUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

