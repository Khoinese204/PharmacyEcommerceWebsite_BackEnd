// OrderStatusLogService.java
package com.example.pharmacywebsite.service;

import com.example.pharmacywebsite.domain.Order;
import com.example.pharmacywebsite.domain.OrderStatusLog;
import com.example.pharmacywebsite.domain.User;
import com.example.pharmacywebsite.enums.OrderStatus;
import com.example.pharmacywebsite.repository.OrderStatusLogRepository;
import com.example.pharmacywebsite.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderStatusLogService {

    private final OrderStatusLogRepository orderStatusLogRepository;
    private final UserRepository userRepository; // assume admin logs

    public void logInitialStatus(Order order) {
        OrderStatusLog log = new OrderStatusLog();
        log.setOrder(order);
        log.setStatus(OrderStatus.PENDING);
        log.setUpdatedAt(LocalDateTime.now());
        log.setNote("Đơn hàng được tạo");

        userRepository.findByRole_Name("Admin")
                .stream()
                .findFirst()
                .ifPresent(log::setUpdatedBy); // Gán admin đầu tiên có role là ADMIN

        orderStatusLogRepository.save(log);
    }

}