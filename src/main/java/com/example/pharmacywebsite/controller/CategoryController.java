package com.example.pharmacywebsite.controller;

import com.example.pharmacywebsite.dto.CategoryDto;
import com.example.pharmacywebsite.dto.CategoryRequest; // ✅ Import
import com.example.pharmacywebsite.service.CategoryService;
import com.example.pharmacywebsite.service.FileStorageService; // ✅ Import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile; // ✅ Import

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FileStorageService fileStorageService; 

    @GetMapping
    public List<CategoryDto> getAll(@RequestParam(name = "name", required = false) String search) {
        return categoryService.getAll(search);
    }

    @GetMapping("/{id}")
    public CategoryDto getById(@PathVariable Integer id) {
        return categoryService.getById(id);
    }

    @PostMapping
    public CategoryDto create(@ModelAttribute CategoryRequest request,
                              @RequestParam("file") MultipartFile file) {
        String filename = fileStorageService.store(file);
        return categoryService.create(request, filename);
    }

    // ✅ SỬA LỖI Ở ĐÂY
    @PutMapping("/{id}")
    public CategoryDto update(@PathVariable("id") Integer id, // <-- Thêm ("id") vào đây
                              @ModelAttribute CategoryRequest request,
                              @RequestParam(value = "file", required = false) MultipartFile file) {
        
        String filename = null;
        if (file != null && !file.isEmpty()) {
            CategoryDto oldCategory = categoryService.getById(id);
            fileStorageService.delete(oldCategory.getImageUrl());
            filename = fileStorageService.store(file);
        }
        
        return categoryService.update(id, request, filename);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Integer id) {
        categoryService.delete(id);
        return "Category deleted successfully";
    }
}