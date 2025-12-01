package com.example.pharmacywebsite.controller;

import com.example.pharmacywebsite.dto.AiRequest;
import com.example.pharmacywebsite.service.AiChatService;
import com.example.pharmacywebsite.service.KnowledgeService;

import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiChatService aiChatService;
    private final KnowledgeService knowledgeService;

    @PostMapping("/ask")
    public ResponseEntity<?> ask(@RequestBody AiRequest dto) {

        String answer = aiChatService.ask(dto.getQuestion());

        return ResponseEntity.ok(
                java.util.Map.of(
                        "disclaimer", "Nội dung chỉ mang tính tham khảo, không thay thế tư vấn y khoa.",
                        "answer", answer));
    }

    @PostMapping("/ask/with-knowledge")
    public ResponseEntity<Map<String, String>> askWithKnowledge(@RequestBody AiRequest dto) {

        String knowledge = knowledgeService.buildGlobalKnowledge();

        String answer = aiChatService.askWithKnowledge(dto.getQuestion(), knowledge);

        return ResponseEntity.ok(
                Map.of(
                        "disclaimer", "Nội dung chỉ mang tính tham khảo, không thay thế tư vấn từ bác sĩ/dược sĩ.",
                        "answer", answer));
    }
}
