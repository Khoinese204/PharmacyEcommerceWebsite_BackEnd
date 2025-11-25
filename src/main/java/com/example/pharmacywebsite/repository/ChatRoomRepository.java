package com.example.pharmacywebsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pharmacywebsite.domain.ChatRoom;
import com.example.pharmacywebsite.domain.User;
import com.example.pharmacywebsite.enums.ChatRoomStatus;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findFirstByCustomerAndStatusOrderByCreatedAtDesc(User customer, ChatRoomStatus status);

    List<ChatRoom> findByPharmacistAndStatusOrderByCreatedAtDesc(User pharmacist, ChatRoomStatus status);

    List<ChatRoom> findByPharmacistOrderByCreatedAtDesc(User pharmacist);

    List<ChatRoom> findByStatusOrderByCreatedAtDesc(ChatRoomStatus status);
}
