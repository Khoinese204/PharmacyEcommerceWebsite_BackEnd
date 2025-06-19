// package com.example.pharmacywebsite.designpattern.CoR;

// import com.example.pharmacywebsite.service.PaymentService;
// import lombok.RequiredArgsConstructor;
// import org.springframework.stereotype.Service;

// @Service
// @RequiredArgsConstructor
// public class PaymentVerificationHandler extends OrderHandler {

//     private final PaymentService paymentService;

//     @Override
//     public void handle(OrderContext context) {
//         // Nếu cần xác thực thì gọi ở đây (ví dụ nếu bạn đã triển khai verify logic)
//         // paymentService.verify(context.getDto().getPaymentInfo());

//         // Gọi handler tiếp theo nếu có
//         if (next != null)
//             next.handle(context);
//     }
// }
