package com.example.pharmacywebsite.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pharmacywebsite.domain.PaymentTransaction;
import com.example.pharmacywebsite.dto.PaymentConfirmRequest;
import com.example.pharmacywebsite.dto.PaymentInitiateRequest;
import com.example.pharmacywebsite.dto.PaymentResponse;
import com.example.pharmacywebsite.service.PaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/initiate")
    public ResponseEntity<PaymentResponse> initiatePayment(@RequestBody PaymentInitiateRequest request) {
        PaymentTransaction transaction = paymentService.initiatePayment(request);
        return ResponseEntity.ok(convertToResponse(transaction));
    }

    @PostMapping("/confirm")
    public ResponseEntity<PaymentResponse> confirmPayment(@RequestBody PaymentConfirmRequest request) {
        PaymentTransaction transaction = paymentService.confirmPayment(request);
        return ResponseEntity.ok(convertToResponse(transaction));
    }

    @GetMapping("/history")
    public ResponseEntity<List<PaymentResponse>> getHistory(@RequestParam Integer userId) {
        return ResponseEntity.ok(paymentService.getPaymentHistory(userId));
    }

    private PaymentResponse convertToResponse(PaymentTransaction tx) {
        PaymentResponse res = new PaymentResponse();
        res.setTransactionId(tx.getId());
        res.setOrderId(tx.getOrder().getId());
        res.setAmount(tx.getAmount());
        res.setStatus(tx.getStatus());
        res.setPaidAt(tx.getPaidAt());
        res.setPaymentMethod(tx.getPaymentMethod());
        return res;
    }
}
