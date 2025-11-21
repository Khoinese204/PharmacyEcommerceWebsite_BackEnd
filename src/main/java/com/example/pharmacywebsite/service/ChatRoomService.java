package com.example.pharmacywebsite.service;

import com.example.pharmacywebsite.domain.ChatRoom;
import com.example.pharmacywebsite.domain.User;
import com.example.pharmacywebsite.dto.ChatRoomResponse;
import com.example.pharmacywebsite.enums.ChatRoomStatus;
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
    private final SupportStatusService supportStatusService;

    public ChatRoomService(ChatRoomRepository chatRoomRepository,
            UserRepository userRepository,
            SupportStatusService supportStatusService) {
        this.chatRoomRepository = chatRoomRepository;
        this.userRepository = userRepository;
        this.supportStatusService = supportStatusService;
    }

    /**
     * Khách start chat:
     * - Nếu đã có room OPEN của chính khách đó => dùng lại.
     * - Nếu chưa có => tạo room mới và gán một pharmacist đang online.
     */
    @Transactional
    public ChatRoomResponse startChat(String customerEmail) {
        User customer = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new ApiException("Không tìm thấy khách hàng", HttpStatus.NOT_FOUND));

        // Nếu đã có room OPEN thì dùng lại (1 customer = 1 room mở)
        var existing = chatRoomRepository
                .findFirstByCustomerAndStatusOrderByCreatedAtDesc(customer, ChatRoomStatus.OPEN);

        if (existing.isPresent()) {
            return ChatRoomResponse.fromEntity(existing.get());
        }

        // Chọn 1 dược sĩ đang online
        List<User> onlinePharmacists = supportStatusService.getOnlinePharmacistsEntities();
        if (onlinePharmacists.isEmpty()) {
            // Giống thông báo bạn dùng trước đây
            throw new ApiException(
                    "Hiện chưa có dược sĩ trực tuyến, vui lòng quay lại sau hoặc gọi hotline.",
                    HttpStatus.SERVICE_UNAVAILABLE);
        }

        // Ví dụ: chọn dược sĩ đầu tiên (sau này có thể ưu tiên ít room nhất)
        User pharmacist = onlinePharmacists.get(0);

        ChatRoom room = new ChatRoom();
        room.setCustomer(customer);
        room.setPharmacist(pharmacist);
        room.setStatus(ChatRoomStatus.OPEN);
        room.setCreatedAt(LocalDateTime.now());
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
}
