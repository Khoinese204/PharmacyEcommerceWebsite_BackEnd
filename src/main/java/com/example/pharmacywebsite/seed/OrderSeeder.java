package com.example.pharmacywebsite.seed;

import com.example.pharmacywebsite.domain.*;
import com.example.pharmacywebsite.enums.OrderStatus;
import com.example.pharmacywebsite.enums.PaymentMethod;
import com.example.pharmacywebsite.repository.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@org.springframework.core.annotation.Order(3)
@RequiredArgsConstructor
public class OrderSeeder {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final MedicineRepository medicineRepository;

    @PostConstruct
    public void seed() {
        if (orderRepository.count() == 0) {
            // Lấy user và thuốc mẫu có kiểm tra tồn tại rõ ràng
            User user = userRepository.findById(1)
                    .orElseThrow(() -> new RuntimeException("❌ Không tìm thấy User với ID = 1"));

            Medicine medicine1 = medicineRepository.findById(7)
                    .orElseThrow(() -> new RuntimeException("❌ Không tìm thấy Medicine với ID = 1"));

            Medicine medicine2 = medicineRepository.findById(8)
                    .orElseThrow(() -> new RuntimeException("❌ Không tìm thấy Medicine với ID = 2"));

            // Tạo đơn hàng
            Order order = new Order();
            order.setUser(user);
            order.setOrderDate(LocalDateTime.now().minusDays(2));
            order.setStatus(OrderStatus.PENDING);
            order.setPaymentMethod(PaymentMethod.MOMO);
            order.setTotalPrice(medicine1.getPrice() * 2 + medicine2.getPrice());

            orderRepository.save(order);

            // Tạo order items
            OrderItem item1 = new OrderItem();
            item1.setOrder(order);
            item1.setMedicine(medicine1);
            item1.setQuantity(2);
            item1.setUnitPrice(medicine1.getPrice());

            OrderItem item2 = new OrderItem();
            item2.setOrder(order);
            item2.setMedicine(medicine2);
            item2.setQuantity(1);
            item2.setUnitPrice(medicine2.getPrice());

            orderItemRepository.saveAll(Arrays.asList(item1, item2));

            System.out.println("✅ Đã seed đơn hàng mẫu với 2 sản phẩm.");
        }
    }
}
