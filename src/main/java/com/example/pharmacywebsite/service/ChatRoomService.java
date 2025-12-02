package com.example.pharmacywebsite.service;

import com.example.pharmacywebsite.domain.ChatRoom;
import com.example.pharmacywebsite.domain.User;
import com.example.pharmacywebsite.dto.ChatRoomResponse;
import com.example.pharmacywebsite.enums.ChatRoomStatus;
import com.example.pharmacywebsite.enums.ChatRoomType;
import com.example.pharmacywebsite.exception.ApiException;
import com.example.pharmacywebsite.repository.ChatRoomRepository;
import com.example.pharmacywebsite.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    // private final SupportStatusService supportStatusService;

    public ChatRoomService(ChatRoomRepository chatRoomRepository,
            UserRepository userRepository,
            SupportStatusService supportStatusService) {
        this.chatRoomRepository = chatRoomRepository;
        this.userRepository = userRepository;
        // this.supportStatusService = supportStatusService;
    }

    /**
     * Khách start chat:
     * - Nếu đã có room OPEN của chính khách đó => dùng lại.
     * - Nếu chưa có => tạo room mới và gán một pharmacist đang online.
     */
    @Transactional
    public ChatRoomResponse startChat(String customerEmail, ChatRoomType type) {
        User customer = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Nếu đã có room OPEN của khách thì dùng lại
        var existing = chatRoomRepository
                .findFirstByCustomerAndTypeAndStatusOrderByCreatedAtDesc(
                        customer, type, ChatRoomStatus.OPEN);

        if (existing.isPresent()) {
            return ChatRoomResponse.fromEntity(existing.get());
        }

        // ⭐ Tạo room mới, CHƯA gán dược sĩ nào
        ChatRoom room = new ChatRoom();
        room.setCustomer(customer);
        room.setType(type);
        room.setPharmacist(null); // chưa có dược sĩ chính thức
        room.setStatus(ChatRoomStatus.OPEN);
        room.setClosedAt(null);

        chatRoomRepository.save(room);
        return ChatRoomResponse.fromEntity(room);
    }

    @Transactional
    public void closeRoom(Long roomId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ApiException("Không tìm thấy phòng chat", HttpStatus.NOT_FOUND));
        room.setStatus(ChatRoomStatus.CLOSED);
        room.setClosedAt(LocalDateTime.now());
        chatRoomRepository.save(room);
    }

    @Transactional(readOnly = true)
    public List<ChatRoomResponse> getRoomsBySupportRole(ChatRoomType type) {
        return chatRoomRepository.findByTypeAndStatusOrderByCreatedAtDesc(type, ChatRoomStatus.OPEN)
                .stream()
                .map(ChatRoomResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ChatRoomResponse> getRoomsForCustomer(Integer customerId) {
        return chatRoomRepository.findByCustomerIdOrderByCreatedAtDesc(customerId)
                .stream()
                .map(ChatRoomResponse::fromEntity)
                .toList();
    }

}
