package com.example.pharmacywebsite.controller;

import com.example.pharmacywebsite.dto.BestSellingProductDto;
import com.example.pharmacywebsite.dto.MedicineDto;
import com.example.pharmacywebsite.repository.MedicineRepository;
import com.example.pharmacywebsite.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
