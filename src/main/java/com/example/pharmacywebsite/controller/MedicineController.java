package com.example.pharmacywebsite.controller;

import com.example.pharmacywebsite.dto.MedicineDto;
import com.example.pharmacywebsite.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/medicines")
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

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
}
