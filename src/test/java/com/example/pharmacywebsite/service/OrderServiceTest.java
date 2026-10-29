package com.example.pharmacywebsite.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.pharmacywebsite.domain.Order;
import com.example.pharmacywebsite.domain.OrderItem;
import com.example.pharmacywebsite.domain.OrderStatusLog;
import com.example.pharmacywebsite.enums.OrderStatus;
import com.example.pharmacywebsite.enums.PaymentMethod;
import com.example.pharmacywebsite.repository.InventoryLogRepository;
import com.example.pharmacywebsite.repository.InventoryRepository;
import com.example.pharmacywebsite.repository.MedicineRepository;
import com.example.pharmacywebsite.repository.OrderItemRepository;
import com.example.pharmacywebsite.repository.OrderRepository;
import com.example.pharmacywebsite.repository.OrderShippingInfoRepository;
import com.example.pharmacywebsite.repository.OrderStatusLogRepository;
import com.example.pharmacywebsite.repository.PaymentTransactionRepository;
import com.example.pharmacywebsite.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private PaymentTransactionRepository paymentTransactionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MedicineRepository medicineRepository;

    @Mock
    private OrderShippingInfoRepository orderShippingInfoRepository;

    @Mock
    private OrderStatusLogRepository orderStatusLogRepository;

    @Mock
    private InventoryLogRepository inventoryLogRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void getOrderById_shouldThrow_whenOrderDoesNotExist() {
        // Arrange
        Integer orderId = 1;

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> orderService.getOrderById(orderId));

        assertEquals("Order không tồn tại", exception.getMessage());

        verify(orderRepository).findById(orderId);

        verifyNoInteractions(
                orderStatusLogRepository,
                orderItemRepository,
                paymentTransactionRepository,
                orderShippingInfoRepository);
    }

    @Test
    void getOrderById_shouldThrow_whenPaymentDoesNotExist() {
        // Arrange
        Integer orderId = 1;

        Order order = new Order();
        order.setId(orderId);
        order.setOrderDate(LocalDateTime.of(2026, 1, 10, 10, 0));
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentMethod(PaymentMethod.COD);

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        when(orderStatusLogRepository
                .findByOrderIdOrderByUpdatedAtAsc(orderId))
                .thenReturn(List.<OrderStatusLog>of());

        when(orderItemRepository.findByOrderId(orderId))
                .thenReturn(List.<OrderItem>of());

        when(paymentTransactionRepository.findByOrder_Id(orderId))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> orderService.getOrderById(orderId));

        assertEquals(
                "Không có thông tin thanh toán",
                exception.getMessage());

        verify(paymentTransactionRepository)
                .findByOrder_Id(orderId);

        verifyNoInteractions(orderShippingInfoRepository);
    }
}
