package com.example.pharmacywebsite.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiChatService {

    private final ChatClient chatClient;

    // 1) Không có knowledge base → cho AI nói tự do
    public String ask(String question) {
        String prompt = """
                Bạn là chatbot PrimeCare của nhà thuốc.

                - Trả lời thân thiện, dễ hiểu, ngắn gọn bằng tiếng Việt.
                - Nếu người dùng chỉ chào hỏi (vd: "hello", "hi"), hãy chào lại và hỏi xem họ cần hỗ trợ gì.
                - Nếu câu hỏi liên quan thuốc/sức khỏe, chỉ trả lời ở mức độ tư vấn chung, luôn nhắc người dùng nên hỏi trực tiếp bác sĩ / dược sĩ.

                Câu hỏi: %s
                """
                .formatted(question);

        return chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();
    }

    // 2) Có knowledge base nội bộ → ép dùng dữ liệu nội bộ
    public String askWithKnowledge(String question, String knowledgeBase) {

        String prompt = """
                Bạn là Chatbot PrimeCare – trợ lý tư vấn của nhà thuốc.

                Nhiệm vụ:
                1. Chỉ được dùng dữ liệu nội bộ bên dưới để trả lời.
                2. Không được tự bịa, không được suy đoán.
                3. Không được trả lời dựa vào kiến thức y tế bên ngoài.
                4. Nếu dữ liệu không đủ → trả lời đúng y chang câu sau:
                   "Hệ thống chưa có dữ liệu cho câu hỏi này. Vui lòng liên hệ dược sĩ."

                ---- DỮ LIỆU NỘI BỘ ----
                %s
                -------------------------

                Câu hỏi của khách hàng:
                "%s"

                Hãy trả lời ngắn gọn, chính xác và không thêm thông tin ngoài dữ liệu trên.
                """.formatted(knowledgeBase, question);

        return chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();
    }
}