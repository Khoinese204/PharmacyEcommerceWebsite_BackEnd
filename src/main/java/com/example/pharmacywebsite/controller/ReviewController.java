package com.example.pharmacywebsite.controller;

import com.example.pharmacywebsite.dto.CommentCreateDto;
import com.example.pharmacywebsite.dto.ReviewCreateDto;
import com.example.pharmacywebsite.dto.ReviewListResponse;
import com.example.pharmacywebsite.dto.ReviewResponseDto;
import com.example.pharmacywebsite.dto.ReviewSummaryResponse;
import com.example.pharmacywebsite.service.ReviewCommandService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medicines/{medicineId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewCommandService commandService;

    /** ✅ 1) Tạo review (FE gửi userId, rating, reviewText) */
    @PostMapping
    public ResponseEntity<ReviewResponseDto> createReview(
            @PathVariable("medicineId") Integer medicineId,
            @RequestBody ReviewCreateDto body) {
        ReviewResponseDto dto = commandService.createReview(medicineId, body);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    /** ✅ 2) Phản hồi review (CUSTOMER / PHARMACIST / ADMIN) */
    @PostMapping("/{reviewId}/comments")
    public ResponseEntity<ReviewResponseDto> replyReview(
            @PathVariable("medicineId") Integer medicineId,
            @PathVariable("reviewId") Integer reviewId,
            @RequestBody CommentCreateDto body) {
        ReviewResponseDto dto = commandService.replyReview(medicineId, reviewId, body);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    /** ✅ 3) Danh sách review (lọc theo sao, luôn sort mới nhất) */
    @GetMapping("/all")
    public ResponseEntity<ReviewListResponse> listAll(
            @PathVariable("medicineId") Integer medicineId,
            @RequestParam(value = "star", defaultValue = "all") String star,
            @RequestParam(value = "viewerId", required = false) Integer viewerId) {
        ReviewListResponse dto = commandService.listAll(medicineId, star, viewerId);
        return ResponseEntity.ok(dto);
    }

    /** ✅ 4) Summary: tổng hợp đánh giá sản phẩm */

    @GetMapping("/summary")
    public ResponseEntity<ReviewSummaryResponse> getReviewSummary(
            @PathVariable("medicineId") Integer medicineId) {
        ReviewSummaryResponse dto = commandService.getSummary(medicineId);
        return ResponseEntity.ok(dto);
    }

}
