package com.example.pharmacywebsite.dto;

import java.time.LocalDateTime;

import com.example.pharmacywebsite.domain.ChatMessage;
import com.example.pharmacywebsite.enums.ChatSenderRole;

public class ChatMessageResponse {

    private Long id;
    private Long roomId;
    private Integer senderId;
    private String senderName;
    private ChatSenderRole senderRole;
    private String content;
    private LocalDateTime sentAt;

    public static ChatMessageResponse fromEntity(ChatMessage m) {
        ChatMessageResponse dto = new ChatMessageResponse();
        dto.id = m.getId();
        dto.roomId = m.getRoom().getId();
        dto.senderId = m.getSender().getId();
        dto.senderName = m.getSender().getFullName();
        dto.senderRole = m.getSenderRole();
        dto.content = m.getContent();
        dto.sentAt = m.getSentAt();
        return dto;
    }

    // getter ...

    public Long getId() {
        return id;
    }

    public Long getRoomId() {
        return roomId;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public ChatSenderRole getSenderRole() {
        return senderRole;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }
}