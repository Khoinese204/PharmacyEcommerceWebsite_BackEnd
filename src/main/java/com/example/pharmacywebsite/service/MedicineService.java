package com.example.pharmacywebsite.service;

import com.example.pharmacywebsite.domain.Category;
import com.example.pharmacywebsite.domain.Medicine;
import com.example.pharmacywebsite.domain.MedicineDetail;
import com.example.pharmacywebsite.dto.MedicineDetailDto;
import com.example.pharmacywebsite.dto.MedicineDto;
import com.example.pharmacywebsite.repository.CategoryRepository;
import com.example.pharmacywebsite.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

        if (dto.getDetails() != null) {
            List<MedicineDetail> details = dto.getDetails().stream().map(d -> {
                MedicineDetail detail = new MedicineDetail();
                detail.setType(d.getType());
                detail.setContent(d.getContent());
                detail.setMedicine(med);
                return detail;
            }).toList();
            med.setDetails(details);
        }

        return toDto(medicineRepo.save(med));
    }

    public MedicineDto update(Integer id, MedicineDto dto) {
        Medicine med = medicineRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found"));

        if (dto.getName() != null)
            med.setName(dto.getName());
        if (dto.getPrice() != null)
            med.setPrice(dto.getPrice());
        if (dto.getOriginalPrice() != null)
            med.setOriginalPrice(dto.getOriginalPrice());
        if (dto.getUnit() != null)
            med.setUnit(dto.getUnit());
        if (dto.getShortDescription() != null)
            med.setShortDescription(dto.getShortDescription());
        if (dto.getBrandOrigin() != null)
            med.setBrandOrigin(dto.getBrandOrigin());
        if (dto.getManufacturer() != null)
            med.setManufacturer(dto.getManufacturer());
        if (dto.getCountryOfManufacture() != null)
            med.setCountryOfManufacture(dto.getCountryOfManufacture());
        if (dto.getImageUrl() != null)
            med.setImageUrl(dto.getImageUrl());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepo.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            med.setCategory(category);
        }

        if (dto.getDetails() != null) {
            med.getDetails().clear();
            List<MedicineDetail> details = dto.getDetails().stream().map(d -> {
                MedicineDetail detail = new MedicineDetail();
                detail.setType(d.getType());
                detail.setContent(d.getContent());
                detail.setMedicine(med);
                return detail;
            }).toList();
            med.getDetails().addAll(details);
        }

        return toDto(medicineRepo.save(med));
    }

    public void delete(Integer id) {
        if (!medicineRepo.existsById(id)) {
            throw new RuntimeException("Medicine not found");
        }
        medicineRepo.deleteById(id);
    }

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

        if (m.getDetails() != null) {
            List<MedicineDetailDto> detailDtos = m.getDetails().stream().map(d -> {
                MedicineDetailDto dDto = new MedicineDetailDto();
                dDto.setType(d.getType());
                dDto.setContent(d.getContent());
                return dDto;
            }).toList();
            dto.setDetails(detailDtos);
        }

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
