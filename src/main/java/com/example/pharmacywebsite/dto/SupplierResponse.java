package com.example.pharmacywebsite.dto;

import lombok.Data;

@Data
public class SupplierResponse {
    private Integer id;
    private String name;
    private String contactInfo;
    private String address;

}
