package com.example.pharmacywebsite.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pharmacywebsite.designpattern.Observer.InventoryObserverManager;
import com.example.pharmacywebsite.domain.ImportOrder;
import com.example.pharmacywebsite.domain.ImportOrderItem;
import com.example.pharmacywebsite.domain.Inventory;
import com.example.pharmacywebsite.domain.InventoryLog;
import com.example.pharmacywebsite.domain.Medicine;
import com.example.pharmacywebsite.domain.Supplier;
import com.example.pharmacywebsite.dto.ImportOrderItemRequest;
import com.example.pharmacywebsite.dto.ImportOrderItemResponse;
import com.example.pharmacywebsite.dto.ImportOrderRequest;
import com.example.pharmacywebsite.dto.ImportOrderResponse;
import com.example.pharmacywebsite.enums.InventoryStatus;
import com.example.pharmacywebsite.repository.ImportOrderItemRepository;
import com.example.pharmacywebsite.repository.ImportOrderRepository;
import com.example.pharmacywebsite.repository.InventoryLogRepository;
import com.example.pharmacywebsite.repository.InventoryRepository;
import com.example.pharmacywebsite.repository.MedicineRepository;
import com.example.pharmacywebsite.repository.SupplierRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImportOrderService {
    private final SupplierRepository supplierRepository;
    private final MedicineRepository medicineRepository;
    private final ImportOrderRepository importOrderRepository;
    private final ImportOrderItemRepository importOrderItemRepository;
    private final InventoryLogRepository inventoryLogRepository;
    private final InventoryRepository inventoryRepository;
    private final InventoryObserverManager inventoryObserverManager;

    public ImportOrder createImportOrder(ImportOrderRequest request) {
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        ImportOrder order = new ImportOrder();
        order.setSupplier(supplier);
        order.setCreatedAt(LocalDateTime.now());

        double total = 0;
        List<ImportOrderItem> items = new ArrayList<>();

        for (ImportOrderItemRequest itemReq : request.getItems()) {
            Medicine medicine = medicineRepository.findById(itemReq.getMedicineId())
                    .orElseThrow(() -> new RuntimeException("Medicine not found"));

            ImportOrderItem item = new ImportOrderItem();
            item.setMedicine(medicine);
            item.setQuantity(itemReq.getQuantity());
            item.setUnitPrice(itemReq.getUnitPrice());
            item.setImportOrder(order);
            items.add(item);

            total += itemReq.getQuantity() * itemReq.getUnitPrice();
        }

        order.setTotalPrice(total);
        importOrderRepository.save(order);
        importOrderItemRepository.saveAll(items);

        for (int i = 0; i < items.size(); i++) {
            ImportOrderItem item = items.get(i);
            ImportOrderItemRequest itemReq = request.getItems().get(i);
            Medicine medicine = item.getMedicine();
            int quantity = item.getQuantity();
            LocalDate expiredAt = itemReq.getExpiredAt();

            // ✅ Tìm các lô chưa hết hạn & status còn dùng được
            List<Inventory> inventories = inventoryRepository
                    .findByMedicineOrderByExpiredAtAsc(medicine).stream()
                    .filter(inv -> inv.getExpiredAt() != null && !inv.getExpiredAt().isBefore(LocalDate.now()))
                    .filter(inv -> inv.getStatus() == InventoryStatus.AVAILABLE
                            || inv.getStatus() == InventoryStatus.LOW_STOCK)
                    .collect(Collectors.toList());

            // ✅ Tìm xem có lô nào cùng ngày hết hạn để cộng dồn không
            Optional<Inventory> existingLot = inventories.stream()
                    .filter(inv -> inv.getExpiredAt().equals(expiredAt))
                    .findFirst();

            if (existingLot.isPresent()) {
                Inventory inventory = existingLot.get();
                int newQty = inventory.getQuantity() + quantity;
                inventory.setQuantity(newQty);
                inventory.setStatus(calculateStatus(newQty));
                inventoryRepository.save(inventory);
                inventoryObserverManager.notifyAll(inventory);
            } else {
                Inventory newInventory = new Inventory();
                newInventory.setMedicine(medicine);
                newInventory.setQuantity(quantity);
                newInventory.setExpiredAt(expiredAt != null ? expiredAt : LocalDate.now().plusMonths(24));
                newInventory.setStatus(calculateStatus(quantity));
                newInventory.setCreatedAt(LocalDateTime.now());

                inventoryRepository.save(newInventory);
                inventoryObserverManager.notifyAll(newInventory);
            }

            InventoryLog log = new InventoryLog();
            log.setMedicine(medicine);
            log.setType("import");
            log.setQuantity(quantity);
            log.setRelatedOrderId(order.getId());
            log.setCreatedAt(LocalDateTime.now());

            inventoryLogRepository.save(log);
        }

        return order;
    }

    public List<ImportOrderResponse> getAllImportOrders() {
        List<ImportOrder> orders = importOrderRepository.findAll();

        return orders.stream().map(order -> {
            ImportOrderResponse response = new ImportOrderResponse();
            response.setId(order.getId());
            response.setSupplierName(order.getSupplier().getName());
            response.setTotalPrice(order.getTotalPrice());
            response.setCreatedAt(order.getCreatedAt());

            List<ImportOrderItemResponse> itemResponses = order.getImportOrderItems().stream().map(item -> {
                ImportOrderItemResponse itemRes = new ImportOrderItemResponse();
                itemRes.setId(item.getId());
                itemRes.setMedicineName(item.getMedicine().getName());
                itemRes.setQuantity(item.getQuantity());
                itemRes.setUnitPrice(item.getUnitPrice());
                return itemRes;
            }).toList();

            response.setItems(itemResponses);
            return response;
        }).toList();
    }

    public ImportOrderResponse getImportOrderById(Integer id) {
        ImportOrder order = importOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Import Order not found"));

        ImportOrderResponse response = new ImportOrderResponse();
        response.setId(order.getId());
        response.setSupplierName(order.getSupplier().getName());
        response.setTotalPrice(order.getTotalPrice());
        response.setCreatedAt(order.getCreatedAt());

        List<ImportOrderItemResponse> items = order.getImportOrderItems().stream().map(item -> {
            ImportOrderItemResponse res = new ImportOrderItemResponse();
            res.setId(item.getId());
            res.setMedicineName(item.getMedicine().getName());
            res.setQuantity(item.getQuantity());
            res.setUnitPrice(item.getUnitPrice());
            return res;
        }).toList();

        response.setItems(items);
        return response;
    }

    private InventoryStatus calculateStatus(int quantity) {
        return quantity <= 20 ? InventoryStatus.LOW_STOCK : InventoryStatus.AVAILABLE;
    }

}
