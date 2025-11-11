package com.example.pharmacywebsite.service;

import com.example.pharmacywebsite.domain.*;
import com.example.pharmacywebsite.dto.CommentCreateDto;
import com.example.pharmacywebsite.dto.ReviewCreateDto;
import com.example.pharmacywebsite.dto.ReviewListResponse;
import com.example.pharmacywebsite.dto.ReviewResponseDto;
import com.example.pharmacywebsite.dto.ReviewSummaryResponse;
import com.example.pharmacywebsite.helper.ReviewTreeMapper;
import com.example.pharmacywebsite.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

import java.util.*;

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
        // ===== Validate input =====
        if (dto.getUserId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId is required");
        }
        if (dto.getRating() == null || dto.getRating() < 1 || dto.getRating() > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "rating must be in [1..5]");
        }
        // reviewText có thể cho phép rỗng; nếu muốn bắt buộc thì bật check dưới:
        // if (dto.getReviewText() == null || dto.getReviewText().isBlank()) {
        // throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "reviewText is
        // required");
        // }

        // ===== Load relations =====
        Medicine medicine = medicineRepo.findById(medicineId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MEDICINE_NOT_FOUND"));

        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "USER_NOT_FOUND"));

        // ===== Persist review =====
        Review r = new Review();
        r.setMedicine(medicine);
        r.setUser(user);
        r.setRating(dto.getRating());
        r.setReviewText(dto.getReviewText());
        r = reviewRepo.save(r);

        // ===== Build response (comments rỗng, capabilities theo role của actor) =====
        boolean canReply = true; // CUSTOMER/PHARMACIST/ADMIN => true
        // Theo yêu cầu của bạn: KHÔNG cho sửa/xoá review
        boolean canEdit = false;
        boolean canDelete = false;

        return ReviewResponseDto.builder()
                .reviewId(r.getReviewId())
                .userId(user.getId())
                .userName(user.getFullName()) // nếu DTO có field này
                .userRole(user.getRole() != null ? user.getRole().getName() : null)
                .medicineId(medicine.getId())
                .rating(r.getRating())
                .reviewText(r.getReviewText())
                .createdAt(r.getCreatedAt())
                .updatedAt(r.getUpdatedAt())
                .comments(java.util.Collections.emptyList())
                .capabilities(ReviewResponseDto.Capabilities.builder()
                        .canReply(canReply)
                        .canEdit(canEdit)
                        .canDelete(canDelete)
                        .build())
                .build();
    }

    /** CUSTOMER / PHARMACIST / ADMIN => được reply */
    private boolean canReplyByRole(User u) {
        if (u == null || u.getRole() == null || u.getRole().getName() == null)
            return false;
        String role = u.getRole().getName().trim().toUpperCase();
        return role.equals("CUSTOMER") || role.equals("PHARMACIST") || role.equals("ADMIN");
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

        // Ai cũng được trả lời: CUSTOMER/PHARMACIST/ADMIN (như rule bạn đã đặt)
        User actor = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "USER_NOT_FOUND"));

        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "REVIEW_NOT_FOUND"));
        if (!Objects.equals(review.getMedicine().getId(), medicineId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "REVIEW_NOT_OF_MEDICINE");
        }

        Comment c = new Comment();
        c.setReview(review);
        c.setUser(actor);
        c.setCommentText(dto.getCommentText());

        // ✅ nếu reply vào một comment khác
        if (dto.getParentCommentId() != null) {
            Comment parent = commentRepo.findById(dto.getParentCommentId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PARENT_COMMENT_NOT_FOUND"));
            if (!Objects.equals(parent.getReview().getReviewId(), reviewId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "PARENT_NOT_IN_THIS_REVIEW");
            }
            c.setParent(parent);
        }

        commentRepo.save(c);

        // trả về review sau khi thêm comment, với comments dạng cây
        return ReviewTreeMapper.toReviewResponseTree(review, commentRepo);
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
                        .canReply(true)
                        .canEdit(false) // ❌ Không ai được sửa
                        .canDelete(false) // ❌ Không ai được xóa
                        .build())
                .build();
    }

    @Transactional(readOnly = true)
    public ReviewListResponse listAll(Integer medicineId, String star, Integer viewerId) {

        // 1) Lấy danh sách review theo filter sao
        final List<Review> reviews;
        if (star == null || "all".equalsIgnoreCase(star)) {
            reviews = reviewRepo.findByMedicineIdFetchAll(medicineId);
        } else {
            int rating;
            try {
                rating = Integer.parseInt(star);
            } catch (NumberFormatException ex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "star must be all|1|2|3|4|5");
            }
            if (rating < 1 || rating > 5) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "star must be in [1..5] or 'all'");
            }
            reviews = reviewRepo.findByMedicineIdAndRatingFetchAll(medicineId, rating);
        }

        // 2) Luôn sort mới nhất trước
        reviews.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));

        // 3) Tính quyền reply theo viewer
        String viewerRole = null;
        if (viewerId != null) {
            var viewer = userRepo.findById(viewerId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "VIEWER_NOT_FOUND"));
            viewerRole = viewer.getRole() != null ? viewer.getRole().getName() : null;
        }
        final boolean viewerCanReply = canReplyByRole(viewerRole);

        // 4) Map sang DTO (comments ở dạng cây)
        var items = reviews.stream().map(r -> {
            // lấy toàn bộ comments của review -> build tree
            var flat = commentRepo.findAllByReviewIdWithUser(r.getReviewId());
            var tree = ReviewTreeMapper.buildTreeForList(flat);

            return ReviewListResponse.Item.builder()
                    .reviewId(r.getReviewId())
                    .userId(r.getUser() != null ? r.getUser().getId() : null)
                    .userName(r.getUser() != null ? r.getUser().getFullName() : null)
                    .userRole(r.getUser() != null && r.getUser().getRole() != null ? r.getUser().getRole().getName()
                            : null)
                    .medicineId(r.getMedicine() != null ? r.getMedicine().getId() : null)
                    .rating(r.getRating())
                    .reviewText(r.getReviewText())
                    .createdAt(r.getCreatedAt())
                    .updatedAt(r.getUpdatedAt())
                    .comments(tree)
                    .capabilities(ReviewListResponse.Capabilities.builder()
                            .canReply(true)
                            .canEdit(false) // theo yêu cầu: khách hàng không được sửa/xoá review
                            .canDelete(false)
                            .build())
                    .build();
        }).collect(Collectors.toList());

        return ReviewListResponse.builder().items(items).build();
    }

    /** CUSTOMER / PHARMACIST / ADMIN => được phép reply */
    private boolean canReplyByRole(String roleName) {
        if (roleName == null)
            return false;
        String r = roleName.trim().toUpperCase(Locale.ROOT);
        return r.equals("CUSTOMER") || r.equals("PHARMACIST") || r.equals("ADMIN");
    }

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
