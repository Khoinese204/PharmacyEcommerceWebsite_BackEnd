package com.example.pharmacywebsite.service;

import com.example.pharmacywebsite.domain.Category;
import com.example.pharmacywebsite.dto.CategoryDto;
import com.example.pharmacywebsite.dto.CategoryRequest; // ✅ Import
import com.example.pharmacywebsite.exception.ApiException;
import com.example.pharmacywebsite.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // ✅ Import

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private FileStorageService fileStorageService; // ✅ Tiêm FileStorageService

    // Hàm toDto giờ map cả 4 trường
    private CategoryDto toDto(Category c) {
        return new CategoryDto(c.getId(), c.getName(), c.getImageUrl(), c.getSlug());
    }

    public List<CategoryDto> getAll(String search) {
        return categoryRepo.findAll().stream()
                .filter(c -> search == null || c.getName().toLowerCase().contains(search.toLowerCase()))
                .map(this::toDto)
                .toList();
    }

    public CategoryDto getById(Integer id) {
        Category c = categoryRepo.findById(id)
                .orElseThrow(() -> new ApiException("Category not found with ID: " + id, HttpStatus.NOT_FOUND));
        return toDto(c);
    }

    // ✅ Cập nhật hàm Create
    @Transactional
    public CategoryDto create(CategoryRequest request, String imageUrl) {
        Category c = new Category();
        c.setName(request.getName());
        c.setSlug(request.getSlug());
        c.setImageUrl(imageUrl); // Lưu tên file
        return toDto(categoryRepo.save(c));
    }

    // ✅ Cập nhật hàm Update
    @Transactional
    public CategoryDto update(Integer id, CategoryRequest request, String imageUrl) {
        Category c = categoryRepo.findById(id)
                .orElseThrow(() -> new ApiException("Category not found with ID: " + id, HttpStatus.NOT_FOUND));
        
        c.setName(request.getName());
        c.setSlug(request.getSlug());
        
        // Chỉ cập nhật ảnh nếu có file mới được tải lên
        if (imageUrl != null) {
            c.setImageUrl(imageUrl);
        }
        return toDto(categoryRepo.save(c));
    }

    // ✅ Cập nhật hàm Delete (để xoá cả file ảnh)
    @Transactional
    public void delete(Integer id) {
        Category c = categoryRepo.findById(id)
                .orElseThrow(() -> new ApiException("Category not found with ID: " + id, HttpStatus.NOT_FOUND));
        
        String imageUrl = c.getImageUrl();
        categoryRepo.delete(c); // Xoá trong DB
        fileStorageService.delete(imageUrl); // Xoá file trên server
    }
}