package com.example.pharmacywebsite.seed;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.example.pharmacywebsite.domain.Role;
import com.example.pharmacywebsite.repository.RoleRepository;

@Component
@Order(1) // Chạy trước UserSeeder
@RequiredArgsConstructor
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        if (roleRepository.count() == 0) {
            Role role1 = new Role();
            role1.setId(1);
            role1.setName("Admin");

            Role role2 = new Role();
            role2.setId(2);
            role2.setName("Customer");

            Role role3 = new Role();
            role3.setId(3);
            role3.setName("Warehouse");

            Role role4 = new Role();
            role4.setId(4);
            role4.setName("Sales");

            Role role5 = new Role();
            role5.setId(5);
            role5.setName("Pharmacist");

            roleRepository.save(role1);
            roleRepository.save(role2);
            roleRepository.save(role3);
            roleRepository.save(role4);
            roleRepository.save(role5);

            System.out.println("✅ Role seed completed");
        }
    }
}
