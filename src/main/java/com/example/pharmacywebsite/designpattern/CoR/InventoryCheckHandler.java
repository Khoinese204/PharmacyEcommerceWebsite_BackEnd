package com.example.pharmacywebsite.designpattern.CoR;

import com.example.pharmacywebsite.domain.Inventory;
import com.example.pharmacywebsite.domain.Medicine;
import com.example.pharmacywebsite.dto.CartItemDto;
import com.example.pharmacywebsite.enums.InventoryStatus;
import com.example.pharmacywebsite.exception.ApiException;
import com.example.pharmacywebsite.repository.InventoryRepository;
import com.example.pharmacywebsite.repository.MedicineRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InventoryCheckHandler extends OrderHandler {

    private final InventoryRepository inventoryRepo;
    private final MedicineRepository medicineRepo;

    @Override
    public void handle(OrderContext context) {
        for (CartItemDto item : context.getCart().getItems()) {
            Medicine medicine = medicineRepo.findById(item.getMedicineId())
                    .orElseThrow(() -> new ApiException(
                            "Không tìm thấy thuốc với ID: " + item.getMedicineId(),
                            HttpStatus.NOT_FOUND));

            // Lấy danh sách tồn kho còn dùng được, sắp xếp theo hạn dùng tăng dần
            List<Inventory> inventories = inventoryRepo.findByMedicineAndStatusOrderByExpiredAtAsc(
                    medicine, InventoryStatus.AVAILABLE);

            int availableQty = inventories.stream()
                    .mapToInt(Inventory::getQuantity)
                    .sum();

            if (availableQty < item.getQuantity()) {
                throw new ApiException(
                        "Không đủ tồn kho cho thuốc: " + medicine.getName(),
                        HttpStatus.BAD_REQUEST);
            }
        }

        // Gọi handler tiếp theo nếu có
        if (next != null) {
            next.handle(context);
        }
    }
}
