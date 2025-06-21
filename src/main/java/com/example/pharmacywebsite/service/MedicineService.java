package com.example.pharmacywebsite.service;

import com.example.pharmacywebsite.domain.Category;
import com.example.pharmacywebsite.domain.Medicine;
import com.example.pharmacywebsite.domain.MedicineDetail;
import com.example.pharmacywebsite.domain.User;
import com.example.pharmacywebsite.dto.MedicineDetailDto;
import com.example.pharmacywebsite.dto.MedicineDto;
import com.example.pharmacywebsite.dto.UserDto;
import com.example.pharmacywebsite.dto.UserFullnameDto;
import com.example.pharmacywebsite.exception.ApiException;
import com.example.pharmacywebsite.repository.CategoryRepository;
import com.example.pharmacywebsite.repository.MedicineDetailRepository;
import com.example.pharmacywebsite.repository.MedicineRepository;
import com.example.pharmacywebsite.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Service
public class MedicineService {

    @Autowired
    private MedicineRepository medicineRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private MedicineDetailRepository medicineDetailRepo;

    @Autowired
    private UserRepository userRepo;

    public List<MedicineDto> getAll(String keyword) {
        List<Medicine> medicines = (keyword != null && !keyword.isBlank())
                ? medicineRepo.findByNameContainingIgnoreCase(keyword)
                : medicineRepo.findAll();

        return medicines.stream().map(this::toDto).toList();
    }

    public MedicineDto getById(Integer id) {
        Medicine med = medicineRepo.findById(id)
                .orElseThrow(() -> new ApiException("Medicine not found", HttpStatus.NOT_FOUND));
        return toDto(med);
    }

    public MedicineDto create(MedicineDto dto) {
        Medicine med = toEntity(dto);
        Category category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new ApiException("Category not found", HttpStatus.NOT_FOUND));
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
                .orElseThrow(() -> new ApiException("Medicine not found", HttpStatus.NOT_FOUND));

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
                    .orElseThrow(() -> new ApiException("Category not found", HttpStatus.NOT_FOUND));
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

        med.setUpdatedAt(LocalDateTime.now());

        return toDto(medicineRepo.save(med));
    }

    public void delete(Integer id) {
        Medicine medicine = medicineRepo.findById(id)
                .orElseThrow(() -> new ApiException("Medicine not found", HttpStatus.NOT_FOUND));

        medicineRepo.delete(medicine); // <- Hibernate sẽ xử lý cascade và orphan
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

        if (m.getCreator() != null) {
            UserFullnameDto creatorDto = new UserFullnameDto();
            creatorDto.setFullName(m.getCreator().getFullName());
            dto.setCreator(creatorDto);
        } else {
            dto.setCreator(null);
        }

        if (m.getUpdater() != null) {
            UserFullnameDto updaterDto = new UserFullnameDto();
            updaterDto.setFullName(m.getUpdater().getFullName());
            dto.setUpdater(updaterDto);
        } else {
            dto.setUpdater(null);
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

    public String getNextImageFileName() {
        List<String> imageNames = medicineRepo.findAllProductImageNames();
        int max = 0;
        for (String name : imageNames) {
            try {
                int num = Integer.parseInt(name.replaceAll("\\D", ""));
                if (num > max)
                    max = num;
            } catch (NumberFormatException ignored) {
            }
        }
        return "product" + (max + 1) + ".jpg";
    }

    public void saveMedicine(
            Integer userId,
            String name, String unit, Double originalPrice, Double price,
            String brandOrigin,
            String manufacturer, String country, String shortDescription,
            Integer categoryId, String detailsJson, MultipartFile imageFile) throws IOException {
        String imageUrl = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = getNextImageFileName();
            Path path = Paths.get("src/main/resources/static/images/products/" + fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, imageFile.getBytes());
            imageUrl = fileName;
        }

        Medicine medicine = new Medicine();
        medicine.setName(name);
        medicine.setUnit(unit);
        medicine.setOriginalPrice(originalPrice);
        medicine.setPrice(price);
        medicine.setBrandOrigin(brandOrigin);
        medicine.setManufacturer(manufacturer);
        medicine.setCountryOfManufacture(country);
        medicine.setShortDescription(shortDescription);
        medicine.setImageUrl(imageUrl);

        Category category = categoryRepo.findById(categoryId).orElseThrow();
        medicine.setCategory(category);

        User creator = userRepo.findById(userId)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
        medicine.setCreator(creator);

        medicineRepo.save(medicine);

        ObjectMapper mapper = new ObjectMapper();
        List<MedicineDetail> details = mapper.readValue(detailsJson, new TypeReference<List<MedicineDetail>>() {
        });
        for (MedicineDetail d : details) {
            d.setMedicine(medicine);
        }

        medicineDetailRepo.saveAll(details);
    }

    public void updateMedicine(Integer id, Integer userId, String name, String unit,
            Double originalPrice, Double price,
            String brandOrigin, String manufacturer, String countryOfManufacture,
            String shortDescription, Integer categoryId, String detailsJson,
            MultipartFile image) throws IOException {

        Medicine med = medicineRepo.findById(id)
                .orElseThrow(() -> new ApiException("Không tìm thấy thuốc", HttpStatus.NOT_FOUND));

        med.setName(name);
        med.setUnit(unit);
        med.setOriginalPrice(originalPrice);
        med.setPrice(price);
        med.setBrandOrigin(brandOrigin);
        med.setManufacturer(manufacturer);
        med.setCountryOfManufacture(countryOfManufacture);
        med.setShortDescription(shortDescription);
        med.setUpdatedAt(LocalDateTime.now());

        // Set category
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ApiException("Không tìm thấy danh mục", HttpStatus.NOT_FOUND));
        med.setCategory(category);

        // Set details
        med.getDetails().clear();
        ObjectMapper objectMapper = new ObjectMapper();
        List<MedicineDetailDto> detailDtos = objectMapper.readValue(detailsJson,
                new TypeReference<List<MedicineDetailDto>>() {
                });
        List<MedicineDetail> details = detailDtos.stream().map(d -> {
            MedicineDetail detail = new MedicineDetail();
            detail.setType(d.getType());
            detail.setContent(d.getContent());
            detail.setMedicine(med);
            return detail;
        }).toList();
        med.getDetails().addAll(details);

        // Upload image if new image provided
        if (image != null && !image.isEmpty()) {
            String filename = getNextImageFileName(); // dùng hàm bạn đã viết
            Path path = Paths.get("src/main/resources/static/images/products");
            Files.createDirectories(path); // đảm bảo folder tồn tại
            Files.copy(image.getInputStream(), path.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            med.setImageUrl(filename);
        }

        // Optional: set updater
        if (userId != null) {
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new ApiException("User không tồn tại", HttpStatus.NOT_FOUND));
            med.setUpdater(user);
        }

        medicineRepo.save(med);
    }

}
