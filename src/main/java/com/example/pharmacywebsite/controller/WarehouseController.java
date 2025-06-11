package com.example.pharmacywebsite.controller;

import com.example.pharmacywebsite.domain.User;
import com.example.pharmacywebsite.repository.UserRepository;
import com.example.pharmacywebsite.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/warehouse")
@RequiredArgsConstructor
public class WarehouseController {

    private final UserRepository userRepository;
    private final WarehouseService warehouseService;

    @PostMapping("/import/confirm")
    public ResponseEntity<String> confirmImport(@RequestParam Integer importOrderId,
            @RequestParam Integer confirmedById) {
        User confirmedBy = userRepository.findById(confirmedById)
                .orElseThrow(() -> new RuntimeException("User not found"));

        warehouseService.confirmImportOrder(importOrderId, confirmedBy);
        return ResponseEntity.ok("Xác nhận nhập kho thành công.");
    }
}
