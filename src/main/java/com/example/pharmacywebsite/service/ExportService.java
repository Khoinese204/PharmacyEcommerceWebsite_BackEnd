package com.example.pharmacywebsite.service;

import com.example.pharmacywebsite.domain.Inventory;
import com.example.pharmacywebsite.domain.OrderItem;
import com.example.pharmacywebsite.domain.OrderShippingInfo;
import com.example.pharmacywebsite.dto.CustomerInfoDto;
import com.example.pharmacywebsite.dto.ExportDetailResponse;
import com.example.pharmacywebsite.dto.ExportItemDto;
import com.example.pharmacywebsite.dto.ExportOrderDto;
import com.example.pharmacywebsite.dto.SummaryDto;
import com.example.pharmacywebsite.enums.OrderStatus;
import com.example.pharmacywebsite.repository.InventoryRepository;
import com.example.pharmacywebsite.repository.OrderItemRepository;
import com.example.pharmacywebsite.repository.OrderShippingInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExportService {

    private final OrderShippingInfoRepository orderShippingInfoRepository;
    private final InventoryRepository inventoryRepository;
    private final OrderItemRepository orderItemRepository;

    public List<ExportOrderDto> getAllExportOrders() {
        List<OrderStatus> allowedStatuses = List.of(
                OrderStatus.PACKING,
                OrderStatus.DELIVERING,
                OrderStatus.DELIVERED,
                OrderStatus.CANCELLED);

        List<OrderShippingInfo> infos = orderShippingInfoRepository.findAllByOrder_StatusIn(allowedStatuses);

        return infos.stream()
                .peek(info -> {
                    // ✅ Chỉ trừ kho nếu đơn đang giao và chưa trừ
                    if (info.getOrder().getStatus() == OrderStatus.DELIVERING) {
                        updateInventoryWhenDelivering(info);
                    }
                })
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private ExportOrderDto mapToDto(OrderShippingInfo info) {
        return new ExportOrderDto(
                info.getId(),
                info.getOrder().getId(),
                info.getRecipientName(),
                info.getPhone(),
                info.getProvince(),
                info.getDistrict(),
                info.getWard(),
                info.getAddressDetail(),
                info.getOrder().getStatus());
    }

    private void updateInventoryWhenDelivering(OrderShippingInfo info) {
        Integer orderId = info.getOrder().getId();

        // ✅ Lấy danh sách mặt hàng trong đơn hàng
        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);

        for (OrderItem item : items) {
            Long medicineId = item.getMedicine().getId().longValue();
            int quantityToSubtract = item.getQuantity();

            // ✅ Tìm tất cả tồn kho của thuốc này
            List<Inventory> inventories = inventoryRepository.findAllByMedicineId(medicineId);
            if (inventories.isEmpty()) {
                throw new RuntimeException("Không tìm thấy tồn kho cho thuốc ID " + medicineId);
            }

            // ✅ Ưu tiên trừ từ lô đầu tiên (sắp hết hạn)
            Inventory inventory = inventories.get(0); // bạn có thể sắp xếp theo expiredAt nếu cần

            int newQuantity = inventory.getQuantity() - quantityToSubtract;
            if (newQuantity < 0) {
                throw new RuntimeException("Không đủ số lượng tồn kho cho thuốc ID " + medicineId);
            }

            inventory.setQuantity(newQuantity);
            inventoryRepository.save(inventory);
        }
    }

    // public ExportDetailResponse getExportDetailById(Integer exportId) {
    // OrderShippingInfo info = orderShippingInfoRepository.findById(exportId)
    // .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin xuất kho
    // với ID: " + exportId));

    // Order order = info.getOrder();

    // // 1. Lấy danh sách sản phẩm
    // List<OrderItem> items = orderItemRepository.findByOrder(order);
    // List<ExportItemDto> itemDtos = items.stream().map(item -> {
    // ExportItemDto dto = new ExportItemDto();
    // dto.setMedicineName(item.getMedicine().getName());
    // dto.setQuantity(item.getQuantity());
    // dto.setUnitPrice(item.getUnitPrice());
    // return dto;
    // }).toList();

    // // 2. Tính toán tổng hợp
    // double totalPrice = items.stream().mapToDouble(i -> i.getQuantity() *
    // i.getUnitPrice()).sum();
    // double discount = order.getDiscount() != null ? order.getDiscount() : 0;
    // double voucher = order.getVoucherDiscount() != null ?
    // order.getVoucherDiscount() : 0;
    // double shippingFee = order.getShippingDiscount() != null ?
    // order.getShippingDiscount() : 0;
    // double finalTotal = totalPrice - discount - voucher - shippingFee;

    // // 3. Đóng gói dữ liệu trả về
    // ExportDetailResponse response = new ExportDetailResponse();
    // response.setExportCode("PX-" + order.getId()); // hoặc lấy từ orderCode nếu
    // có
    // response.setExportDate(order.getOrderDate().toString());
    // response.setStatus(order.getStatus().toString());
    // response.setItems(itemDtos);

    // CustomerInfoDto customerDto = new CustomerInfoDto();
    // customerDto.setFullName(info.getRecipientName());
    // customerDto.setPhone(info.getPhone());
    // customerDto.setAddress(info.getFullAddress()); // custom getter nếu cần
    // customerDto.setNote(order.getNote()); // nếu có

    // response.setCustomerInfo(customerDto);

    // SummaryDto summary = new SummaryDto();
    // summary.setTotalPrice(totalPrice);
    // summary.setDiscount(discount);
    // summary.setVoucherDiscount(voucher);
    // summary.setShippingFee(shippingFee);
    // summary.setFinalTotal(finalTotal);

    // response.setSummary(summary);

    // return response;
    // }
}
