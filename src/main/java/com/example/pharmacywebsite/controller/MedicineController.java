package com.example.pharmacywebsite.controller;

import com.example.pharmacywebsite.dto.BestSellingProductDto;
import com.example.pharmacywebsite.dto.MedicineDto;
import com.example.pharmacywebsite.repository.MedicineRepository;
import com.example.pharmacywebsite.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/medicines")
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    @Autowired
    private MedicineRepository medicineRepository;

    @GetMapping
    public List<MedicineDto> getAll(@RequestParam(name = "name", required = false) String search) {
        return medicineService.getAll(search);
    }

    @GetMapping("/{id}")
    public MedicineDto getById(@PathVariable(name = "id") Integer id) {
        return medicineService.getById(id);
    }

    @PostMapping
    public MedicineDto create(@RequestBody MedicineDto dto) {
        return medicineService.create(dto);
    }

    @PutMapping("/{id}")
    public MedicineDto update(@PathVariable(name = "id") Integer id, @RequestBody MedicineDto dto) {
        return medicineService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Integer id) {
        medicineService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Medicine deleted successfully"));
    }

    @GetMapping("/best-selling")
    public ResponseEntity<?> getTopBestSellingProducts() {
        List<Object[]> rawData = medicineRepository.findTop8BestSellingRaw();

        List<BestSellingProductDto> topProducts = rawData.stream()
                .map(obj -> new BestSellingProductDto(
                        (Integer) obj[0],
                        (String) obj[1],
                        (String) obj[2],
                        obj[3] != null ? ((Number) obj[3]).doubleValue() : 0.0,
                        obj[4] != null ? ((Number) obj[4]).doubleValue() : 0.0,
                        (String) obj[5]))
                .collect(Collectors.toList());

        return ResponseEntity.ok(topProducts);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(
            @RequestParam(name = "userId") Integer userId,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "unit") String unit,
            @RequestParam(name = "originalPrice") Double originalPrice,
            @RequestParam(name = "price") Double price,
            @RequestParam(name = "categoryId") Integer categoryId,
            @RequestParam(name = "shortDescription") String shortDescription,
            @RequestParam(name = "brandOrigin") String brandOrigin,
            @RequestParam(name = "manufacturer") String manufacturer,
            @RequestParam(name = "countryOfManufacture") String countryOfManufacture,
            @RequestParam(name = "details") String details,
            @RequestParam(name = "image", required = false) MultipartFile image) {
        try {
            medicineService.saveMedicine(
                    userId, name, unit, originalPrice, price,
                    brandOrigin,
                    manufacturer, countryOfManufacture, shortDescription,
                    categoryId, details, image);
            return ResponseEntity.ok("Thêm thuốc thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/upload")
    public ResponseEntity<?> updateMedicine(
            @PathVariable(name = "id") Integer id,
            @RequestParam(name = "userId") Integer userId,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "unit") String unit,
            @RequestParam(name = "originalPrice") Double originalPrice,
            @RequestParam(name = "price") Double price,
            @RequestParam(name = "categoryId") Integer categoryId,
            @RequestParam(name = "shortDescription") String shortDescription,
            @RequestParam(name = "brandOrigin") String brandOrigin,
            @RequestParam(name = "manufacturer") String manufacturer,
            @RequestParam(name = "countryOfManufacture") String countryOfManufacture,
            @RequestParam(name = "details") String details, // JSON string
            @RequestParam(name = "image", required = false) MultipartFile image) {

        try {
            medicineService.updateMedicine(id, userId, name, unit, originalPrice, price,
                    brandOrigin, manufacturer, countryOfManufacture, shortDescription,
                    categoryId, details, image);
            return ResponseEntity.ok("Cập nhật thuốc thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi: " + e.getMessage());
        }
    }

    

}
