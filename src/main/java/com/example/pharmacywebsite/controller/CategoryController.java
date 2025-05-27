package com.example.pharmacywebsite.controller;

import com.example.pharmacywebsite.dto.CategoryDto;
import com.example.pharmacywebsite.domain.Category;
import com.example.pharmacywebsite.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepo;

    @GetMapping
    public List<CategoryDto> getAll(@RequestParam(name = "name", required = false) String search) {
        return categoryRepo.findAll().stream()
                .filter(c -> search == null || c.getName().toLowerCase().contains(search.toLowerCase()))
                .map(c -> new CategoryDto(c.getId(), c.getName()))
                .toList();
    }

    @PostMapping
    public Category create(@RequestBody CategoryDto dto) {
        Category c = new Category();
        c.setName(dto.getName());
        return categoryRepo.save(c);
    }

    @PutMapping("/{id}")
    public Category update(@PathVariable Integer id, @RequestBody CategoryDto dto) {
        Category c = categoryRepo.findById(id).orElseThrow();
        c.setName(dto.getName());
        return categoryRepo.save(c);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        categoryRepo.deleteById(id);
    }
}
