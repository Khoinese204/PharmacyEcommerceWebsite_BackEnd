package com.example.pharmacywebsite.service;

import com.example.pharmacywebsite.domain.*;
import com.example.pharmacywebsite.dto.ImportOrderSummaryDto;
import com.example.pharmacywebsite.dto.InventoryDto;
import com.example.pharmacywebsite.enums.InventoryStatus;
import com.example.pharmacywebsite.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ImportOrderRepository importOrderRepository;
    private final InventoryRepository inventoryRepository;
    private final InventoryLogRepository inventoryLogRepository;
    private final WarehouseImportLogRepository warehouseImportLogRepository;
    private final WarehouseDeliveryLogRepository warehouseDeliveryLogRepository;

    public void confirmImportOrder(Integer importOrderId, User confirmedBy) {
        ImportOrder order = importOrderRepository.findById(importOrderId)
                .orElseThrow(() -> new RuntimeException("Import Order not found"));

        List<ImportOrderItem> items = order.getImportOrderItems();
        LocalDateTime now = LocalDateTime.now();

        for (ImportOrderItem item : items) {
            Medicine medicine = item.getMedicine();

            // ✅ Tạo inventory mới
            Inventory inventory = new Inventory();
            inventory.setMedicine(medicine);
            inventory.setQuantity(item.getQuantity());
            inventory.setExpiredAt(LocalDate.now().plusYears(2)); // giả sử hết hạn sau 2 năm
            inventory.setStatus(InventoryStatus.AVAILABLE); // bạn có thể tùy enum này
            inventory.setCreatedAt(now);
            inventoryRepository.save(inventory);

            // ✅ Ghi log inventory
            InventoryLog log = new InventoryLog();
            log.setMedicine(medicine);
            log.setType("import");
            log.setQuantity(item.getQuantity());
            log.setRelatedOrderId(order.getId());
            log.setCreatedAt(now);
            inventoryLogRepository.save(log);

            // ✅ Ghi log xác nhận nhập kho
            WarehouseImportLog confirmLog = new WarehouseImportLog();
            confirmLog.setImportOrder(order);
            confirmLog.setMedicine(medicine);
            confirmLog.setQuantityExpected(item.getQuantity()); // có thể thay đổi nếu có số thực nhận
            confirmLog.setQuantityReceived(item.getQuantity()); // giả sử đúng như mong đợi
            confirmLog.setConfirmedBy(confirmedBy);
            confirmLog.setConfirmedAt(now);
            confirmLog.setNote("Xác nhận đúng số lượng");
            warehouseImportLogRepository.save(confirmLog);
        }
    }

    @Transactional
    public void confirmExportOrder(Integer orderId, User deliveredBy) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        List<OrderItem> items = orderItemRepository.findByOrder(order);

        for (OrderItem item : items) {
            Medicine medicine = item.getMedicine();
            int quantityToExport = item.getQuantity();

            // Lấy các inventory hợp lệ theo hạn sử dụng (FIFO)
            List<Inventory> inventories = inventoryRepository
                    .findByMedicineAndStatusOrderByExpiredAtAsc(medicine, InventoryStatus.AVAILABLE);

            int remaining = quantityToExport;
            for (Inventory inventory : inventories) {
                if (remaining <= 0)
                    break;

                int deducted = Math.min(inventory.getQuantity(), remaining);
                inventory.setQuantity(inventory.getQuantity() - deducted);
                inventoryRepository.save(inventory);

                // Log inventory
                InventoryLog log = new InventoryLog();
                log.setMedicine(medicine);
                log.setType("export");
                log.setQuantity(deducted);
                log.setRelatedOrderId(order.getId());
                log.setCreatedAt(LocalDateTime.now());
                inventoryLogRepository.save(log);

                remaining -= deducted;
            }

            if (remaining > 0) {
                throw new RuntimeException("Không đủ tồn kho cho thuốc: " + medicine.getName());
            }

            // Ghi nhận giao hàng
            WarehouseDeliveryLog deliveryLog = new WarehouseDeliveryLog();
            deliveryLog.setOrder(order);
            deliveryLog.setMedicine(medicine);
            deliveryLog.setQuantity(quantityToExport);
            deliveryLog.setDeliveredBy(deliveredBy);
            deliveryLog.setDeliveredAt(LocalDateTime.now());
            deliveryLog.setNote("Xuất kho từ đơn hàng #" + order.getId());
            warehouseDeliveryLogRepository.save(deliveryLog);
        }
    }

    public List<InventoryDto> getInventoryList() {
        return inventoryRepository.findAll().stream().map(inventory -> {
            InventoryDto dto = new InventoryDto();
            dto.setBatchNumber("LOT" + String.format("%03d", inventory.getId()));
            dto.setProductName(inventory.getMedicine().getName());
            dto.setQuantity(inventory.getQuantity());
            dto.setExpiryDate(inventory.getExpiredAt().toString());
            dto.setStatus(inventory.getStatus().name()); // hoặc .name() nếu không có displayName
            return dto;
        }).toList();
    }

    public List<ImportOrderSummaryDto> getAllImportOrders() {
        List<ImportOrder> orders = importOrderRepository.findAll();

        return orders.stream().map(order -> {
            ImportOrderSummaryDto dto = new ImportOrderSummaryDto();
            dto.setId(order.getId());
            dto.setSupplierName(order.getSupplier().getName());
            dto.setTotalPrice(order.getTotalPrice());
            dto.setCreatedAt(order.getCreatedAt());
            dto.setStatus(order.getStatus()); // nếu dùng enum
            return dto;
        }).collect(Collectors.toList());
    }

}
