// service/OrderService.java
package com.example.pharmacywebsite.service;

import com.example.pharmacywebsite.designpattern.FactoryMethod.BankTransferPaymentFactory;
import com.example.pharmacywebsite.designpattern.FactoryMethod.CodPaymentFactory;
import com.example.pharmacywebsite.designpattern.FactoryMethod.PaymentFactory;
import com.example.pharmacywebsite.designpattern.State.OrderContext;
import com.example.pharmacywebsite.domain.*;
import com.example.pharmacywebsite.dto.CreateOrderRequest;
import com.example.pharmacywebsite.dto.CustomerInfoDto;
import com.example.pharmacywebsite.dto.ItemDto;
import com.example.pharmacywebsite.dto.OrderDetailDto;
import com.example.pharmacywebsite.dto.OrderDetailResponse;
import com.example.pharmacywebsite.dto.OrderDto;
import com.example.pharmacywebsite.dto.OrderHistoryDto;
import com.example.pharmacywebsite.dto.OrderItemDetailDto;
import com.example.pharmacywebsite.dto.OrderItemDto;
import com.example.pharmacywebsite.dto.PaymentDto;
import com.example.pharmacywebsite.dto.ShippingInfoDto;
import com.example.pharmacywebsite.dto.StatusLogDto;
import com.example.pharmacywebsite.dto.SummaryDto;
import com.example.pharmacywebsite.dto.UpdateOrderStatusRequest;
import com.example.pharmacywebsite.enums.OrderStatus;
import com.example.pharmacywebsite.enums.PaymentMethod;
import com.example.pharmacywebsite.exception.ApiException;
import com.example.pharmacywebsite.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        // ✅ Gán thêm 2 trường giảm giá
        order.setVoucherDiscount(request.getVoucherDiscount() != null ? request.getVoucherDiscount() : 0);
        order.setShippingDiscount(request.getShippingDiscount() != null ? request.getShippingDiscount() : 0);

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

    public List<OrderHistoryDto> getOrdersByUserId(Integer userId) {
        List<Order> orders = orderRepository.findByUser_IdOrderByOrderDateDesc(userId);
        return orders.stream().map(order -> new OrderHistoryDto(
                "DH" + order.getId(),
                order.getTotalPrice(),
                order.getOrderDate().format(DateTimeFormatter.ofPattern("HH:mm:ss dd/M/yyyy")),
                order.getStatus().name())).collect(Collectors.toList());
    }

    // service/OrderService.java
    // service/OrderService.java
    public OrderDetailResponse getOrderById(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order không tồn tại"));

        List<OrderStatusLog> logs = orderStatusLogRepository.findByOrderIdOrderByUpdatedAtAsc(id);

        List<StatusLogDto> statusLogs = new ArrayList<>();

        // Nếu không có log nào => thêm log mặc định từ Order entity
        if (logs.isEmpty()) {
            statusLogs.add(new StatusLogDto(
                    order.getStatus().name(),
                    order.getOrderDate().format(DateTimeFormatter.ofPattern("HH:mm:ss dd/M/yyyy"))));
        } else {
            // Nếu đã có logs thì hiển thị từ bảng logs
            for (OrderStatusLog log : logs) {
                statusLogs.add(new StatusLogDto(
                        log.getStatus().name(),
                        log.getUpdatedAt().format(DateTimeFormatter.ofPattern("HH:mm:ss dd/M/yyyy"))));
            }
        }
        List<ItemDto> items = orderItemRepository.findByOrderId(id).stream().map(oi -> {
            Medicine med = oi.getMedicine();
            return new ItemDto(
                    med.getName(),
                    med.getImageUrl(),
                    med.getUnit(),
                    oi.getUnitPrice(),
                    med.getOriginalPrice(),
                    oi.getQuantity());
        }).toList();

        SummaryDto summary = new SummaryDto(
                items.stream().mapToDouble(i -> i.getOriginalPrice() * i.getQuantity()).sum(),
                items.stream().mapToDouble(i -> (i.getOriginalPrice() - i.getUnitPrice()) * i.getQuantity()).sum(),
                order.getVoucherDiscount() != null ? order.getVoucherDiscount() : 0,
                order.getShippingDiscount() != null ? order.getShippingDiscount() : 0,
                order.getTotalPrice());

        PaymentTransaction tx = paymentTransactionRepository.findByOrder_Id(id)
                .orElseThrow(() -> new RuntimeException("Không có thông tin thanh toán"));
        PaymentDto payment = new PaymentDto(order.getPaymentMethod().name(), tx.getStatus().name());

        OrderShippingInfo ship = orderShippingInfoRepository.findByOrder(order);
        if (ship == null) {
            throw new RuntimeException("Không có thông tin giao hàng");
        }
        CustomerInfoDto customer = new CustomerInfoDto(
                ship.getRecipientName(),
                ship.getPhone(),
                ship.getAddressDetail() + ", " + ship.getWard() + ", " + ship.getDistrict() + ", " + ship.getProvince(),
                ship.getNote());

        boolean canCancel = order.getStatus() == OrderStatus.PENDING;

        return new OrderDetailResponse(
                order.getId(),
                "DH" + order.getId(),
                order.getOrderDate(),
                order.getOrderDate().toLocalDate().plusDays(3), // giả sử ngày giao hàng là 3 ngày sau
                order.getStatus().name(),
                statusLogs,
                items,
                summary,
                payment,
                customer,
                canCancel);
    }

    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream().map(order -> {
            OrderDto dto = new OrderDto();
            dto.setOrderId(order.getId());
            dto.setOrderDate(order.getOrderDate());
            dto.setCustomer(order.getUser().getFullName());
            dto.setStatus(order.getStatus());
            dto.setTotalPrice(order.getTotalPrice());
            return dto;
        }).collect(Collectors.toList());
    }

    // ✅ Cập nhật trạng thái đơn hàng
    @Transactional
    public void updateOrderStatus(Integer id, UpdateOrderStatusRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!id.equals(request.getOrderId())) {
            throw new RuntimeException("ID mismatch between path and request body");
        }

        User updatedBy = userRepository.findById(request.getUpdatedByUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        OrderContext context = new OrderContext(order, updatedBy, orderRepository,
                orderStatusLogRepository);

        OrderStatus currentStatus = order.getStatus();
        OrderStatus targetStatus = request.getNewStatus();

        if (currentStatus == targetStatus) {
            // Không cần cập nhật nếu trạng thái không thay đổi
            return;
        }

        if (targetStatus == OrderStatus.CANCELLED) {
            context.cancel();
        } else {
            // Logic cho phép chuyển tiếp theo thứ tự
            switch (currentStatus) {
                case PENDING -> {
                    if (targetStatus == OrderStatus.PACKING) {
                        context.next();
                    } else {
                        throw new RuntimeException("Không thể chuyển trực tiếp từ PENDING đến " +
                                targetStatus);
                    }
                }
                case PACKING -> {
                    if (targetStatus == OrderStatus.DELIVERING) {
                        context.next();
                    } else {
                        throw new RuntimeException("Không thể chuyển từ PACKING đến " +
                                targetStatus);
                    }
                }
                case DELIVERING -> {
                    if (targetStatus == OrderStatus.DELIVERED) {
                        context.next();
                    } else {
                        throw new RuntimeException("Không thể chuyển từ DELIVERING đến " +
                                targetStatus);
                    }
                }
                case DELIVERED, CANCELLED -> {
                    throw new RuntimeException("Không thể cập nhật từ trạng thái đã kết thúc");
                }
                default -> {
                    throw new RuntimeException("Trạng thái không hợp lệ");
                }
            }
        }
    }
    // @Transactional
    // public void updateOrderStatus(Integer id, UpdateOrderStatusRequest request) {
    // Order order = orderRepository.findById(id)
    // .orElseThrow(() -> new RuntimeException("Order not found"));

    // if (!id.equals(request.getOrderId())) {
    // throw new RuntimeException("ID mismatch between path and request body");
    // }

    // User updatedBy = userRepository.findById(request.getUpdatedByUserId())
    // .orElseThrow(() -> new RuntimeException("User not found"));

    // OrderContext context = new OrderContext(order, updatedBy, orderRepository,
    // orderStatusLogRepository);

    // OrderStatus currentStatus = order.getStatus();
    // OrderStatus targetStatus = request.getNewStatus();

    // // 🔒 Check role permission
    // String role = updatedBy.getRole().getName();

    // boolean isAllowed = switch (role) {
    // case "Admin" -> true;
    // case "Sales", "Warehouse" -> currentStatus == OrderStatus.PENDING ||
    // currentStatus == OrderStatus.PACKING;
    // default -> false;
    // };

    // if (!isAllowed) {
    // throw new RuntimeException("Bạn không có quyền cập nhật trạng thái đơn hàng ở
    // bước này.");
    // }

    // if (currentStatus == targetStatus) {
    // return; // trạng thái không thay đổi
    // }

    // // ✅ Áp dụng State Pattern
    // if (targetStatus == OrderStatus.CANCELLED) {
    // context.cancel();
    // } else {
    // switch (currentStatus) {
    // case PENDING -> {
    // if (targetStatus == OrderStatus.PACKING) {
    // context.next();
    // } else {
    // throw new RuntimeException("Không thể chuyển trực tiếp từ PENDING đến " +
    // targetStatus);
    // }
    // }
    // case PACKING -> {
    // if (targetStatus == OrderStatus.DELIVERING) {
    // context.next();
    // } else {
    // throw new RuntimeException("Không thể chuyển từ PACKING đến " +
    // targetStatus);
    // }
    // }
    // case DELIVERING -> {
    // if (targetStatus == OrderStatus.DELIVERED) {
    // context.next();
    // } else {
    // throw new RuntimeException("Không thể chuyển từ DELIVERING đến " +
    // targetStatus);
    // }
    // }
    // case DELIVERED, CANCELLED -> {
    // throw new RuntimeException("Không thể cập nhật từ trạng thái đã kết thúc");
    // }
    // default -> throw new RuntimeException("Trạng thái không hợp lệ");
    // }
    // }
    // }

}
