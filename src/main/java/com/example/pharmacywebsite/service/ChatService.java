package com.example.pharmacywebsite.service;

import com.example.pharmacywebsite.domain.ChatMessage;
import com.example.pharmacywebsite.domain.ChatRoom;
import com.example.pharmacywebsite.domain.User;
import com.example.pharmacywebsite.dto.ChatMessageRequest;
import com.example.pharmacywebsite.dto.ChatMessageResponse;
import com.example.pharmacywebsite.enums.ChatSenderRole;
import com.example.pharmacywebsite.exception.ApiException;
import com.example.pharmacywebsite.repository.ChatMessageRepository;
import com.example.pharmacywebsite.repository.ChatRoomRepository;
import com.example.pharmacywebsite.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

                if (senderEmail == null) {
                        throw new ApiException("WebSocket không xác thực (thiếu JWT)", HttpStatus.UNAUTHORIZED);
                }

                User sender = userRepository.findByEmail(senderEmail)
                                .orElseThrow(() -> new ApiException("Không tìm thấy người gửi",
                                                HttpStatus.UNAUTHORIZED));

                ChatRoom room = chatRoomRepository.findById(req.getRoomId())
                                .orElseThrow(() -> new ApiException("Không tìm thấy phòng chat", HttpStatus.NOT_FOUND));

                // ===== KIỂM TRA QUYỀN TRONG ROOM (1 customer + 1 pharmacist + optional ADMIN)
                // =====
                Integer senderId = sender.getId();

                boolean isCustomerInRoom = room.getCustomer() != null &&
                                room.getCustomer().getId().equals(senderId);

                boolean isPharmacistInRoom = room.getPharmacist() != null &&
                                room.getPharmacist().getId().equals(senderId);

                boolean isAdmin = sender.getRole() != null
                                && "ADMIN".equalsIgnoreCase(sender.getRole().getName());

                if (!isCustomerInRoom && !isPharmacistInRoom && !isAdmin) {
                        throw new ApiException("Bạn không được phép gửi tin trong phòng chat này",
                                        HttpStatus.FORBIDDEN);
                }

                // ===== XÁC ĐỊNH ROLE GỬI =====
                String roleName = sender.getRole() != null ? sender.getRole().getName() : null;
                ChatSenderRole senderRole;

                if ("PHARMACIST".equalsIgnoreCase(roleName) || isPharmacistInRoom) {
                        senderRole = ChatSenderRole.PHARMACIST;
                } else {
                        senderRole = ChatSenderRole.CUSTOMER;
                }

                // ===== LƯU TIN NHẮN =====
                ChatMessage message = new ChatMessage();
                message.setRoom(room);
                message.setSender(sender);
                message.setSenderRole(senderRole);
                message.setContent(req.getContent());

                chatMessageRepository.save(message);

                ChatMessageResponse dto = ChatMessageResponse.fromEntity(message);

                // ===== BROADCAST TỚI CÁC CLIENT ĐANG SUBSCRIBE ROOM =====
                String destination = "/topic/rooms/" + room.getId();
                messagingTemplate.convertAndSend(destination, dto);

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
        public List<ChatMessageResponse> getMessages(Long roomId) {
                ChatRoom room = chatRoomRepository.findById(roomId)
                                .orElseThrow(() -> new ApiException("Không tìm thấy phòng chat", HttpStatus.NOT_FOUND));
                return chatMessageRepository.findByRoomOrderBySentAtAsc(room)
                                .stream()
                                .map(ChatMessageResponse::fromEntity)
                                .toList();
        }
}
