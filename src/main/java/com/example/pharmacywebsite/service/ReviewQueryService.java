package com.example.pharmacywebsite.service;

import com.example.pharmacywebsite.domain.Review;
import com.example.pharmacywebsite.domain.User;
import com.example.pharmacywebsite.dto.ReviewListResponse;
import com.example.pharmacywebsite.repository.ReviewRepository;
import com.example.pharmacywebsite.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewQueryService {

    private final ReviewRepository reviewRepo;
    private final UserRepository userRepo;

    @Transactional(readOnly = true)
    public ReviewListResponse listAll(Integer medicineId, String star, Integer viewerId) {
        List<Review> reviews;
        if (star == null || star.equalsIgnoreCase("all")) {
            reviews = reviewRepo.findByMedicineIdFetchAll(medicineId);
        } else {
            int rating = Integer.parseInt(star);
            reviews = reviewRepo.findByMedicineIdAndRatingFetchAll(medicineId, rating);
        }

        reviews.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));

        User viewer = null;
        if (viewerId != null) {
            viewer = userRepo.findById(viewerId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Viewer not found"));
        }

        // ✅ Fix: gán sang final biến mới
        final User finalViewer = viewer;

        var items = reviews.stream().map(r -> {
            boolean canReply = false;
            if (finalViewer != null && finalViewer.getRole() != null) {
                String roleName = finalViewer.getRole().getName().toUpperCase();
                canReply = roleName.equals("CUSTOMER") || roleName.equals("PHARMACIST") || roleName.equals("ADMIN");
            }

            var comments = r.getComments().stream().map(c -> ReviewListResponse.CommentDto.builder()
                    .id(c.getCommentId())
                    .reviewId(r.getReviewId())
                    .userId(c.getUser().getId())
                    .userRole(c.getUser().getRole() != null ? c.getUser().getRole().getName() : null)
                    .commentText(c.getCommentText())
                    .createdAt(c.getCreatedAt())
                    .build()).collect(Collectors.toList());

            return ReviewListResponse.Item.builder()
                    .reviewId(r.getReviewId())
                    .userId(r.getUser().getId())
                    .medicineId(r.getMedicine().getId())
                    .rating(r.getRating())
                    .reviewText(r.getReviewText())
                    .createdAt(r.getCreatedAt())
                    .updatedAt(r.getUpdatedAt())
                    .comments(comments)
                    .capabilities(ReviewListResponse.Capabilities.builder()
                            .canReply(canReply)
                            .canEdit(false)
                            .canDelete(false)
                            .build())
                    .build();
        }).collect(Collectors.toList());

        return ReviewListResponse.builder()
                .items(items)
                .build();
    }

}
