package com.example.pharmacywebsite.dto;

import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImportOrderResponse {
    private Integer id;
    private String supplierName;
    private Double totalPrice;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    private List<ImportOrderItemResponse> items;
}
