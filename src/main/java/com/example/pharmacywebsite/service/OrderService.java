package com.example.pharmacywebsite.service;

import com.example.pharmacywebsite.domain.*;
import com.example.pharmacywebsite.dto.*;
import com.example.pharmacywebsite.enums.OrderStatus;
import com.example.pharmacywebsite.exception.ApiException;
import com.example.pharmacywebsite.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private OrderItemRepository orderItemRepo;
    @Autowired
    private CartService cartService;
    @Autowired
    private CartItemRepository cartItemRepo;
    @Autowired
    private MedicineRepository medicineRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private OrderShippingInfoRepository orderShippingInfoRepo;
    @Autowired
    private OrderStatusLogRepository orderStatusLogRepo;

    @Transactional
    public OrderDto createOrder(CreateOrderDTO dto) {
        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.CONFIRMED);
        order.setPaymentMethod(dto.getPaymentMethod());
        order.setTotalPrice(0.0);
        order = orderRepo.save(order);

        CartDto cart = cartService.getCartByUserId(dto.getUserId());

        double total = 0.0;
        for (CartItemDto item : cart.getItems()) {
            Medicine medicine = medicineRepo.findById(item.getMedicineId())
                    .orElseThrow(() -> new ApiException("Medicine not found", HttpStatus.NOT_FOUND));

            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setMedicine(medicine);
            oi.setQuantity(item.getQuantity());
            oi.setUnitPrice(medicine.getPrice());
            total += item.getQuantity() * medicine.getPrice();
            orderItemRepo.save(oi);
        }

        order.setTotalPrice(total);
        orderRepo.save(order);

        OrderShippingInfo info = new OrderShippingInfo();
        info.setOrder(order);
        info.setRecipientName(dto.getRecipientName());
        info.setPhone(dto.getPhone());
        info.setProvince(dto.getProvince());
        info.setDistrict(dto.getDistrict());
        info.setWard(dto.getWard());
        info.setAddressDetail(dto.getAddressDetail());
        info.setNote(dto.getNote());
        info.setRequiresInvoice(dto.getRequiresInvoice());
        orderShippingInfoRepo.save(info);

        OrderStatusLog log = new OrderStatusLog();
        log.setOrder(order);
        log.setStatus(OrderStatus.CONFIRMED);
        log.setUpdatedAt(LocalDateTime.now());
        log.setUpdatedBy(user); // giả định tạo đơn là người cập nhật
        orderStatusLogRepo.save(log);

        cartService.clearCart(dto.getUserId());

        return toDto(order);
    }

    public List<OrderDto> getOrdersByUser(Integer userId) {
        return orderRepo.findByUserId(userId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public OrderDto getOrderDetail(Integer orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ApiException("Order not found", HttpStatus.NOT_FOUND));
        return toDto(order);
    }

    @Transactional
    public void cancelOrder(Integer orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ApiException("Order not found", HttpStatus.NOT_FOUND));

        order.setStatus(OrderStatus.CANCELLED);
        orderRepo.save(order);

        OrderStatusLog log = new OrderStatusLog();
        log.setOrder(order);
        log.setStatus(OrderStatus.CANCELLED);
        log.setUpdatedAt(LocalDateTime.now());
        orderStatusLogRepo.save(log);
    }

    @Transactional
    public void updateOrderStatus(UpdateOrderStatusRequest dto) {
        Order order = orderRepo.findById(dto.getOrderId())
                .orElseThrow(() -> new ApiException("Order not found", HttpStatus.NOT_FOUND));

        User updater = userRepo.findById(dto.getUpdatedByUserId())
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));

        order.setStatus(dto.getNewStatus());
        orderRepo.save(order);

        OrderStatusLog log = new OrderStatusLog();
        log.setOrder(order);
        log.setStatus(dto.getNewStatus());
        log.setNote(dto.getNote());
        log.setUpdatedAt(LocalDateTime.now());
        log.setUpdatedBy(updater);
        orderStatusLogRepo.save(log);
    }

    private OrderDto toDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setOrderId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setTotalPrice(order.getTotalPrice());
        return dto;
    }
}
