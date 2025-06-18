package com.example.pharmacywebsite.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.pharmacywebsite.designpattern.FactoryMethod.PaymentFactory;
import com.example.pharmacywebsite.designpattern.FactoryMethod.PaymentFactoryProvider;
import com.example.pharmacywebsite.domain.Order;
import com.example.pharmacywebsite.domain.PaymentTransaction;
import com.example.pharmacywebsite.dto.PaymentConfirmRequest;
import com.example.pharmacywebsite.dto.PaymentInitiateRequest;
import com.example.pharmacywebsite.dto.PaymentResponse;
import com.example.pharmacywebsite.enums.PaymentStatus;
import com.example.pharmacywebsite.repository.OrderRepository;
import com.example.pharmacywebsite.repository.PaymentTransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentTransactionRepository paymentRepo;
    private final OrderRepository orderRepo;
    private final PaymentFactoryProvider factoryProvider;

    public PaymentTransaction initiatePayment(PaymentInitiateRequest request) {
        Order order = orderRepo.findById(request.getOrderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Không tìm thấy đơn hàng với ID: " + request.getOrderId()));

        PaymentFactory factory = factoryProvider.getFactory(request.getPaymentMethod());
        PaymentTransaction transaction = factory.createTransaction(order, request.getAmount());

        return paymentRepo.save(transaction);
    }

    public PaymentTransaction confirmPayment(PaymentConfirmRequest request) {
        PaymentTransaction transaction = paymentRepo.findById(request.getTransactionId())
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        transaction.setProviderTransactionId(request.getProviderTransactionId());
        transaction.setStatus(PaymentStatus.SUCCESS);
        transaction.setPaidAt(LocalDateTime.now());

        return paymentRepo.save(transaction);
    }

    public List<PaymentResponse> getPaymentHistory(Integer userId) {
        return paymentRepo.findByOrder_User_Id(userId).stream().map(tx -> {
            PaymentResponse res = new PaymentResponse();
            res.setTransactionId(tx.getId());
            res.setOrderId(tx.getOrder().getId());
            res.setAmount(tx.getAmount());
            res.setStatus(tx.getStatus());
            res.setPaidAt(tx.getPaidAt());
            res.setPaymentMethod(tx.getPaymentMethod());
            return res;
        }).toList();
    }
}
