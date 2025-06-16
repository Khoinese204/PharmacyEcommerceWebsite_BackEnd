package com.example.pharmacywebsite.seed;

import com.example.pharmacywebsite.domain.Category;
import com.example.pharmacywebsite.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3) // Chạy trước MedicineSeeder
@RequiredArgsConstructor
public class CategorySeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) {
        if (categoryRepository.count() == 0) {
            Category cat1 = new Category();
            cat1.setId(1);
            cat1.setName("Thuốc");
            categoryRepository.save(cat1);

            Category cat2 = new Category();
            cat2.setId(2);
            cat2.setName("Thực phẩm chức năng");
            categoryRepository.save(cat2);

            Category cat3 = new Category();
            cat3.setId(3);
            cat3.setName("Chăm sóc cá nhân");
            categoryRepository.save(cat3);

            System.out.println("✅ Category seed completed");
        }
    }
}
