package com.example.pharmacywebsite.service;

import com.example.pharmacywebsite.dto.ForecastResultList;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Dùng model 2.5 Flash Lite (Theo danh sách bạn gửi)
    // Model này hỗ trợ context window lớn, rất hợp để gửi list dài
    private final String URL_TEMPLATE = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-lite:generateContent?key=%s";

    public ForecastResultList getBatchForecast(List<Map<String, Object>> medicineDataList) {
        try {
            // 1. Convert list thuốc sang JSON String để nhét vào prompt
            String dataJson = objectMapper.writeValueAsString(medicineDataList);

            String currentMonth = java.time.LocalDate.now().getMonth().toString();

            // 2. Tạo Prompt (Yêu cầu trả về đúng cấu trúc JSON)
            String prompt = String.format(
                    "Bạn là chuyên gia kho dược tại Việt Nam. Hiện tại đang là tháng: %s. \n" +
                            "Hãy phân tích danh sách thuốc dưới đây để dự báo nhu cầu 7 ngày tới.\n" +
                            "QUY TẮC SUY LUẬN BỔ SUNG (SEASONAL & WEATHER):\n" +
                            "1. Nếu là mùa Đông/Xuân (Tháng 11-4): Ưu tiên TRENDING cho thuốc ho, cảm cúm, vitamin tăng đề kháng.\n"
                            +
                            "2. Nếu là mùa Hè (Tháng 5-8): Ưu tiên TRENDING cho kem chống nắng, điện giải, tiêu hóa.\n"
                            +
                            "3. Nếu thuốc có 'soldLast30Days' cao -> TRENDING.\n" +
                            "4. Nếu thuốc phù hợp mùa vụ hiện tại nhưng tồn kho thấp -> Báo động ĐỎ (LOW_STOCK).\n\n" +
                            "Dữ liệu đầu vào: %s\n\n" +
                            "OUTPUT JSON (no markdown): { \"results\": [ { \"medicineId\": int, \"predictedSales\": int, \"status\": \"LOW_STOCK\"/\"NORMAL\"/\"OVERSTOCK\"/\"TRENDING_SEASON\", \"reason\": \"<lý do ngắn gọn, nhắc đến yếu tố mùa vụ nếu có>\" } ] }",
                    currentMonth, dataJson);

            // 3. Gọi API
            String url = String.format(URL_TEMPLATE, apiKey);
            Map<String, Object> requestBody = Map.of(
                    "contents", new Object[] { Map.of("parts", new Object[] { Map.of("text", prompt) }) });

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            System.out.println("🚀 Đang gửi Batch Request cho " + medicineDataList.size() + " thuốc...");
            String response = restTemplate.postForObject(url, entity, String.class);

            // 4. Parse kết quả
            JsonNode root = objectMapper.readTree(response);
            String textResult = root.path("candidates").get(0).path("content").path("parts").get(0).path("text")
                    .asText();

            // Lọc bỏ markdown
            if (textResult.contains("```json"))
                textResult = textResult.replace("```json", "").replace("```", "");
            if (textResult.contains("{"))
                textResult = textResult.substring(textResult.indexOf("{"), textResult.lastIndexOf("}") + 1);

            return objectMapper.readValue(textResult.trim(), ForecastResultList.class);

        } catch (Exception e) {
            System.err.println("❌ Lỗi Batch AI: " + e.getMessage());
            e.printStackTrace();
            return new ForecastResultList();
        }
    }
}