package com.example.pharmacywebsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pharmacywebsite.domain.ChatRoom;
import com.example.pharmacywebsite.domain.User;
import com.example.pharmacywebsite.enums.ChatRoomStatus;
import com.example.pharmacywebsite.enums.ChatRoomType;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findFirstByCustomerAndStatusOrderByCreatedAtDesc(User customer, ChatRoomStatus status);

    List<ChatRoom> findByPharmacistAndStatusOrderByCreatedAtDesc(User pharmacist, ChatRoomStatus status);

    List<ChatRoom> findByPharmacistOrderByCreatedAtDesc(User pharmacist);

    List<ChatRoom> findByStatusOrderByCreatedAtDesc(ChatRoomStatus status);

    List<ChatRoom> findByTypeAndStatus(ChatRoomType type, String status);

    List<ChatRoom> findByCustomerId(Long customerId);

    Optional<ChatRoom> findFirstByCustomerAndTypeAndStatusOrderByCreatedAtDesc(
            User customer, ChatRoomType type, ChatRoomStatus status);

    List<ChatRoom> findByTypeAndStatusOrderByCreatedAtDesc(ChatRoomType type, ChatRoomStatus status);

    List<ChatRoom> findByCustomerIdOrderByCreatedAtDesc(Integer customerId);
}
