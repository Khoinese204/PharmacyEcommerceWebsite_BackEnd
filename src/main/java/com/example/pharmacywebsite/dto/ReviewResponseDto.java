// dto/review/ReviewResponseDto.java
package com.example.pharmacywebsite.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @Builder
public class ReviewResponseDto {
    private Integer reviewId;
    private Integer userId;
    private Integer medicineId;

    private Integer rating;
    private String reviewText;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<CommentDto> comments;
    private Capabilities capabilities;

    @Getter @Setter @Builder
    public static class CommentDto {
        private Integer id;
        private Integer reviewId;
        private Integer userId;
        private String userRole;       // "PHARMACIST"/"ADMIN"/...
        private String commentText;
        private LocalDateTime createdAt;
    }

    @Getter @Setter @Builder
    public static class Capabilities {
        private boolean canReply;      // pharmacist/admin
        private boolean canEdit;       // owner
        private boolean canDelete;     // owner
    }
}
