package com.example.pharmacywebsite.designpattern.TemplateMethod;

public abstract class AbstractStockImportProcessor<T> {

    public final void process(T request) {
        validate(request);
        persistImportRecord(request);
        updateInventory(request);
        logImport(request);
    }

    protected void validate(T request) {
        System.out.println("[Validate] Default validation logic...");
    }

    protected abstract void persistImportRecord(T request);

    protected abstract void updateInventory(T request);

    protected void logImport(T request) {
        System.out.println("[Log] Import processed: " + request.toString());
    }
}
