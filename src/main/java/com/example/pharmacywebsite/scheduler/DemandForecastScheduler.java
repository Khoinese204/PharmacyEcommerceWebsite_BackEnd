package com.example.pharmacywebsite.scheduler;

import com.example.pharmacywebsite.domain.Medicine;
import com.example.pharmacywebsite.dto.ForecastResultList;
import com.example.pharmacywebsite.enums.InventoryStatus;
import com.example.pharmacywebsite.repository.InventoryRepository;
import com.example.pharmacywebsite.repository.MedicineRepository;
import com.example.pharmacywebsite.repository.OrderItemRepository;
import com.example.pharmacywebsite.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DemandForecastScheduler {

    private final MedicineRepository medicineRepository;
    private final OrderItemRepository orderItemRepository;
    private final InventoryRepository inventoryRepository;
    private final GeminiService geminiService;

    @Scheduled(cron = "0 0 2 * * ?") 
    public synchronized void runForecast() {
        System.out.println("🤖 AI bắt đầu dự báo (Chế độ Batch)...");
        
        List<Medicine> medicines = medicineRepository.findAll();
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        List<InventoryStatus> activeStatuses = Arrays.asList(InventoryStatus.AVAILABLE, InventoryStatus.LOW_STOCK);

        // 1. Chuẩn bị dữ liệu gửi đi (List các Map)
        List<Map<String, Object>> payload = new ArrayList<>();

        for (Medicine m : medicines) {
            Integer soldQty = orderItemRepository.countSoldMedicineSince(m.getId(), thirtyDaysAgo);
            Integer currentStock = inventoryRepository.countTotalStockByMedicine(m.getId(), activeStatuses);
            
            Map<String, Object> item = new HashMap<>();
            item.put("medicineId", m.getId());
            item.put("name", m.getName());
            item.put("currentStock", currentStock);
            item.put("soldLast30Days", soldQty);
            payload.add(item);
        }

        if (payload.isEmpty()) return;

        // 2. Gửi 1 lần duy nhất (Batch Call)
        // Nếu danh sách quá lớn (>50 thuốc), bạn có thể chia nhỏ (chunk) thành các gói 30 thuốc/lần
        ForecastResultList results = geminiService.getBatchForecast(payload);

        // 3. Map kết quả trả về vào DB
        if (results.getResults() != null) {
            Map<Integer, ForecastResultList.Item> resultMap = results.getResults().stream()
                .collect(Collectors.toMap(ForecastResultList.Item::getMedicineId, item -> item));

            for (Medicine m : medicines) {
                ForecastResultList.Item prediction = resultMap.get(m.getId());
                if (prediction != null) {
                    m.setForecastStatus(prediction.getStatus());
                    m.setAiInsight(prediction.getReason());
                    medicineRepository.save(m);
                }
            }
        }
        
        System.out.println("✅ Hoàn tất dự báo cho " + medicines.size() + " thuốc.");
    }
}