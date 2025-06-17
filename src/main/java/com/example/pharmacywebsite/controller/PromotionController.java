package com.example.pharmacywebsite.controller;

import com.example.pharmacywebsite.designpattern.Strategy.CouponCodeFixedAmountStrategy;
import com.example.pharmacywebsite.designpattern.Strategy.DiscountContext;
import com.example.pharmacywebsite.designpattern.Strategy.DiscountStrategy;
import com.example.pharmacywebsite.designpattern.Strategy.QuantityBasedCategoryDiscountStrategy;
import com.example.pharmacywebsite.designpattern.Strategy.ShippingDiscountStrategy;
import com.example.pharmacywebsite.designpattern.Strategy.ThresholdFixedAmountDiscountStrategy;

import com.example.pharmacywebsite.domain.Promotion;
import com.example.pharmacywebsite.dto.ApplyPromotionRequest;
import com.example.pharmacywebsite.dto.ApplyPromotionResponse;
import com.example.pharmacywebsite.dto.PromotionDTO;
import com.example.pharmacywebsite.repository.MedicineRepository;
import com.example.pharmacywebsite.repository.PromotionRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionRepository promotionRepo;
    private final MedicineRepository medicineRepo;

    /**
     * Lấy danh sách mã khuyến mãi còn hiệu lực tại thời điểm hiện tại
     */
    @GetMapping
    public List<PromotionDTO> getAllValidPromotions() {
        LocalDate today = LocalDate.now();
        return promotionRepo
                .findByStartDateLessThanEqualAndEndDateGreaterThanEqual(today, today)
                .stream()
                .map(p -> new PromotionDTO(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        p.getDiscountPercent(),
                        p.getApplicableCategory() != null ? p.getApplicableCategory().getId() : null))
                .collect(Collectors.toList());
    }

    /**
     * Áp dụng một mã khuyến mãi cụ thể lên danh sách sản phẩm trong giỏ
     */
    @PostMapping("/apply")
    public ApplyPromotionResponse applyPromotion(@RequestBody ApplyPromotionRequest req) {
        Promotion promo = promotionRepo.findById(req.getPromotionId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mã giảm giá không tồn tại"));

        if (req.getSelectedItems() == null || req.getSelectedItems().isEmpty()) {
            return new ApplyPromotionResponse(false, 0, "Không có sản phẩm nào được chọn");
        }

        DiscountStrategy strategy;

        String name = promo.getName().toLowerCase();

        if (promo.getApplicableCategory() != null) {
            // Loại 3: giảm theo danh mục (p3a–p3c)
            strategy = new QuantityBasedCategoryDiscountStrategy(
                    promo.getApplicableCategory().getId(), 2, 30000);
        } else if (name.contains("coupon2024")) {
            strategy = new CouponCodeFixedAmountStrategy(40000);
        } else if (name.contains("coupon2025")) {
            strategy = new CouponCodeFixedAmountStrategy(50000);
        } else if (name.contains("coupon2026")) {
            strategy = new CouponCodeFixedAmountStrategy(60000);
        } else if (name.contains("vanchuyen20k")) {
            strategy = new ShippingDiscountStrategy(300000, 20000);
        } else if (name.contains("vanchuyen30k")) {
            strategy = new ShippingDiscountStrategy(400000, 30000);
        } else if (name.contains("vanchuyen50k")) {
            strategy = new ShippingDiscountStrategy(600000, 50000);
        } else if (name.contains("500k")) {
            strategy = new ThresholdFixedAmountDiscountStrategy(500000, 50000);
        } else if (name.contains("600k")) {
            strategy = new ThresholdFixedAmountDiscountStrategy(600000, 60000);
        } else if (name.contains("700k")) {
            strategy = new ThresholdFixedAmountDiscountStrategy(700000, 70000);
        } else {
            return new ApplyPromotionResponse(false, 0, "Không xác định được loại mã giảm giá");
        }

        DiscountContext context = new DiscountContext(strategy, medicineRepo);
        double discountAmount = context.applyDiscount(req.getSelectedItems());

        if (discountAmount <= 0) {
            return new ApplyPromotionResponse(false, 0, "Mã giảm giá này không áp dụng cho các sản phẩm đã chọn");
        }

        return new ApplyPromotionResponse(true, discountAmount, "Áp dụng thành công");
    }

}
