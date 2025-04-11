package com.example.pharmacywebsite.domain;

import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "person")
public class Person {
    @Id
    private long id;

    private String name;

}
