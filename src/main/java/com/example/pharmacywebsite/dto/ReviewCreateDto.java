// dto/review/ReviewCreateDto.java
package com.example.pharmacywebsite.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewCreateDto {
    private Integer userId; // thêm userId
    private Integer rating;
    private String reviewText;
}
