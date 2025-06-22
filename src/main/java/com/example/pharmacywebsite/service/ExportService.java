package com.example.pharmacywebsite.service;

import com.example.pharmacywebsite.domain.OrderShippingInfo;
import com.example.pharmacywebsite.dto.ExportOrderDto;
import com.example.pharmacywebsite.enums.OrderStatus;
import com.example.pharmacywebsite.repository.OrderShippingInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExportService {

    private final OrderShippingInfoRepository orderShippingInfoRepository;

    public List<ExportOrderDto> getAllExportOrders() {
        List<OrderShippingInfo> infos = orderShippingInfoRepository.findAll();
        return infos.stream()
                .filter(info -> {
                    OrderStatus status = info.getOrder().getStatus();
                    return status == OrderStatus.PACKING ||
                            status == OrderStatus.DELIVERING ||
                            status == OrderStatus.DELIVERED ||
                            status == OrderStatus.CANCELLED;
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
}
