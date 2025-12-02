package com.example.pharmacywebsite.service;

import com.example.pharmacywebsite.domain.ChatMessage;
import com.example.pharmacywebsite.domain.ChatRoom;
import com.example.pharmacywebsite.domain.User;
import com.example.pharmacywebsite.dto.ChatMessageRequest;
import com.example.pharmacywebsite.dto.ChatMessageResponse;
import com.example.pharmacywebsite.enums.ChatRoomType;
import com.example.pharmacywebsite.enums.ChatSenderRole;
import com.example.pharmacywebsite.exception.ApiException;
import com.example.pharmacywebsite.repository.ChatMessageRepository;
import com.example.pharmacywebsite.repository.ChatRoomRepository;
import com.example.pharmacywebsite.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatService(ChatMessageRepository chatMessageRepository,
            ChatRoomRepository chatRoomRepository,
            UserRepository userRepository,
            SimpMessagingTemplate messagingTemplate) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Gửi tin nhắn trong room qua WebSocket.
     * senderEmail lấy từ JWT (principal.getName()).
     */
    @Transactional
    public ChatMessageResponse handleIncomingMessage(ChatMessageRequest req, String senderEmail) {
        // 1. Tìm Room và Sender
        ChatRoom room = chatRoomRepository.findById(req.getRoomId())
                .orElseThrow(() -> new RuntimeException("Chat room not found"));

        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        // 2. Xác định Role của người gửi (Map từ String Role sang Enum)
        String roleName = sender.getRole() != null ? sender.getRole().getName().toUpperCase() : "";

        ChatSenderRole senderRole;
        if (roleName.contains("PHARMACIST")) {
            senderRole = ChatSenderRole.PHARMACIST;
        } else if (roleName.contains("SALE") || roleName.contains("STAFF")) {
            senderRole = ChatSenderRole.SALES;
        } else {
            senderRole = ChatSenderRole.CUSTOMER;
        }

        // 3. KIỂM TRA QUYỀN (VALIDATION)
        if (senderRole == ChatSenderRole.CUSTOMER) {
            // Khách: Chỉ chat trong phòng của mình
            if (!room.getCustomer().getId().equals(sender.getId())) {
                throw new RuntimeException("Bạn không thể chat trong phòng của người khác");
            }
        } else {
            // Nhân viên: Check đúng chuyên môn
            if (room.getType() == ChatRoomType.MEDICAL_ADVICE && !roleName.contains("PHARMACIST")) {
                throw new RuntimeException("Chỉ Dược sĩ mới được trả lời tư vấn thuốc.");
            }
            if (room.getType() == ChatRoomType.ORDER_SUPPORT
                    && !(roleName.contains("SALE") || roleName.contains("STAFF"))) {
                throw new RuntimeException("Chỉ Nhân viên mới được hỗ trợ đơn hàng.");
            }
        }

        // 4. LƯU VÀ GỬI TIN NHẮN (Phần này phải để RA NGOÀI if-else để ai cũng chạy
        // được)

        ChatMessage message = new ChatMessage();
        message.setRoom(room);
        message.setSender(sender);
        message.setSenderRole(senderRole);
        message.setContent(req.getContent());
        // sentAt thường được set tự động trong @PrePersist của Entity, hoặc bạn set ở
        // đây:
        // message.setSentAt(LocalDateTime.now());

        chatMessageRepository.save(message);

        // Convert sang DTO response
        ChatMessageResponse dto = ChatMessageResponse.fromEntity(message);

        // Bắn WebSocket tới các client đang sub
        messagingTemplate.convertAndSend("/topic/rooms/" + room.getId(), dto);

        // Trả về kết quả cho Controller (để không bị lỗi thiếu return)
        return dto;
    }

    /**
     * Lấy lịch sử tin nhắn cho 1 user cụ thể (có kiểm tra quyền vào room).
     */
    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getMessagesForUser(Long roomId, User viewer) {

        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ApiException("Không tìm thấy phòng chat", HttpStatus.NOT_FOUND));

        Integer viewerId = viewer.getId();

        boolean isCustomerInRoom = room.getCustomer() != null &&
                room.getCustomer().getId().equals(viewerId);

        boolean isPharmacistInRoom = room.getPharmacist() != null &&
                room.getPharmacist().getId().equals(viewerId);

        boolean isAdmin = viewer.getRole() != null
                && "ADMIN".equalsIgnoreCase(viewer.getRole().getName());

        if (!isCustomerInRoom && !isPharmacistInRoom && !isAdmin) {
            throw new ApiException("Bạn không có quyền xem phòng chat này",
                    HttpStatus.FORBIDDEN);
        }

        return chatMessageRepository.findByRoomOrderBySentAtAsc(room)
                .stream()
                .map(ChatMessageResponse::fromEntity)
                .toList();
    }

    // Giữ hàm cũ nếu chỗ khác đang dùng nội bộ, nhưng không expose ra controller
    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getMessages(Long roomId, String requesterEmail) {
        User requester = userRepository.findByEmail(requesterEmail)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.UNAUTHORIZED));

        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ApiException("Chat room not found", HttpStatus.NOT_FOUND));

        String roleName = requester.getRole() != null ? requester.getRole().getName().toUpperCase() : "";

        boolean isAllowed = false;

        // 1. Admin luôn được xem
        if (roleName.equals("ADMIN")) {
            isAllowed = true;
        }
        // 2. Khách hàng chính chủ được xem
        else if (room.getCustomer().getId().equals(requester.getId())) {
            isAllowed = true;
        }
        // 3. Nhân viên: Check theo loại phòng
        else {
            if (room.getType() == ChatRoomType.MEDICAL_ADVICE && roleName.contains("PHARMACIST")) {
                isAllowed = true;
            } else if (room.getType() == ChatRoomType.ORDER_SUPPORT
                    && (roleName.contains("SALE") || roleName.contains("STAFF"))) {
                isAllowed = true; // <--- Dòng này giúp Sales không bị lỗi 403 nữa
            }
        }

        if (!isAllowed) {
            // In ra log để debug xem tại sao bị chặn
            System.out.println(
                    "❌ Access Denied: User=" + requesterEmail + ", Role=" + roleName + ", RoomType=" + room.getType());
            throw new ApiException("Bạn không có quyền xem phòng chat này", HttpStatus.FORBIDDEN);
        }

        return chatMessageRepository.findByRoomOrderBySentAtAsc(room)
                .stream()
                .map(ChatMessageResponse::fromEntity)
                .toList();
    }
}
