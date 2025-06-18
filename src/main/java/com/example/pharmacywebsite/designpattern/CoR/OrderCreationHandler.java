package com.example.pharmacywebsite.designpattern.CoR;

import com.example.pharmacywebsite.domain.Medicine;
import com.example.pharmacywebsite.domain.Order;
import com.example.pharmacywebsite.domain.OrderItem;
import com.example.pharmacywebsite.dto.CartItemDto;
import com.example.pharmacywebsite.exception.ApiException;
import com.example.pharmacywebsite.repository.MedicineRepository;
import com.example.pharmacywebsite.repository.OrderItemRepository;
import com.example.pharmacywebsite.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderCreationHandler extends OrderHandler {

    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;
    private final MedicineRepository medicineRepo;

    @Override
    public void handle(OrderContext context) {
        Order order = context.getOrder();
        double total = 0.0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItemDto item : context.getCart().getItems()) {
            Medicine medicine = medicineRepo.findById(item.getMedicineId())
                    .orElseThrow(() -> new ApiException(
                            "Không tìm thấy thuốc với ID: " + item.getMedicineId(),
                            HttpStatus.NOT_FOUND));

            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setMedicine(medicine);
            oi.setQuantity(item.getQuantity());
            oi.setUnitPrice(medicine.getPrice());

            total += item.getQuantity() * medicine.getPrice();
            orderItems.add(oi);
        }

        // Lưu order và order items
        order.setTotalPrice(total);
        orderRepo.save(order);
        orderItemRepo.saveAll(orderItems); // tốt hơn về hiệu suất

        if (next != null)
            next.handle(context);
    }
}
