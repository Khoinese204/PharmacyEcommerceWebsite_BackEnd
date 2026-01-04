package com.example.pharmacywebsite.controller;

import com.example.pharmacywebsite.scheduler.DemandForecastScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestForecastController {

    private final DemandForecastScheduler scheduler;

    @GetMapping("/run-ai")
    public ResponseEntity<String> triggerAi() {
        scheduler.runForecast();
        return ResponseEntity.ok("Đã kích hoạt AI! Hãy check Log console.");
    }
}