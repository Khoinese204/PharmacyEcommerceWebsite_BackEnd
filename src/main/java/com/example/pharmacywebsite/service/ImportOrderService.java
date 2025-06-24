package com.example.pharmacywebsite.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

            total += itemReq.getQuantity() * itemReq.getUnitPrice();
            items.add(item);
        }

        order.setTotalPrice(total);
        importOrderRepository.save(order);
        importOrderItemRepository.saveAll(items);

        for (ImportOrderItem item : items) {
            Medicine medicine = item.getMedicine();

            List<Inventory> inventoryList = inventoryRepository.findByMedicineOrderByExpiredAtAsc(medicine);

            if (!inventoryList.isEmpty()) {
                Inventory inventory = inventoryList.get(0);
                int newQuantity = inventory.getQuantity() + item.getQuantity();
                inventory.setQuantity(newQuantity);
                inventory.setStatus(calculateStatus(newQuantity)); // ✅ cập nhật lại trạng thái
                inventoryRepository.save(inventory);
                inventoryObserverManager.notifyAll(inventory); // 👈 gọi Observer
            } else {
                int quantity = item.getQuantity();
                Inventory inventory = new Inventory();
                inventory.setMedicine(medicine);
                inventory.setQuantity(quantity);
                inventory.setExpiredAt(LocalDate.now().plusMonths(24));
                inventory.setStatus(calculateStatus(quantity)); // ✅ không set cứng nữa
                inventory.setCreatedAt(LocalDateTime.now());
                inventoryRepository.save(inventory);
                inventoryObserverManager.notifyAll(inventory); // 👈 gọi Observer
            }

            
            InventoryLog log = new InventoryLog();
            log.setMedicine(medicine);
            log.setType("import");
            log.setQuantity(item.getQuantity());
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
