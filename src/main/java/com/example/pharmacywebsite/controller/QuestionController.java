package com.example.pharmacywebsite.controller;

import com.example.pharmacywebsite.dto.AnswerCreateDto;
import com.example.pharmacywebsite.dto.QuestionCreateDto;
import com.example.pharmacywebsite.dto.QuestionListResponse;
import com.example.pharmacywebsite.dto.QuestionResponseDto;
import com.example.pharmacywebsite.service.QuestionQueryService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medicines/{medicineId}/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionQueryService questionService;

    /** 2.1 Danh sách câu hỏi (không phân trang, sort theo người dùng chọn) */
    @GetMapping
    public ResponseEntity<QuestionListResponse> listQuestions(
            @PathVariable("medicineId") Integer medicineId,
            @RequestParam(value = "sort", defaultValue = "newest") String sort,
            @RequestParam(value = "viewerId", required = false) Integer viewerId) {
        var response = questionService.listAll(medicineId, sort, viewerId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<QuestionResponseDto> createQuestion(
            @PathVariable("medicineId") Integer medicineId,
            @RequestBody QuestionCreateDto body) {
        QuestionResponseDto dto = questionService.createQuestion(medicineId, body);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    /** 2.3 Trả lời câu hỏi (CUSTOMER / PHARMACIST / ADMIN) */
    @PostMapping("/{questionId}/answers")
    public ResponseEntity<QuestionResponseDto> answerQuestion(
            @PathVariable("medicineId") Integer medicineId,
            @PathVariable("questionId") Integer questionId,
            @RequestBody AnswerCreateDto body) {
        var dto = questionService.answerQuestion(medicineId, questionId, body);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
}
