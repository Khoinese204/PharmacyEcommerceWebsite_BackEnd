package com.example.pharmacywebsite.designpattern.CoR;

import org.springframework.stereotype.Service;

@Service
public class PromotionApplyHandler extends OrderHandler {

    @Override
    public void handle(OrderContext context) {
        // Hiện chưa có logic khuyến mãi, đây là nơi áp dụng giảm giá nếu cần
        // Ví dụ: giảm giá theo tổng đơn, khách hàng VIP, mã giảm giá...

        if (next != null)
            next.handle(context);
    }
}
