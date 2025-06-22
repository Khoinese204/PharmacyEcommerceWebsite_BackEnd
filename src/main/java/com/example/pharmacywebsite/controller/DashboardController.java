package com.example.pharmacywebsite.controller;

import com.example.pharmacywebsite.domain.Inventory;
import com.example.pharmacywebsite.domain.PaymentTransaction;
import com.example.pharmacywebsite.dto.DailyCountDto;
import com.example.pharmacywebsite.dto.DashboardSummaryDto;
import com.example.pharmacywebsite.dto.LowStockDto;
import com.example.pharmacywebsite.dto.RevenueChartDto;
import com.example.pharmacywebsite.enums.PaymentStatus;
import com.example.pharmacywebsite.repository.InventoryRepository;
import com.example.pharmacywebsite.repository.OrderRepository;
import com.example.pharmacywebsite.repository.PaymentTransactionRepository;
import com.example.pharmacywebsite.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.*;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final OrderRepository orderRepo;
    private final UserRepository userRepo;
    private final InventoryRepository inventoryRepo;
    private final PaymentTransactionRepository paymentTransactionRepository;

    @GetMapping("/summary")
    public DashboardSummaryDto getSummary(
            @RequestParam("month") int month,
            @RequestParam("year") int year) {

        // Xác định khoảng thời gian trong tháng
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        // ✅ Lấy doanh thu từ giao dịch thanh toán thành công
        Double totalRevenue = paymentTransactionRepository
                .sumAmountByCreatedAtBetweenAndStatus(startDateTime, endDateTime, PaymentStatus.SUCCESS);
        long totalOrders = orderRepo.countByOrderDateBetween(startDateTime, endDateTime);
        long totalCustomers = userRepo.countByRole_Name("Customer");
        long lowStockCount = inventoryRepo.countByQuantityLessThanEqual(20);

        return new DashboardSummaryDto(
                totalRevenue != null ? totalRevenue : 0.0,
                totalOrders,
                totalCustomers,
                lowStockCount);
    }

    @GetMapping("/revenue-chart")
    public List<RevenueChartDto> getRevenueChart(
            @RequestParam("month") int month,
            @RequestParam("year") int year) {

        List<RevenueChartDto> chartData = new ArrayList<>();
        LocalDate startDate = LocalDate.of(year, month, 1);
        int daysInMonth = startDate.lengthOfMonth();

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = startDate.withDayOfMonth(day);
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.atTime(LocalTime.MAX);

            // ✅ Chỉ tính doanh thu từ giao dịch đã SUCCESS
            Double revenue = paymentTransactionRepository
                    .sumAmountByCreatedAtBetweenAndStatus(start, end, PaymentStatus.SUCCESS);

            chartData.add(new RevenueChartDto(day, revenue != null ? revenue : 0.0));
        }

        return chartData;
    }

    @GetMapping("/orders-chart")
    public List<DailyCountDto> getOrdersChart(
            @RequestParam("month") int month,
            @RequestParam("year") int year) {

        List<DailyCountDto> chartData = new ArrayList<>();
        LocalDate startDate = LocalDate.of(year, month, 1);
        int daysInMonth = startDate.lengthOfMonth();

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDateTime start = startDate.withDayOfMonth(day).atStartOfDay();
            LocalDateTime end = start.toLocalDate().atTime(LocalTime.MAX);

            long count = orderRepo.countByOrderDateBetween(start, end);
            chartData.add(new DailyCountDto(day, count));
        }

        return chartData;
    }

    @GetMapping("/customers-chart")
    public List<DailyCountDto> getCustomersChart(
            @RequestParam("month") int month,
            @RequestParam("year") int year) {

        List<DailyCountDto> chartData = new ArrayList<>();
        LocalDate startDate = LocalDate.of(year, month, 1);
        int daysInMonth = startDate.lengthOfMonth();

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDateTime start = startDate.withDayOfMonth(day).atStartOfDay();
            LocalDateTime end = start.toLocalDate().atTime(LocalTime.MAX);

            long count = userRepo.countByCreatedAtBetweenAndRole_Name(start, end, "Customer");
            chartData.add(new DailyCountDto(day, count));
        }

        return chartData;
    }

    @GetMapping("/low-stock-chart")
    public List<LowStockDto> getLowStockChart() {
        List<Inventory> allInventories = inventoryRepo.findAll(); // lấy tất cả tồn kho

        // Gộp theo tên thuốc -> tổng số lượng
        Map<String, Integer> medicineMap = new HashMap<>();
        for (Inventory inv : allInventories) {
            String medicineName = inv.getMedicine().getName();
            medicineMap.put(medicineName, medicineMap.getOrDefault(medicineName, 0) + inv.getQuantity());
        }

        // Chỉ lấy những thuốc có tổng tồn kho <= 20
        return medicineMap.entrySet().stream()
                .filter(entry -> entry.getValue() <= 20)
                .map(entry -> new LowStockDto(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparingInt(LowStockDto::getQuantity))
                .toList();
    }

}
