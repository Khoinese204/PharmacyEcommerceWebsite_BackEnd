package com.example.pharmacywebsite.service;

import com.example.pharmacywebsite.domain.Category;
import com.example.pharmacywebsite.domain.Medicine;
import com.example.pharmacywebsite.dto.MedicineDto;
import com.example.pharmacywebsite.repository.CategoryRepository;
import com.example.pharmacywebsite.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicineService {

    @Autowired
    private MedicineRepository medicineRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    public List<MedicineDto> getAll(String keyword) {
        List<Medicine> medicines = (keyword != null && !keyword.isBlank())
                ? medicineRepo.findByNameContainingIgnoreCase(keyword)
                : medicineRepo.findAll();

        return medicines.stream().map(this::toDto).toList();
    }

    public MedicineDto getById(Integer id) {
        Medicine med = medicineRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found"));
        return toDto(med);
    }

    public MedicineDto create(MedicineDto dto) {
        Medicine med = toEntity(dto);
        Category category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        med.setCategory(category);
        return toDto(medicineRepo.save(med));
    }

    public MedicineDto update(Integer id, MedicineDto dto) {
        Medicine med = medicineRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found"));

        med.setName(dto.getName());
        med.setPrice(dto.getPrice());
        med.setOriginalPrice(dto.getOriginalPrice());
        med.setUnit(dto.getUnit());
        med.setShortDescription(dto.getShortDescription());
        med.setBrandOrigin(dto.getBrandOrigin());
        med.setManufacturer(dto.getManufacturer());
        med.setCountryOfManufacture(dto.getCountryOfManufacture());
        med.setImageUrl(dto.getImageUrl());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepo.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            med.setCategory(category);
        }

        return toDto(medicineRepo.save(med));
    }

    public void delete(Integer id) {
        if (!medicineRepo.existsById(id)) {
            throw new RuntimeException("Medicine not found");
        }
        medicineRepo.deleteById(id);
    }

    // Mapping functions
    private MedicineDto toDto(Medicine m) {
        MedicineDto dto = new MedicineDto();
        dto.setId(m.getId());
        dto.setName(m.getName());
        dto.setPrice(m.getPrice());
        dto.setOriginalPrice(m.getOriginalPrice());
        dto.setUnit(m.getUnit());
        dto.setShortDescription(m.getShortDescription());
        dto.setBrandOrigin(m.getBrandOrigin());
        dto.setManufacturer(m.getManufacturer());
        dto.setCountryOfManufacture(m.getCountryOfManufacture());
        dto.setImageUrl(m.getImageUrl());
        dto.setCategoryId(m.getCategory() != null ? m.getCategory().getId() : null);
        return dto;
    }

    private Medicine toEntity(MedicineDto dto) {
        Medicine m = new Medicine();
        m.setName(dto.getName());
        m.setPrice(dto.getPrice());
        m.setOriginalPrice(dto.getOriginalPrice());
        m.setUnit(dto.getUnit());
        m.setShortDescription(dto.getShortDescription());
        m.setBrandOrigin(dto.getBrandOrigin());
        m.setManufacturer(dto.getManufacturer());
        m.setCountryOfManufacture(dto.getCountryOfManufacture());
        m.setImageUrl(dto.getImageUrl());
        return m;
    }
}
