package com.example.pharmacywebsite.dto;

import lombok.*;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewSummaryResponse {
    private Integer medicineId;
    private Double average;
    private Long totalReviews;
    private Map<Integer, Long> stars; // key = số sao (1–5), value = số lượt đánh giá
}
