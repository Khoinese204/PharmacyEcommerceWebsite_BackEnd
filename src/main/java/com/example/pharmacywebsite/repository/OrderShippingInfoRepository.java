package com.example.pharmacywebsite.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pharmacywebsite.domain.Order;
import com.example.pharmacywebsite.domain.OrderShippingInfo;
import com.example.pharmacywebsite.enums.OrderStatus;

public interface OrderShippingInfoRepository extends JpaRepository<OrderShippingInfo, Integer> {

    OrderShippingInfo findByOrder(Order order);

    OrderShippingInfo findByOrderId(Integer orderId);

    List<OrderShippingInfo> findAllByOrder(Order order);

    List<OrderShippingInfo> findAllByOrderId(Integer orderId);

    List<OrderShippingInfo> findAllByOrder_StatusIn(List<OrderStatus> statuses);
}