// InventoryService.java
package com.example.pharmacywebsite.service;

import com.example.pharmacywebsite.domain.Medicine;
import com.example.pharmacywebsite.domain.OrderItem;
import com.example.pharmacywebsite.repository.InventoryRepository;
import com.example.pharmacywebsite.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final MedicineRepository medicineRepository;

    public void deductStock(List<OrderItem> orderItems) {
        for (OrderItem item : orderItems) {
            Medicine medicine = item.getMedicine();
            // ... logic to deduct inventory from medicine
            // Bạn có thể truy xuất inventory và giảm số lượng tương ứng
        }
    }
}
