package com.example.pharmacywebsite.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionCreateDto {
    private Integer userId;   // FE gửi kèm userId
    private String content;   // Nội dung câu hỏi
}