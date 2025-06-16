package com.example.pharmacywebsite.seed;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.pharmacywebsite.domain.Role;
import com.example.pharmacywebsite.domain.User;
import com.example.pharmacywebsite.repository.RoleRepository;
import com.example.pharmacywebsite.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Order(2) // chạy sau RoleSeeder (nếu có)
@RequiredArgsConstructor
public class UserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository; // thêm dòng này

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            LocalDateTime now = LocalDateTime.now();

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            Role adminRole = roleRepository.findById(1).orElseThrow();
            User admin = new User();
            admin.setFullName("Admin User");
            admin.setEmail("admin@example.com");
            admin.setPasswordHash(passwordEncoder.encode("admin123")); // nên mã hóa thật nếu vào production
            admin.setGender("MALE");
            admin.setAddress("123 Admin St");
            admin.setAvatarUrl("https://example.com/admin.png");
            admin.setBirthDate(LocalDate.parse("1990-01-01"));
            admin.setCreatedAt(now);
            admin.setUpdatedAt(now);
            admin.setRole(adminRole); // gán role cho user
            userRepository.save(admin);

            Role customerRole = roleRepository.findById(2).orElseThrow();
            User customer = new User();
            customer.setFullName("Customer User");
            customer.setEmail("customer@example.com");
            customer.setPasswordHash(passwordEncoder.encode("customer123"));
            customer.setGender("FEMALE");
            customer.setAddress("456 Customer St");
            customer.setAvatarUrl("https://example.com/customer.png");
            customer.setBirthDate(LocalDate.parse("1995-02-02"));
            customer.setCreatedAt(now);
            customer.setUpdatedAt(now);
            customer.setRole(customerRole); // gán role cho user
            userRepository.save(customer);

            Role warehouseRole = roleRepository.findById(3).orElseThrow();
            User warehouse = new User();
            warehouse.setFullName("Warehouse User");
            warehouse.setEmail("warehouse@example.com");
            warehouse.setPasswordHash(passwordEncoder.encode("warehouse123"));
            warehouse.setGender("MALE");
            warehouse.setAddress("789 Warehouse St");
            warehouse.setAvatarUrl("https://example.com/warehouse.png");
            warehouse.setBirthDate(LocalDate.parse("1988-03-03"));
            warehouse.setCreatedAt(now);
            warehouse.setUpdatedAt(now);
            warehouse.setRole(warehouseRole); // gán role cho user
            userRepository.save(warehouse);

            Role salesRole = roleRepository.findById(4).orElseThrow();
            User sales = new User();
            sales.setFullName("Sales User");
            sales.setEmail("sales@example.com");
            sales.setPasswordHash(passwordEncoder.encode("sales123"));
            sales.setGender("FEMALE");
            sales.setAddress("321 Sales St");
            sales.setAvatarUrl("https://example.com/sales.png");
            sales.setBirthDate(LocalDate.parse("1992-04-04"));
            sales.setCreatedAt(now);
            sales.setUpdatedAt(now);
            sales.setRole(salesRole);
            userRepository.save(sales);

            System.out.println("✅ User seed completed");
        }
    }
}
