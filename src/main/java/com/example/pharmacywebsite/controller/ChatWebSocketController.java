package com.example.pharmacywebsite.controller;

import com.example.pharmacywebsite.dto.ChatMessageRequest;
import com.example.pharmacywebsite.service.ChatService;
import com.example.pharmacywebsite.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class ChatWebSocketController {

    private final ChatService chatService;

    public ChatWebSocketController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/chat.sendMessage")
    public void handleSendMessage(ChatMessageRequest req, Principal principal) {
        if (principal == null) {
            // Nếu tới đây mà vẫn null => client không gửi JWT / token sai
            throw new ApiException("WebSocket không xác thực", HttpStatus.UNAUTHORIZED);
        }

        String email = principal.getName(); // email từ JWT (gắn trong WebSocketJwtAuthChannelInterceptor)
        chatService.handleIncomingMessage(req, email);
    }
}
