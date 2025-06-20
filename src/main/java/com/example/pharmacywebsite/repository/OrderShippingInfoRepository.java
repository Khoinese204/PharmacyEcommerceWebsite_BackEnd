package com.example.pharmacywebsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pharmacywebsite.domain.Order;
import com.example.pharmacywebsite.domain.OrderShippingInfo;

public interface OrderShippingInfoRepository extends JpaRepository<OrderShippingInfo, Integer> {

    OrderShippingInfo findByOrder(Order order);

    OrderShippingInfo findByOrderId(Integer orderId);
}