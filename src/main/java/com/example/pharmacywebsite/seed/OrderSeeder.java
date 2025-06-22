package com.example.pharmacywebsite.seed;

import com.example.pharmacywebsite.domain.*;
import com.example.pharmacywebsite.enums.OrderStatus;
import com.example.pharmacywebsite.enums.PaymentMethod;
import com.example.pharmacywebsite.enums.PaymentStatus;
import com.example.pharmacywebsite.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Component
@org.springframework.core.annotation.Order(7) // dùng đầy đủ ở đây
@RequiredArgsConstructor
public class OrderSeeder implements CommandLineRunner {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderShippingInfoRepository orderShippingInfoRepository;
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final UserRepository userRepository;
    private final MedicineRepository medicineRepository;

    @Override
    public void run(String... args) {
        if (orderRepository.count() == 0) { // Comment để chạy lại seed (Chỉ chạy 1 lần duy nhất, TẮT TRƯỚC KHI CHẠY
                                                // LẠI)
            List<User> customers = userRepository.findByRole_Name("Customer");
            List<Medicine> medicines = medicineRepository.findAll();
            if (customers.isEmpty() || medicines.size() < 3)
                return;

            Random random = new Random();
            LocalDateTime now = LocalDateTime.now();

            for (int i = 1; i <= 10; i++) {
                User customer = customers.get(random.nextInt(customers.size()));
                Order order = new Order();
                order.setUser(customer);
                order.setOrderDate(now.minusDays(random.nextInt(20)));
                order.setStatus(random.nextBoolean() ? OrderStatus.PENDING : OrderStatus.DELIVERED);
                order.setPaymentMethod(random.nextBoolean() ? PaymentMethod.COD : PaymentMethod.BANK_TRANSFER);
                order.setVoucherDiscount(5000.0);
                order.setShippingDiscount(10000.0);
                order.setTotalPrice(0.0); // tính sau
                order = orderRepository.save(order);

                // Shipping info
                OrderShippingInfo shipping = new OrderShippingInfo();
                shipping.setOrder(order);
                shipping.setRecipientName("Nguyễn Văn " + i);
                shipping.setPhone("090000000" + i);
                shipping.setProvince("Hồ Chí Minh");
                shipping.setDistrict("Quận " + (i % 5 + 1));
                shipping.setWard("Phường " + (i % 10 + 1));
                shipping.setAddressDetail("Số " + i + " Đường ABC");
                shipping.setNote("Giao giờ hành chính");
                shipping.setRequiresInvoice(i % 2 == 0);
                orderShippingInfoRepository.save(shipping);

                // Order items
                double total = 0.0;
                for (int j = 0; j < 2; j++) {
                    Medicine med = medicines.get(random.nextInt(medicines.size()));
                    int quantity = random.nextInt(3) + 1;
                    OrderItem item = new OrderItem();
                    item.setOrder(order);
                    item.setMedicine(med);
                    item.setQuantity(quantity);
                    item.setUnitPrice(med.getPrice());
                    total += quantity * med.getPrice();
                    orderItemRepository.save(item);
                }
                total = total - order.getVoucherDiscount() - order.getShippingDiscount();
                order.setTotalPrice(Math.max(total, 0));
                orderRepository.save(order);

                // Payment
                PaymentTransaction transaction = new PaymentTransaction();
                transaction.setOrder(order);
                transaction.setPaymentMethod(order.getPaymentMethod());
                transaction.setAmount(order.getTotalPrice());
                transaction.setStatus(
                        order.getPaymentMethod() == PaymentMethod.COD ? PaymentStatus.PENDING : PaymentStatus.SUCCESS);
                transaction.setProviderTransactionId("TRANS" + System.currentTimeMillis());
                transaction.setCreatedAt(order.getOrderDate());
                transaction.setPaidAt(order.getPaymentMethod() == PaymentMethod.COD ? null : order.getOrderDate());
                paymentTransactionRepository.save(transaction);
            }

            System.out.println("✅ Order seed completed");
        }
    }
}
