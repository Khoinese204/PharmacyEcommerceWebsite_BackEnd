package com.example.pharmacywebsite.controller;

import com.example.pharmacywebsite.domain.User;
import com.example.pharmacywebsite.dto.ChatMessageResponse;
import com.example.pharmacywebsite.dto.ChatRoomResponse;
import com.example.pharmacywebsite.service.ChatRoomService;
import com.example.pharmacywebsite.service.ChatService;
import com.example.pharmacywebsite.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatRestController {

    private final ChatRoomService chatRoomService;
    private final ChatService chatService;
    private final JwtService jwtService;

    public ChatRestController(ChatRoomService chatRoomService,
            ChatService chatService,
            JwtService jwtService) {
        this.chatRoomService = chatRoomService;
        this.chatService = chatService;
        this.jwtService = jwtService;
    }

    // Khách hàng bấm "Tư vấn với dược sĩ" (role CUSTOMER)
    @PostMapping("/start")
    public ResponseEntity<ChatRoomResponse> startChat(Authentication authentication) {
        String email = authentication.getName(); // từ JWT HTTP
        ChatRoomResponse room = chatRoomService.startChat(email);
        return ResponseEntity.ok(room);
    }

    // FE load lịch sử tin nhắn cho room cụ thể (customer/pharmacist/admin)
    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<List<ChatMessageResponse>> getMessages(
            @PathVariable("roomId") Long roomId,
            HttpServletRequest request) {
        // Lấy user hiện tại từ JWT trong header Authorization
        User currentUser = jwtService.extractUserFromRequest(request);

        List<ChatMessageResponse> messages = chatService.getMessagesForUser(roomId, currentUser);

        return ResponseEntity.ok(messages);
    }
}
