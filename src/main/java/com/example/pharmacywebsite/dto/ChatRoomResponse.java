package com.example.pharmacywebsite.dto;

import java.time.LocalDateTime;

import com.example.pharmacywebsite.domain.ChatRoom;
import com.example.pharmacywebsite.enums.ChatRoomStatus;

public class ChatRoomResponse {

    private Long id;
    private String customerName;
    private String pharmacistName;
    private ChatRoomStatus status;
    private LocalDateTime createdAt;

    public static ChatRoomResponse fromEntity(ChatRoom room) {
        ChatRoomResponse dto = new ChatRoomResponse();
        dto.id = room.getId();
        dto.customerName = room.getCustomer().getFullName();
        dto.pharmacistName = room.getPharmacist() != null ? room.getPharmacist().getFullName() : null;
        dto.status = room.getStatus();
        dto.createdAt = room.getCreatedAt();
        return dto;
    }

    // getter ...

    public Long getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getPharmacistName() {
        return pharmacistName;
    }

    public ChatRoomStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}