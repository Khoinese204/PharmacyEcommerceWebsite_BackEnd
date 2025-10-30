package com.example.pharmacywebsite.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AnswerCreateDto {
    private Integer userId;   // FE gửi user đang trả lời
    private String content;   // Nội dung câu trả lời
}
