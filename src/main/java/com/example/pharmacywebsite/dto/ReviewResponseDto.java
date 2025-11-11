// dto/review/ReviewResponseDto.java
package com.example.pharmacywebsite.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class ReviewResponseDto {
    private Integer reviewId;
    private Integer userId;
    private String userName; // ✅ thêm để hiển thị tên người review
    private Integer medicineId;

    private Integer rating;
    private String reviewText;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<CommentDto> comments;
    private Capabilities capabilities;

    @Getter
    @Setter
    @Builder
    public static class CommentDto {
        private Integer id;
        private Integer reviewId;
        private Integer userId;
        private String userName; // ✅ mới
        private String userRole; // "PHARMACIST"/"ADMIN"/...
        private String commentText;
        private LocalDateTime createdAt;

        private Integer parentCommentId; // ✅ mới
        private List<CommentDto> replies; // ✅ mới (đệ quy)
    }

    @Getter
    @Setter
    @Builder
    public static class Capabilities {
        private boolean canReply; // pharmacist/admin/customer
        private boolean canEdit; // owner
        private boolean canDelete; // owner
    }
}
