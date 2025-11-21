package com.example.pharmacywebsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pharmacywebsite.domain.ChatMessage;
import com.example.pharmacywebsite.domain.ChatRoom;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByRoomOrderBySentAtAsc(ChatRoom room);
}