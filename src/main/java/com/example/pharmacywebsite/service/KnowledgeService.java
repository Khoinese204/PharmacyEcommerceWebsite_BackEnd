package com.example.pharmacywebsite.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class KnowledgeService {
    public String buildGlobalKnowledge() {
        try {
            var resource = new ClassPathResource("knowledge/knowledge-base.md");
            byte[] bytes = resource.getInputStream().readAllBytes();
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "";
        }
    }
}
