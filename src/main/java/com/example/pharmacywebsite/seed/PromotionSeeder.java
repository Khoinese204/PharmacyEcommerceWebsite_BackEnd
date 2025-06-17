package com.example.pharmacywebsite.seed;

import com.example.pharmacywebsite.domain.Category;
import com.example.pharmacywebsite.domain.Promotion;
import com.example.pharmacywebsite.repository.CategoryRepository;
import com.example.pharmacywebsite.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Order(5) // 👈 Chạy sau MedicineSeeder
@RequiredArgsConstructor
public class PromotionSeeder implements CommandLineRunner {

    private final PromotionRepository promotionRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) {
        if (promotionRepository.count() == 0) {
            LocalDate today = LocalDate.now();
            LocalDate end = today.plusDays(30);

            // 1. Giảm 50k nếu đơn hàng từ 500k
            Promotion p1a = new Promotion();
            p1a.setName("Giảm 50k cho đơn hàng từ 500k");
            p1a.setDescription("Áp dụng cho tất cả sản phẩm");
            p1a.setDiscountPercent(0.0);
            p1a.setStartDate(today);
            p1a.setEndDate(end);
            p1a.setApplicableCategory(null);
            promotionRepository.save(p1a);

            Promotion p1b = new Promotion();
            p1b.setName("Giảm 60k cho đơn hàng từ 600k");
            p1b.setDescription("Áp dụng cho tất cả sản phẩm");
            p1b.setDiscountPercent(0.0);
            p1b.setStartDate(today);
            p1b.setEndDate(end);
            p1b.setApplicableCategory(null);
            promotionRepository.save(p1b);

            Promotion p1c = new Promotion();
            p1c.setName("Giảm 70k cho đơn hàng từ 700k");
            p1c.setDescription("Áp dụng cho tất cả sản phẩm");
            p1c.setDiscountPercent(0.0);
            p1c.setStartDate(today);
            p1c.setEndDate(end);
            p1c.setApplicableCategory(null);
            promotionRepository.save(p1c);

            // 2. Coupon cố định
            Promotion p2a = new Promotion();
            p2a.setName("Coupon2024");
            p2a.setDescription("Mã giảm giá cố định 40.000đ");
            p2a.setDiscountPercent(0.0);
            p2a.setStartDate(today);
            p2a.setEndDate(end);
            p2a.setApplicableCategory(null);
            promotionRepository.save(p2a);

            Promotion p2b = new Promotion();
            p2b.setName("Coupon2025");
            p2b.setDescription("Mã giảm giá cố định 50.000đ");
            p2b.setDiscountPercent(0.0);
            p2b.setStartDate(today);
            p2b.setEndDate(end);
            p2b.setApplicableCategory(null);
            promotionRepository.save(p2b);

            Promotion p2c = new Promotion();
            p2c.setName("Coupon2026");
            p2c.setDescription("Mã giảm giá cố định 60.000đ");
            p2c.setDiscountPercent(0.0);
            p2c.setStartDate(today);
            p2c.setEndDate(end);
            p2c.setApplicableCategory(null);
            promotionRepository.save(p2c);

            // 3a. Giảm 30k khi mua từ 2 sản phẩm danh mục thuốc
            Category thuoc = categoryRepository.findById(1).orElse(null);
            if (thuoc != null) {
                Promotion p3a = new Promotion();
                p3a.setName("DanhMucThuoc30k");
                p3a.setDescription("Giảm 30k khi mua từ 2 sản phẩm danh mục thuốc");
                p3a.setDiscountPercent(0.0);
                p3a.setStartDate(LocalDate.of(2025, 6, 17));
                p3a.setEndDate(LocalDate.of(2025, 7, 17));
                p3a.setApplicableCategory(thuoc);
                promotionRepository.save(p3a);
            }

            // 3b. Giảm 30k khi mua từ 2 sản phẩm danh mục vitamin
            Category vitamin = categoryRepository.findById(2).orElse(null);
            if (vitamin != null) {
                Promotion p3b = new Promotion();
                p3b.setName("DanhMucThucPhamChucNang30k");
                p3b.setDescription("Giảm 30k khi mua từ 2 sản phẩm danh mục thực phẩm chức năng");
                p3b.setDiscountPercent(0.0);
                p3b.setStartDate(LocalDate.of(2025, 6, 17));
                p3b.setEndDate(LocalDate.of(2025, 7, 17));
                p3b.setApplicableCategory(vitamin);
                promotionRepository.save(p3b);
            }

            // 3c. Giảm 30k khi mua từ 2 sản phẩm danh mục thực phẩm chức năng
            Category tpcn = categoryRepository.findById(3).orElse(null);
            if (tpcn != null) {
                Promotion p3c = new Promotion();
                p3c.setName("DanhMucChamSocCaNhan30k");
                p3c.setDescription("Giảm 30k khi mua từ 2 sản phẩm danh mục chăm sóc cá nhân");
                p3c.setDiscountPercent(0.0);
                p3c.setStartDate(LocalDate.of(2025, 6, 17));
                p3c.setEndDate(LocalDate.of(2025, 7, 17));
                p3c.setApplicableCategory(tpcn);
                promotionRepository.save(p3c);
            }

            // 4–6. Mã giảm giá phí vận chuyển
            Promotion p4a = new Promotion();
            p4a.setName("VanChuyen20k");
            p4a.setDescription("Giảm 20k phí vận chuyển cho đơn từ 300k");
            p4a.setDiscountPercent(0.0);
            p4a.setStartDate(today);
            p4a.setEndDate(end);
            p4a.setApplicableCategory(null);
            promotionRepository.save(p4a);

            Promotion p4b = new Promotion();
            p4b.setName("VanChuyen30k");
            p4b.setDescription("Giảm 30k phí vận chuyển cho đơn từ 400k");
            p4b.setDiscountPercent(0.0);
            p4b.setStartDate(today);
            p4b.setEndDate(end);
            p4b.setApplicableCategory(null);
            promotionRepository.save(p4b);

            Promotion p4c = new Promotion();
            p4c.setName("VanChuyen50k");
            p4c.setDescription("Giảm 50k phí vận chuyển cho đơn từ 600k");
            p4c.setDiscountPercent(0.0);
            p4c.setStartDate(today);
            p4c.setEndDate(end);
            p4c.setApplicableCategory(null);
            promotionRepository.save(p4c);

            System.out.println("✅ Promotion seed completed");
        }
    }
}
