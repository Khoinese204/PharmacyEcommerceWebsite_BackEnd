package com.example.pharmacywebsite.helper;

import com.example.pharmacywebsite.domain.Comment;
import com.example.pharmacywebsite.domain.Review;
import com.example.pharmacywebsite.domain.User;
import com.example.pharmacywebsite.dto.ReviewListResponse;
import com.example.pharmacywebsite.dto.ReviewResponseDto;
import com.example.pharmacywebsite.repository.CommentRepository;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Mapper dựng cây comment từ danh sách phẳng (có parent_comment_id).
 * - buildTreeForList: dùng cho ReviewListResponse (list nhiều review)
 * - toReviewResponseTree: dùng cho ReviewResponseDto (1 review sau
 * create/reply)
 */
public final class ReviewTreeMapper {

    private ReviewTreeMapper() {
    }

    /*
     * =========================================================
     * ================ LIST RESPONSE (nhiều review) ==========
     * =========================================================
     */

    /** Dựng cây CommentDto (ReviewListResponse) từ danh sách comment phẳng */
    public static List<ReviewListResponse.CommentDto> buildTreeForList(List<Comment> flat) {
        // Map id -> node DTO
        Map<Integer, ReviewListResponse.CommentDto> map = new LinkedHashMap<>();
        List<ReviewListResponse.CommentDto> roots = new ArrayList<>();

        for (Comment c : flat) {
            map.put(c.getCommentId(), toListDto(c));
        }

        for (Comment c : flat) {
            Integer pid = (c.getParent() != null) ? c.getParent().getCommentId() : null;
            ReviewListResponse.CommentDto node = map.get(c.getCommentId());
            if (pid == null) {
                roots.add(node);
            } else {
                ReviewListResponse.CommentDto parent = map.get(pid);
                if (parent != null) {
                    parent.getReplies().add(node);
                } else {
                    // fallback nếu parent không load được
                    roots.add(node);
                }
            }
        }

        // sort theo thời gian tăng dần trong từng cấp
        roots.sort(BY_TIME_LIST);
        roots.forEach(ReviewTreeMapper::sortChildrenList);

        return roots;
    }

    private static ReviewListResponse.CommentDto toListDto(Comment c) {
        User u = c.getUser();
        String role = (u != null && u.getRole() != null) ? safe(u.getRole().getName()) : null;

        return ReviewListResponse.CommentDto.builder()
                .id(c.getCommentId())
                .reviewId(c.getReview() != null ? c.getReview().getReviewId() : null)
                .userId(u != null ? u.getId() : null)
                .userName(u != null ? safe(u.getFullName()) : null)
                .userRole(role)
                .commentText(safe(c.getCommentText()))
                .createdAt(safeTime(c.getCreatedAt()))
                .parentCommentId(c.getParent() != null ? c.getParent().getCommentId() : null)
                .replies(new ArrayList<>())
                .build();
    }

    private static final Comparator<ReviewListResponse.CommentDto> BY_TIME_LIST = Comparator
            .comparing(ReviewListResponse.CommentDto::getCreatedAt, Comparator.nullsFirst(LocalDateTime::compareTo));

    private static void sortChildrenList(ReviewListResponse.CommentDto parent) {
        if (parent.getReplies() == null)
            return;
        parent.getReplies().sort(BY_TIME_LIST);
        parent.getReplies().forEach(ReviewTreeMapper::sortChildrenList);
    }

    /*
     * =========================================================
     * ================ SINGLE RESPONSE (1 review) =============
     * =========================================================
     */

    /**
     * Trả về ReviewResponseDto (bao gồm cây comments) cho 1 review.
     * Tự fetch toàn bộ comment của review từ repository.
     */
    public static ReviewResponseDto toReviewResponseTree(Review r, CommentRepository commentRepo) {
        List<Comment> flat = commentRepo.findAllByReviewIdWithUser(r.getReviewId());

        // build tree cho CommentDto (ReviewResponseDto)
        Map<Integer, ReviewResponseDto.CommentDto> map = new LinkedHashMap<>();
        List<ReviewResponseDto.CommentDto> roots = new ArrayList<>();

        for (Comment c : flat) {
            map.put(c.getCommentId(), toResponseDtoNode(r, c));
        }

        for (Comment c : flat) {
            Integer pid = (c.getParent() != null) ? c.getParent().getCommentId() : null;
            ReviewResponseDto.CommentDto node = map.get(c.getCommentId());
            if (pid == null) {
                roots.add(node);
            } else {
                ReviewResponseDto.CommentDto parent = map.get(pid);
                if (parent != null) {
                    parent.getReplies().add(node);
                } else {
                    // fallback nếu parent không load được
                    roots.add(node);
                }
            }
        }

        // sort theo thời gian tăng dần trong từng cấp
        roots.sort(BY_TIME_RESP);
        roots.forEach(ReviewTreeMapper::sortChildrenResp);

        // build ReviewResponseDto
        return ReviewResponseDto.builder()
                .reviewId(safeId(r.getReviewId()))
                .userId(r.getUser() != null ? r.getUser().getId() : null)
                .userName(r.getUser() != null ? safe(r.getUser().getFullName()) : null)
                .userRole(r.getUser().getRole() != null ? r.getUser().getRole().getName() : null)
                .medicineId(r.getMedicine() != null ? r.getMedicine().getId() : null)
                .rating(r.getRating())
                .reviewText(safe(r.getReviewText()))
                .createdAt(safeTime(r.getCreatedAt()))
                .updatedAt(safeTime(r.getUpdatedAt()))
                .comments(roots)
                .capabilities(ReviewResponseDto.Capabilities.builder()
                        .canReply(false) // nếu cần theo viewer thì set ở chỗ gọi
                        .canEdit(false)
                        .canDelete(false)
                        .build())
                .build();
    }

    private static ReviewResponseDto.CommentDto toResponseDtoNode(Review r, Comment c) {
        User u = c.getUser();
        String role = (u != null && u.getRole() != null) ? safe(u.getRole().getName()) : null;

        return ReviewResponseDto.CommentDto.builder()
                .id(c.getCommentId())
                .reviewId(safeId(r.getReviewId()))
                .userId(u != null ? u.getId() : null)
                .userName(u != null ? safe(u.getFullName()) : null)
                .userRole(role)
                .commentText(safe(c.getCommentText()))
                .createdAt(safeTime(c.getCreatedAt()))
                .parentCommentId(c.getParent() != null ? c.getParent().getCommentId() : null)
                .replies(new ArrayList<>())
                .build();
    }

    private static final Comparator<ReviewResponseDto.CommentDto> BY_TIME_RESP = Comparator
            .comparing(ReviewResponseDto.CommentDto::getCreatedAt, Comparator.nullsFirst(LocalDateTime::compareTo));

    private static void sortChildrenResp(ReviewResponseDto.CommentDto parent) {
        if (parent.getReplies() == null)
            return;
        parent.getReplies().sort(BY_TIME_RESP);
        parent.getReplies().forEach(ReviewTreeMapper::sortChildrenResp);
    }

    /*
     * =========================================================
     * ===================== Helpers =========================
     * =========================================================
     */

    private static String safe(String s) {
        return (s == null ? null : s);
    }

    private static LocalDateTime safeTime(LocalDateTime t) {
        return (t == null ? null : t);
    }

    private static Integer safeId(Integer i) {
        return (i == null ? null : i);
    }
}
