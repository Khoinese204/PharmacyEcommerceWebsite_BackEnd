package com.example.pharmacywebsite.service;

import com.example.pharmacywebsite.domain.Supplier;
import com.example.pharmacywebsite.dto.SupplierRequest;
import com.example.pharmacywebsite.dto.SupplierResponse;
import com.example.pharmacywebsite.repository.SupplierRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {
    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public List<SupplierResponse> getAllSuppliers() {
        return supplierRepository.findAll().stream()
                .map(supplier -> {
                    SupplierResponse dto = new SupplierResponse();
                    dto.setId(supplier.getId());
                    dto.setName(supplier.getName());
                    dto.setContactInfo(supplier.getContactInfo());
                    dto.setAddress(supplier.getAddress());
                    return dto;
                }).toList();
    }

    public Supplier createSupplier(SupplierRequest request) {
        Supplier supplier = new Supplier();
        supplier.setName(request.getName());
        supplier.setContactInfo(request.getContactInfo());
        supplier.setAddress(request.getAddress());
        return supplierRepository.save(supplier);
    }
}
