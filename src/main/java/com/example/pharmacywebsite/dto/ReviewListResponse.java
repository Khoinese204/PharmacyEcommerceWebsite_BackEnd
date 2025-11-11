package com.example.pharmacywebsite.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewListResponse {

    private List<Item> items; // danh sách review

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {
        private Integer reviewId;
        private Integer userId;
        private String userName;
        private String userRole;
        private Integer medicineId;
        private Integer rating;
        private String reviewText;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<CommentDto> comments;
        private Capabilities capabilities;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommentDto {
        private Integer id;
        private Integer reviewId;
        private Integer userId;
        private String userName;
        private String userRole;
        private String commentText;
        private LocalDateTime createdAt;

        private Integer parentCommentId;
        private List<CommentDto> replies;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Capabilities {
        private boolean canReply;
        private boolean canEdit;
        private boolean canDelete;
    }
}
