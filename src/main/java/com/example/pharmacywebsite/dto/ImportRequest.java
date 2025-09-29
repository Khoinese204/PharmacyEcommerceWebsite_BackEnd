package com.example.pharmacywebsite.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ImportRequest {
    private String source;
    private int quantity;

    @Override
    public String toString() {
        return "Import from " + source + ", qty = " + quantity;
    }
}
