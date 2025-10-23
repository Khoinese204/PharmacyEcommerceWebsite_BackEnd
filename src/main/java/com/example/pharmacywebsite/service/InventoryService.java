package com.example.pharmacywebsite.service;

import com.example.pharmacywebsite.designpattern.Observer.InventoryObserverManager;
import com.example.pharmacywebsite.domain.Inventory;
import com.example.pharmacywebsite.domain.InventoryLog;
import com.example.pharmacywebsite.domain.Medicine;
import com.example.pharmacywebsite.domain.OrderItem;
import com.example.pharmacywebsite.dto.InventoryDto;
import com.example.pharmacywebsite.dto.InventoryLogDto;
import com.example.pharmacywebsite.dto.WarehouseDashboardDto;
import com.example.pharmacywebsite.enums.DateStatus;
import com.example.pharmacywebsite.enums.InventoryStatus;
import com.example.pharmacywebsite.repository.InventoryLogRepository;
import com.example.pharmacywebsite.repository.InventoryRepository;
import com.example.pharmacywebsite.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final MedicineRepository medicineRepository;
    private final InventoryLogRepository inventoryLogRepository;
    private final InventoryObserverManager inventoryObserverManager;

    public void deductStock(List<OrderItem> orderItems) {
        for (OrderItem item : orderItems) {
            Medicine medicine = item.getMedicine();

            List<Inventory> inventories = inventoryRepository
                    .findByMedicineOrderByExpiredAtAsc(medicine).stream()
                    .filter(inv -> inv.getStatus() != InventoryStatus.OUT_OF_STOCK &&
                            (inv.getExpiredAt() == null || !inv.getExpiredAt().isBefore(LocalDate.now())))
                    .collect(Collectors.toList());

            int quantityToDeduct = item.getQuantity();

            for (Inventory inventory : inventories) {
                if (quantityToDeduct == 0)
                    break;

                int available = inventory.getQuantity();

                if (available >= quantityToDeduct) {
                    inventory.setQuantity(available - quantityToDeduct);
                    inventoryRepository.save(inventory);
                    inventoryObserverManager.notifyAll(inventory);
                    quantityToDeduct = 0;
                } else {
                    inventory.setQuantity(0);
                    inventoryRepository.save(inventory);
                    inventoryObserverManager.notifyAll(inventory);
                    quantityToDeduct -= available;
                }
            }

            if (quantityToDeduct > 0) {
                throw new RuntimeException("Không đủ hàng trong kho cho thuốc: " + medicine.getName());
            }
        }
    }

    public List<InventoryDto> getAllInventory() {
        List<Inventory> inventories = inventoryRepository.findAll();
        return inventories.stream().map(inv -> {
            InventoryStatus computedStatus = inv.getQuantity() == 0
                    ? InventoryStatus.OUT_OF_STOCK
                    : (inv.getQuantity() <= 20 ? InventoryStatus.LOW_STOCK : InventoryStatus.AVAILABLE);

            DateStatus dateStatus;
            LocalDate today = LocalDate.now();
            LocalDate nearExpiryDate = today.plusDays(30);

            if (inv.getExpiredAt().isBefore(today)) {
                dateStatus = DateStatus.EXPIRED;
            } else if (!inv.getExpiredAt().isAfter(nearExpiryDate)) {
                dateStatus = DateStatus.NEAR_EXPIRY;
            } else {
                dateStatus = DateStatus.VALID;
            }

            InventoryDto dto = new InventoryDto();
            dto.setId(inv.getId());
            dto.setBatchNumber("BATCH" + inv.getId());
            dto.setProductName(inv.getMedicine().getName());
            dto.setQuantity(inv.getQuantity());
            dto.setExpiryDate(inv.getExpiredAt().toString());
            dto.setStatus(mapStatusToText(computedStatus));
            dto.setDateStatus(mapDateStatusToText(dateStatus));
            dto.setUnitPrice(inv.getMedicine().getOriginalPrice());
            return dto;
        }).toList();
    }

    private String mapStatusToText(InventoryStatus status) {
        return switch (status) {
            case AVAILABLE -> "Còn hàng";
            case LOW_STOCK -> "Sắp hết hàng";
            case OUT_OF_STOCK -> "Hết hàng";
        };
    }

    private String mapDateStatusToText(DateStatus status) {
        return switch (status) {
            case EXPIRED -> "Hết hạn";
            case VALID -> "Còn hạn";
            case NEAR_EXPIRY -> "Sắp hết hạn";
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

    public int getQuantityByMedicineId(Integer medicineId) {
        LocalDate today = LocalDate.now();
        List<Inventory> inventories = inventoryRepository.findByMedicineId(medicineId);

        return inventories.stream()
                .filter(inv -> (inv.getStatus() == InventoryStatus.AVAILABLE
                        || inv.getStatus() == InventoryStatus.LOW_STOCK) &&
                        inv.getExpiredAt() != null &&
                        !inv.getExpiredAt().isBefore(today))
                .mapToInt(Inventory::getQuantity)
                .sum();
    }

    public Map<Integer, Integer> getTotalQuantitiesByMedicineIds(List<Integer> medicineIds) {
        LocalDate today = LocalDate.now();

        List<Inventory> inventories = inventoryRepository.findByMedicineIdIn(medicineIds).stream()
                .filter(inv -> (inv.getStatus() == InventoryStatus.AVAILABLE
                        || inv.getStatus() == InventoryStatus.LOW_STOCK)
                        && inv.getExpiredAt() != null
                        && !inv.getExpiredAt().isBefore(today))
                .collect(Collectors.toList());

        return inventories.stream()
                .collect(Collectors.groupingBy(
                        inv -> inv.getMedicine().getId(),
                        Collectors.summingInt(Inventory::getQuantity)));
    }

    public void updateInventoryQuantity(int inventoryId, int newQuantity) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tồn kho"));

        inventory.setQuantity(newQuantity);

        InventoryStatus newStatus = (newQuantity == 0)
                ? InventoryStatus.OUT_OF_STOCK
                : (newQuantity <= 20 ? InventoryStatus.LOW_STOCK : InventoryStatus.AVAILABLE);

        inventory.setStatus(newStatus);
        inventoryRepository.save(inventory);
        inventoryObserverManager.notifyAll(inventory);
    }

    public InventoryDto getInventoryById(int inventoryId) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tồn kho"));

        Medicine medicine = inventory.getMedicine();

        InventoryStatus computedStatus = inventory.getQuantity() == 0
                ? InventoryStatus.OUT_OF_STOCK
                : (inventory.getQuantity() <= 20 ? InventoryStatus.LOW_STOCK : InventoryStatus.AVAILABLE);

        DateStatus dateStatus = inventory.getExpiredAt().isBefore(LocalDate.now())
                ? DateStatus.EXPIRED
                : DateStatus.VALID;

        InventoryDto dto = new InventoryDto();
        dto.setId(inventory.getId());
        dto.setBatchNumber("BATCH" + inventory.getId());
        dto.setProductName(medicine.getName());
        dto.setQuantity(inventory.getQuantity());
        dto.setExpiryDate(inventory.getExpiredAt().toString());
        dto.setStatus(mapStatusToText(computedStatus));
        dto.setDateStatus(mapDateStatusToText(dateStatus));
        dto.setUnitPrice(medicine.getOriginalPrice());
        return dto;
    }

    public Map<String, List<InventoryDto>> getInventoryAlerts() {
        List<Inventory> inventories = inventoryRepository.findAll();
        LocalDate today = LocalDate.now();

        int lowStockThreshold = 20;
        int nearExpiryDays = 30;

        List<InventoryDto> lowStockList = new ArrayList<>();
        List<InventoryDto> nearExpiryList = new ArrayList<>();

        for (Inventory inv : inventories) {
            boolean isLowStock = inv.getQuantity() <= lowStockThreshold;
            boolean isNearExpiry = inv.getExpiredAt() != null &&
                    !inv.getExpiredAt().isBefore(today) &&
                    !inv.getExpiredAt().isAfter(today.plusDays(nearExpiryDays));

            if (isLowStock) {
                lowStockList.add(convertToDto(inv));
            }

            if (isNearExpiry) {
                nearExpiryList.add(convertToDto(inv));
            }
        }

        return Map.of(
                "lowStock", lowStockList,
                "nearExpiry", nearExpiryList);
    }

    private InventoryDto convertToDto(Inventory inv) {
        InventoryDto dto = new InventoryDto();
        dto.setId(inv.getId());
        dto.setBatchNumber("BATCH" + inv.getId());
        dto.setProductName(inv.getMedicine().getName());
        dto.setQuantity(inv.getQuantity());
        dto.setExpiryDate(inv.getExpiredAt() != null ? inv.getExpiredAt().toString() : "N/A");
        dto.setStatus(mapStatusToText(inv.getStatus()));
        dto.setDateStatus(inv.getExpiredAt() != null && inv.getExpiredAt().isBefore(LocalDate.now())
                ? "Hết hạn"
                : "Còn hạn");
        dto.setUnitPrice(inv.getMedicine().getOriginalPrice());
        return dto;
    }

}
