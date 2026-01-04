package com.example.pharmacywebsite.service;

import com.example.pharmacywebsite.domain.Medicine;
import com.example.pharmacywebsite.dto.RecommendationItemDto;
import com.example.pharmacywebsite.dto.RecommendationResponse;
import com.example.pharmacywebsite.repository.MedicineRepository;
import com.example.pharmacywebsite.repository.ProductCrossSellRuleRepository;
import com.example.pharmacywebsite.repository.SimilarNativeRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final SimilarNativeRepository similarNativeRepo;
    private final ProductCrossSellRuleRepository crossSellRuleRepo;
    private final MedicineRepository medicineRepo;

    public RecommendationResponse similar(Integer productId, int limit) {
        List<Object[]> rows = similarNativeRepo.findSimilarByTrigram(productId, limit);

        if (rows.isEmpty()) {
            return new RecommendationResponse(productId, "SIMILAR", List.of());
        }

        List<Integer> ids = rows.stream()
                .map(r -> ((Number) r[0]).intValue())
                .toList();

        Map<Integer, Medicine> map = medicineRepo.findAllById(ids).stream()
                .collect(Collectors.toMap(Medicine::getId, Function.identity()));

        List<RecommendationItemDto> items = new ArrayList<>();
        for (Object[] r : rows) {
            Integer id = ((Number) r[0]).intValue();
            Double score = r[1] == null ? 0.0 : ((Number) r[1]).doubleValue();
            Medicine m = map.get(id);
            if (m == null)
                continue;

            items.add(new RecommendationItemDto(
                    m.getId(),
                    m.getName(),
                    m.getImageUrl(),
                    m.getPrice(),
                    round3(score),
                    null));
        }

        return new RecommendationResponse(productId, "SIMILAR", items);
    }

    public RecommendationResponse crossSell(Integer productId, int limit) {
        var rules = crossSellRuleRepo.findByProductId(productId, PageRequest.of(0, limit));

        if (rules == null || rules.isEmpty()) {
            return new RecommendationResponse(productId, "CROSS_SELL", List.of());
        }

        List<Integer> ids = rules.stream().map(r -> r.getCrossSellId()).toList();

        Map<Integer, Medicine> medicineMap = medicineRepo.findAllById(ids).stream()
                .collect(Collectors.toMap(Medicine::getId, Function.identity()));

        List<RecommendationItemDto> items = new ArrayList<>();
        for (var r : rules) {
            Medicine m = medicineMap.get(r.getCrossSellId());
            if (m == null)
                continue;

            items.add(new RecommendationItemDto(
                    m.getId(),
                    m.getName(),
                    m.getImageUrl(),
                    m.getPrice(),
                    1.0, // cross-sell không cần similarity score, để 1.0 cho dễ demo
                    r.getReason()));
        }

        return new RecommendationResponse(productId, "CROSS_SELL", items);
    }

    private static double round3(double v) {
        return Math.round(v * 1000.0) / 1000.0;
    }
}