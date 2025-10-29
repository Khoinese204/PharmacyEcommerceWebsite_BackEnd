package com.example.pharmacywebsite.service;

import com.example.pharmacywebsite.domain.Review;
import com.example.pharmacywebsite.dto.ReviewSummaryResponse;
import com.example.pharmacywebsite.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewSummaryService {

    private final ReviewRepository reviewRepo;

    @Transactional(readOnly = true)
    public ReviewSummaryResponse getSummary(Integer medicineId) {
        List<Review> reviews = reviewRepo.findByMedicineId(medicineId);

        if (reviews.isEmpty()) {
            return ReviewSummaryResponse.builder()
                    .medicineId(medicineId)
                    .average(0.0)
                    .totalReviews(0L)
                    .stars(Map.of(5, 0L, 4, 0L, 3, 0L, 2, 0L, 1, 0L))
                    .build();
        }

        long total = reviews.size();
        double average = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        // Đếm số lượng review theo từng sao (1–5)
        Map<Integer, Long> starMap = reviews.stream()
                .collect(Collectors.groupingBy(Review::getRating, Collectors.counting()));

        // Đảm bảo luôn có đủ 5 keys
        for (int i = 1; i <= 5; i++) {
            starMap.putIfAbsent(i, 0L);
        }

        // Sắp xếp map theo thứ tự 5 → 1 (để FE vẽ biểu đồ sao dễ hơn)
        Map<Integer, Long> sortedStars = new LinkedHashMap<>();
        for (int i = 5; i >= 1; i--) {
            sortedStars.put(i, starMap.get(i));
        }

        return ReviewSummaryResponse.builder()
                .medicineId(medicineId)
                .average(Math.round(average * 10.0) / 10.0) // làm tròn 1 chữ số thập phân
                .totalReviews(total)
                .stars(sortedStars)
                .build();
    }
}
