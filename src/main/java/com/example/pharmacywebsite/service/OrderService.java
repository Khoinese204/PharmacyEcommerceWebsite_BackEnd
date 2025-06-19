// service/OrderService.java
package com.example.pharmacywebsite.service;

import com.example.pharmacywebsite.designpattern.FactoryMethod.BankTransferPaymentFactory;
import com.example.pharmacywebsite.designpattern.FactoryMethod.CodPaymentFactory;
import com.example.pharmacywebsite.designpattern.FactoryMethod.PaymentFactory;
import com.example.pharmacywebsite.designpattern.State.OrderContext;
import com.example.pharmacywebsite.domain.*;
import com.example.pharmacywebsite.dto.CreateOrderRequest;

import com.example.pharmacywebsite.dto.OrderItemDto;
import com.example.pharmacywebsite.dto.ShippingInfoDto;
import com.example.pharmacywebsite.enums.OrderStatus;
import com.example.pharmacywebsite.enums.PaymentMethod;

import com.example.pharmacywebsite.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MedicineRepository medicineRepository;
    @Autowired
    private OrderShippingInfoRepository orderShippingInfoRepository;
    @Autowired
    private OrderStatusLogRepository orderStatusLogRepository;

    @Transactional
    public Order createOrder(CreateOrderRequest request) {
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("Thiếu userId");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalPrice(request.getTotalPrice());

        order = orderRepository.save(order);

        for (OrderItemDto item : request.getItems()) {
            Medicine medicine = medicineRepository.findById(item.getMedicineId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy thuốc"));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMedicine(medicine);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setUnitPrice(medicine.getPrice()); // 👈 lấy từ DB thay vì FE

            orderItemRepository.save(orderItem);
        }

        ShippingInfoDto info = request.getShippingInfo();

        OrderShippingInfo shipping = new OrderShippingInfo();
        shipping.setOrder(order);
        shipping.setRecipientName(info.getRecipientName());
        shipping.setPhone(info.getPhone());
        shipping.setProvince(info.getProvince());
        shipping.setDistrict(info.getDistrict());
        shipping.setWard(info.getWard());
        shipping.setAddressDetail(info.getAddressDetail());
        shipping.setNote(info.getNote());

        orderShippingInfoRepository.save(shipping);

        PaymentFactory factory = switch (request.getPaymentMethod()) {
            case COD -> new CodPaymentFactory();
            case BANK_TRANSFER -> new BankTransferPaymentFactory();
        };

        PaymentTransaction transaction = factory.createTransaction(order);
        paymentTransactionRepository.save(transaction);

        return order;
    }

    @Transactional
    public void moveOrderToNextStatus(Integer orderId, Integer userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order không tồn tại"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        OrderContext context = new OrderContext(order, user, orderRepository, orderStatusLogRepository);
        context.next();
    }

    @Transactional
    public void cancelOrder(Integer orderId, Integer userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order không tồn tại"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        OrderContext context = new OrderContext(order, user, orderRepository, orderStatusLogRepository);
        context.cancel();
    }

}
