package com.example.pharmacywebsite.designpattern.TemplateMethod;

import com.example.pharmacywebsite.dto.TemplateImportOrderRequest;

// SupplierImportProcessor.java
public class SupplierImportProcessor extends AbstractStockImportProcessor<TemplateImportOrderRequest> {
    @Override
    protected void persistImportRecord(TemplateImportOrderRequest request) {
        System.out.println("[Supplier] Saving import record from supplier: " + request.getSource());
    }

    @Override
    protected void updateInventory(TemplateImportOrderRequest request) {
        System.out.println("[Supplier] Increasing inventory by quantity: " + request.getQuantity());
    }
}
