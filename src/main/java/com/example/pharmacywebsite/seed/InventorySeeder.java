package com.example.pharmacywebsite.seed;

import com.example.pharmacywebsite.domain.Inventory;
import com.example.pharmacywebsite.domain.Medicine;
import com.example.pharmacywebsite.enums.InventoryStatus;
import com.example.pharmacywebsite.repository.InventoryRepository;
import com.example.pharmacywebsite.repository.MedicineRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Component
@Order(6) // chạy sau MedicineSeeder
@RequiredArgsConstructor
public class InventorySeeder implements CommandLineRunner {

    private final InventoryRepository inventoryRepository;
    private final MedicineRepository medicineRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (inventoryRepository.count() == 0) { // Comment để chạy lại seed (Chỉ chạy 1 lần duy nhất, TẮT TRƯỚC KHI CHẠY
                                                // LẠI)
            Random random = new Random();
            LocalDateTime now = LocalDateTime.now();

            for (int medicineId = 104; medicineId <= 124; medicineId++) {
                Optional<Medicine> optionalMedicine = medicineRepository.findById(medicineId);
                if (optionalMedicine.isPresent()) {
                    Medicine medicine = optionalMedicine.get();

                    int quantity = random.nextInt(50) + 1; // 1 -> 50

                    Inventory inventory = new Inventory();
                    inventory.setMedicine(medicine);
                    inventory.setQuantity(quantity);
                    inventory.setExpiredAt(LocalDate.now().plusMonths(random.nextInt(12) + 1));
                    inventory.setStatus(quantity <= 20 ? InventoryStatus.LOW_STOCK : InventoryStatus.AVAILABLE);
                    inventory.setCreatedAt(now.minusDays(random.nextInt(30)));

                    inventoryRepository.save(inventory);
                }
            }

            System.out.println("✅ Inventory seed completed.");
        }
    }
}
