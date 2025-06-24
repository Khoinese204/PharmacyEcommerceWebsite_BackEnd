package com.example.pharmacywebsite.designpattern.TemplateMethod;

import com.example.pharmacywebsite.dto.TemplateImportOrderRequest;

public class ReturnImportProcessor extends AbstractStockImportProcessor<TemplateImportOrderRequest> {

    @Override
    protected void persistImportRecord(TemplateImportOrderRequest request) {
        System.out.println("[Return] Saving return import record: " + request.getSource());
    }

    @Override
    protected void updateInventory(TemplateImportOrderRequest request) {
        System.out.println("[Return] Adding returned quantity to inventory: " + request.getQuantity());
    }

    @Override
    protected void logImport(TemplateImportOrderRequest request) {
        System.out.println("[Return] Logged return import from: " + request.getSource());
    }
}
