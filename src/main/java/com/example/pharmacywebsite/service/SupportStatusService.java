package com.example.pharmacywebsite.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pharmacywebsite.domain.User;

import com.example.pharmacywebsite.repository.UserRepository;

@Service
public class SupportStatusService {

    private final UserRepository userRepository;

    public SupportStatusService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void updateSupportHeartbeat(String email) {
        User pharmacist = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String roleName = pharmacist.getRole() != null ? pharmacist.getRole().getName() : null;

        if (roleName == null || !roleName.equalsIgnoreCase("PHARMACIST")) {
            throw new RuntimeException("User is not pharmacist");
        }
        pharmacist.setLastActiveAt(LocalDateTime.now());
        pharmacist.setOnlineOnSupport(true);
        userRepository.save(pharmacist);
    }

    @Transactional(readOnly = true)
    public List<User> getOnlinePharmacistsEntities() {
        // Nếu gọi API heartbeat cách đây hơn 60 giây → user sẽ bị coi là offline.
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(1);

        return userRepository.findAll().stream()
                .filter(u -> {
                    String role = u.getRole() != null ? u.getRole().getName() : null;
                    return role != null && role.equalsIgnoreCase("PHARMACIST");
                })
                .filter(u -> Boolean.TRUE.equals(u.getOnlineOnSupport()))
                .filter(u -> u.getLastActiveAt() != null && u.getLastActiveAt().isAfter(threshold))
                .toList();
    }

}