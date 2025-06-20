package com.example.pharmacywebsite.service;

import com.example.pharmacywebsite.domain.Category;
import com.example.pharmacywebsite.dto.CategoryDto;
import com.example.pharmacywebsite.exception.ApiException;
import com.example.pharmacywebsite.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepo;

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

    public CategoryDto create(CategoryDto dto) {
        Category c = new Category();
        c.setName(dto.getName());
        return toDto(categoryRepo.save(c));
    }

    public CategoryDto update(Integer id, CategoryDto dto) {
        Category c = categoryRepo.findById(id)
                .orElseThrow(() -> new ApiException("Category not found with ID: " + id, HttpStatus.NOT_FOUND));
        c.setName(dto.getName());
        return toDto(categoryRepo.save(c));
    }

    public void delete(Integer id) {
        if (!categoryRepo.existsById(id)) {
            throw new ApiException("Category not found with ID: " + id, HttpStatus.NOT_FOUND);
        }
        categoryRepo.deleteById(id);
    }

    private CategoryDto toDto(Category c) {
        return new CategoryDto(c.getId(), c.getName());
    }
}
