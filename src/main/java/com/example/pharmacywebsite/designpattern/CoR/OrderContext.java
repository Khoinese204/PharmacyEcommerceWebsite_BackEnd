package com.example.pharmacywebsite.designpattern.CoR;

import com.example.pharmacywebsite.domain.Order;
import com.example.pharmacywebsite.domain.User;
import com.example.pharmacywebsite.dto.CartDto;
import com.example.pharmacywebsite.dto.CreateOrderDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderContext {
    private Order order;
    private CreateOrderDTO dto;
    private CartDto cart;
    private User user;

}
