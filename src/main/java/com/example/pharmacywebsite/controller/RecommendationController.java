package com.example.pharmacywebsite.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.example.pharmacywebsite.dto.RecommendationResponse;
import com.example.pharmacywebsite.service.RecommendationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class RecommendationController {

    private final RecommendationService service;

    @GetMapping("/{id}/similar")
    public RecommendationResponse similar(
            @PathVariable(name = "id") Integer id,
            @RequestParam(name = "limit", defaultValue = "6") int limit) {
        return service.similar(id, limit);
    }

    @GetMapping("/{id}/cross-sell")
    public RecommendationResponse crossSell(
            @PathVariable(name = "id") Integer id,
            @RequestParam(name = "limit", defaultValue = "6") int limit) {
        return service.crossSell(id, limit);
    }
}