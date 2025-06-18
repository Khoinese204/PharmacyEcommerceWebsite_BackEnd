package com.example.pharmacywebsite.service;

import com.example.pharmacywebsite.designpattern.CoR.InventoryCheckHandler;
import com.example.pharmacywebsite.designpattern.CoR.OrderContext;
import com.example.pharmacywebsite.designpattern.CoR.OrderCreationHandler;
import com.example.pharmacywebsite.designpattern.CoR.OrderLogHandler;
import com.example.pharmacywebsite.designpattern.CoR.PaymentVerificationHandler;
import com.example.pharmacywebsite.designpattern.CoR.PriceCalculationHandler;
import com.example.pharmacywebsite.designpattern.CoR.PromotionApplyHandler;
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
    @Autowired
    private InventoryCheckHandler inventoryHandler;
    @Autowired
    private PromotionApplyHandler promotionHandler;
    @Autowired
    private PaymentVerificationHandler paymentHandler;
    @Autowired
    private OrderCreationHandler orderCreationHandler;
    @Autowired
    private OrderLogHandler orderLogHandler;
    @Autowired
    private PriceCalculationHandler priceCalculationHandler;

    @Transactional
    public OrderDto createOrder(CreateOrderDTO dto) {
        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));

        CartDto cart = cartService.getCartByUserId(dto.getUserId());

        // Tạo Order chưa có item
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING); // hoặc PENDING nếu cần duyệt
        order.setPaymentMethod(dto.getPaymentMethod());
        order.setTotalPrice(0.0); // sẽ cập nhật trong handler
        order = orderRepo.save(order);

        // Thiết lập shipping info
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

        // Tạo context
        OrderContext context = new OrderContext();
        context.setOrder(order);
        context.setUser(user);
        context.setCart(cart);
        context.setDto(dto);

        // Nối chuỗi handler
        inventoryHandler
                .setNext(promotionHandler)
                .setNext(priceCalculationHandler)
                .setNext(paymentHandler)
                .setNext(orderCreationHandler)
                .setNext(orderLogHandler);

        // Bắt đầu xử lý
        inventoryHandler.handle(context);

        // Sau khi xử lý xong → clear giỏ hàng
        cartService.clearCart(dto.getUserId());

        return toDto(order);
    }

    public List<OrderDto> getAllOrders() {
        return orderRepo.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
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

    public List<OrderItemDto> getOrderItems(Integer orderId) {
        System.out.println("Fetching orderId: " + orderId);
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ApiException("Order not found", HttpStatus.NOT_FOUND));

        List<OrderItem> items = orderItemRepo.findByOrderId(orderId);
        System.out.println("Found items: " + items.size());

        return items.stream().map(item -> {
            OrderItemDto dto = new OrderItemDto();
            if (item.getMedicine() == null) {
                throw new RuntimeException("Thuốc trong đơn hàng bị null: orderItemId = " + item.getId());
            }
            dto.setMedicineName(item.getMedicine().getName());
            dto.setQuantity(item.getQuantity());
            dto.setUnitPrice(item.getUnitPrice());
            return dto;
        }).collect(Collectors.toList());
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
        dto.setCustomer(order.getUser().getFullName());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setTotalPrice(order.getTotalPrice());
        return dto;
    }

}
