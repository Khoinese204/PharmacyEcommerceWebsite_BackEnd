// dto/review/CommentCreateDto.java
package com.example.pharmacywebsite.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentCreateDto {
    private Integer userId; // thêm userId
    private String commentText;
}