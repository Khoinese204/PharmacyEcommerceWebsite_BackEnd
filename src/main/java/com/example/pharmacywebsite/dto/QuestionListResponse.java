package com.example.pharmacywebsite.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionListResponse {
    private List<Item> items;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {
        private Integer questionId;
        private Integer userId;
        private Integer medicineId;
        private String content;
        private LocalDateTime createdAt;
        private List<AnswerDto> answers;
        private Capabilities capabilities;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AnswerDto {
        private Integer answerId;
        private Integer questionId;
        private Integer userId;
        private String userRole;
        private String content;
        private LocalDateTime createdAt;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Capabilities {
        private boolean canAnswer;
    }
}
