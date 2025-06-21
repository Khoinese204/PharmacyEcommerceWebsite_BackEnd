package com.example.pharmacywebsite.seed;

import com.example.pharmacywebsite.domain.Inventory;
import com.example.pharmacywebsite.domain.Medicine;
import com.example.pharmacywebsite.enums.InventoryStatus;
import com.example.pharmacywebsite.repository.InventoryRepository;
import com.example.pharmacywebsite.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class InventorySeeder implements CommandLineRunner {

    private final InventoryRepository inventoryRepo;
    private final MedicineRepository medicineRepo;

    @Override
    public void run(String... args) throws Exception {
        if (inventoryRepo.count() == 0) {
            Inventory inv1 = new Inventory();
            inv1.setMedicine(medicineRepo.findById(1).orElseThrow());
            inv1.setQuantity(100);
            inv1.setExpiredAt(LocalDate.of(2025, 12, 31));
            inv1.setStatus(InventoryStatus.AVAILABLE);
            inv1.setCreatedAt(LocalDateTime.now());

            Inventory inv2 = new Inventory();
            inv2.setMedicine(medicineRepo.findById(2).orElseThrow());
            inv2.setQuantity(20);
            inv2.setExpiredAt(LocalDate.of(2025, 9, 15));
            inv2.setStatus(InventoryStatus.LOW_STOCK);
            inv2.setCreatedAt(LocalDateTime.now());

            Inventory inv3 = new Inventory();
            inv3.setMedicine(medicineRepo.findById(3).orElseThrow());
            inv3.setQuantity(50);
            inv3.setExpiredAt(LocalDate.of(2025, 12, 1));
            inv3.setStatus(InventoryStatus.AVAILABLE);
            inv3.setCreatedAt(LocalDateTime.now());

            Inventory inv4 = new Inventory();
            inv4.setMedicine(medicineRepo.findById(4).orElseThrow());
            inv4.setQuantity(10);
            inv4.setExpiredAt(LocalDate.of(2026, 7, 1));
            inv4.setStatus(InventoryStatus.LOW_STOCK);
            inv4.setCreatedAt(LocalDateTime.now());

            Inventory inv5 = new Inventory();
            inv5.setMedicine(medicineRepo.findById(5).orElseThrow());
            inv5.setQuantity(75);
            inv5.setExpiredAt(LocalDate.of(2026, 1, 20));
            inv5.setStatus(InventoryStatus.AVAILABLE);
            inv5.setCreatedAt(LocalDateTime.now());

            inventoryRepo.save(inv1);
            inventoryRepo.save(inv2);
            inventoryRepo.save(inv3);
            inventoryRepo.save(inv4);
            inventoryRepo.save(inv5);

            System.out.println("✅ Seeded 5 inventory records.");
        }
    }
}
