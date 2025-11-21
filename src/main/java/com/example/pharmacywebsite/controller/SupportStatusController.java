package com.example.pharmacywebsite.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.pharmacywebsite.service.SupportStatusService;

import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/support")
public class SupportStatusController {

    private final SupportStatusService supportStatusService;

    public SupportStatusController(SupportStatusService supportStatusService) {
        this.supportStatusService = supportStatusService;
    }

    // FE (dashboard dược sĩ) gọi định kỳ 30–60s để báo "tôi đang online"
    @PutMapping("/heartbeat")
    public ResponseEntity<Void> heartbeat(Authentication authentication) {
        String email = authentication.getName();
        supportStatusService.updateSupportHeartbeat(email);
        return ResponseEntity.ok().build();
    }

    // Dùng để chọn dược sĩ cho room
    @GetMapping("/online-pharmacists")
    public ResponseEntity<List<String>> getOnlinePharmacists() {
        List<String> names = supportStatusService.getOnlinePharmacistsEntities()
                .stream()
                .map(u -> u.getFullName())
                .toList();
        return ResponseEntity.ok(names);
    }
}
