package com.example.pharmacywebsite.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImportOrderRequest {
    private Integer supplierId;
    private List<ImportOrderItemRequest> items;
}