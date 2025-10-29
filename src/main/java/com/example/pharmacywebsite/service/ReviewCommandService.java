package com.example.pharmacywebsite.service;

import com.example.pharmacywebsite.domain.*;
import com.example.pharmacywebsite.dto.CommentCreateDto;
import com.example.pharmacywebsite.dto.ReviewCreateDto;
import com.example.pharmacywebsite.dto.ReviewResponseDto;
import com.example.pharmacywebsite.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewCommandService {

    private final ReviewRepository reviewRepo;
    private final CommentRepository commentRepo;
    private final MedicineRepository medicineRepo;
    private final UserRepository userRepo;

    /* ==================== 1) Tạo review ==================== */
    @Transactional
    public ReviewResponseDto createReview(Integer medicineId, ReviewCreateDto dto) {
        if (dto.getUserId() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId is required");
        if (dto.getRating() == null || dto.getRating() < 1 || dto.getRating() > 5)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "rating must be between 1 and 5");

        Medicine medicine = medicineRepo.findById(medicineId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Medicine not found"));
        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Review review = new Review();
        review.setMedicine(medicine);
        review.setUser(user);
        review.setRating(dto.getRating());
        review.setReviewText(dto.getReviewText());

        review = reviewRepo.save(review);

        return toDto(review, user);
    }

    /*
     * ==================== 2) Phản hồi review (CUSTOMER / PHARMACIST / ADMIN)
     * ====================
     */
    @Transactional
    public ReviewResponseDto replyReview(Integer medicineId, Integer reviewId, CommentCreateDto dto) {
        if (dto.getUserId() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId is required");
        if (dto.getCommentText() == null || dto.getCommentText().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "commentText is required");

        User replier = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Kiểm tra role hợp lệ
        validateReplyRole(replier);

        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));
        if (!Objects.equals(review.getMedicine().getId(), medicineId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Review does not belong to this medicine");
        }

        // Tạo comment
        Comment comment = new Comment();
        comment.setReview(review);
        comment.setUser(replier);
        comment.setCommentText(dto.getCommentText());
        commentRepo.save(comment);

        // Trả về review (kèm comments sau khi thêm)
        return toDto(review, replier);
    }

    /* ==================== Helpers ==================== */

    private void validateReplyRole(User user) {
        String roleName = (user.getRole() != null ? user.getRole().getName() : null);
        if (roleName == null)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Role not assigned");

        boolean allowed = roleName.equalsIgnoreCase("CUSTOMER")
                || roleName.equalsIgnoreCase("PHARMACIST")
                || roleName.equalsIgnoreCase("ADMIN");

        if (!allowed)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to reply.");
    }

    private boolean isCustomer(User user) {
        String role = user.getRole() != null ? user.getRole().getName() : null;
        return role != null && role.equalsIgnoreCase("CUSTOMER");
    }

    private boolean isPharmacist(User user) {
        String role = user.getRole() != null ? user.getRole().getName() : null;
        return role != null && role.equalsIgnoreCase("PHARMACIST");
    }

    private boolean isAdmin(User user) {
        String role = user.getRole() != null ? user.getRole().getName() : null;
        return role != null && role.equalsIgnoreCase("ADMIN");
    }

    private ReviewResponseDto toDto(Review review, User actor) {
        boolean canReply = actor != null && (isCustomer(actor) || isPharmacist(actor) || isAdmin(actor));

        return ReviewResponseDto.builder()
                .reviewId(review.getReviewId())
                .userId(review.getUser().getId())
                .medicineId(review.getMedicine().getId())
                .rating(review.getRating())
                .reviewText(review.getReviewText())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .comments(review.getComments().stream()
                        .map(c -> ReviewResponseDto.CommentDto.builder()
                                .id(c.getCommentId())
                                .reviewId(review.getReviewId())
                                .userId(c.getUser().getId())
                                .userRole(c.getUser().getRole() != null ? c.getUser().getRole().getName() : null)
                                .commentText(c.getCommentText())
                                .createdAt(c.getCreatedAt())
                                .build())
                        .collect(Collectors.toList()))
                .capabilities(ReviewResponseDto.Capabilities.builder()
                        .canReply(canReply)
                        .canEdit(false) // ❌ Không ai được sửa
                        .canDelete(false) // ❌ Không ai được xóa
                        .build())
                .build();
    }
}
