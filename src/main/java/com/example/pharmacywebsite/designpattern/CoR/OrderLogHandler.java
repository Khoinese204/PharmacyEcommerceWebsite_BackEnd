package com.example.pharmacywebsite.designpattern.CoR;

import com.example.pharmacywebsite.domain.OrderStatusLog;
import com.example.pharmacywebsite.enums.OrderStatus;
import com.example.pharmacywebsite.repository.OrderStatusLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderLogHandler extends OrderHandler {

    private final OrderStatusLogRepository orderStatusLogRepo;

    @Override
    public void handle(OrderContext context) {
        if (context.getOrder() != null && context.getUser() != null) {
            OrderStatusLog log = new OrderStatusLog();
            log.setOrder(context.getOrder());
            log.setStatus(OrderStatus.CONFIRMED); // Có thể truyền động nếu cần
            log.setUpdatedAt(LocalDateTime.now());
            log.setUpdatedBy(context.getUser());

            orderStatusLogRepo.save(log);
        }

        if (next != null)
            next.handle(context);
    }
}
