package com.example.pharmacywebsite.dto;

import lombok.Data;

import java.util.List;

@Data
public class ExportDetailResponse {
    private String exportCode;
    private String exportDate;
    private String status;
    private List<ExportItemDto> items;
    private CustomerInfoDto customerInfo;
    private SummaryDto summary;
}
