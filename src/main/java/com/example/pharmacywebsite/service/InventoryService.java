// InventoryService.java
package com.example.pharmacywebsite.service;

import com.example.pharmacywebsite.domain.Inventory;
import com.example.pharmacywebsite.domain.InventoryLog;
import com.example.pharmacywebsite.domain.Medicine;
import com.example.pharmacywebsite.domain.OrderItem;
import com.example.pharmacywebsite.dto.InventoryDto;
import com.example.pharmacywebsite.dto.InventoryLogDto;
import com.example.pharmacywebsite.enums.InventoryStatus;
import com.example.pharmacywebsite.repository.InventoryLogRepository;
import com.example.pharmacywebsite.repository.InventoryRepository;
import com.example.pharmacywebsite.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final MedicineRepository medicineRepository;
    private final InventoryLogRepository inventoryLogRepository;

    public void deductStock(List<OrderItem> orderItems) {
        for (OrderItem item : orderItems) {
            Medicine medicine = item.getMedicine();
            // ... logic to deduct inventory from medicine
            // Bạn có thể truy xuất inventory và giảm số lượng tương ứng
        }
    }

    public List<InventoryDto> getAllInventory() {
        List<Inventory> inventories = inventoryRepository.findAll();
        return inventories.stream().map(inv -> {
            InventoryDto dto = new InventoryDto();
            dto.setBatchNumber("BATCH" + inv.getId());
            dto.setProductName(inv.getMedicine().getName());
            dto.setQuantity(inv.getQuantity());
            dto.setExpiryDate(inv.getExpiredAt().toString());
            dto.setStatus(mapStatusToText(inv.getStatus()));
            return dto;
        }).toList();
    }

    private String mapStatusToText(InventoryStatus status) {
        return switch (status) {
            case AVAILABLE -> "Còn hạn";
            case LOW_STOCK -> "Sắp hết hàng";
        };
    }

    public List<InventoryLogDto> getInventoryLogs() {
        List<InventoryLog> logs = inventoryLogRepository.findAll();

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        return logs.stream().map(log -> {
            InventoryLogDto dto = new InventoryLogDto();
            dto.setMedicineName(log.getMedicine().getName());
            dto.setType(log.getType());
            dto.setQuantity(log.getQuantity());
            dto.setRelatedOrderId(log.getRelatedOrderId());
            dto.setCreatedAt(log.getCreatedAt().format(formatter));
            return dto;
        }).toList();
    }

}
