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
                ChatRoom room = chatRoomRepository.findById(req.getRoomId())
                                .orElseThrow(() -> new RuntimeException("Chat room not found"));

                User sender = userRepository.findByEmail(senderEmail)
                                .orElseThrow(() -> new RuntimeException("Sender not found"));

                String roleName = sender.getRole() != null ? sender.getRole().getName() : null;
                ChatSenderRole senderRole = (roleName != null && roleName.equalsIgnoreCase("PHARMACIST"))
                                ? ChatSenderRole.PHARMACIST
                                : ChatSenderRole.CUSTOMER;

                // ⭐ Quy tắc phân quyền:
                if (senderRole == ChatSenderRole.CUSTOMER) {
                        // customer chỉ được chat trong room của chính mình
                        if (!room.getCustomer().getId().equals(sender.getId())) {
                                throw new RuntimeException("You cannot send messages to this room");
                        }
                } else {
                        // senderRole == PHARMACIST → mọi dược sĩ đều trả lời được
                        // không cần check room.getPharmacist() nữa
                }

                ChatMessage message = new ChatMessage();
                message.setRoom(room);
                message.setSender(sender);
                message.setSenderRole(senderRole);
                message.setContent(req.getContent());
                // sentAt đã set default trong entity

                chatMessageRepository.save(message);

                ChatMessageResponse dto = ChatMessageResponse.fromEntity(message);

                messagingTemplate.convertAndSend("/topic/rooms/" + room.getId(), dto);

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

                boolean isCustomerOfRoom = room.getCustomer() != null
                                && room.getCustomer().getId().equals(requester.getId());

                String roleName = requester.getRole() != null
                                ? requester.getRole().getName()
                                : null;
                boolean isPharmacist = roleName != null && roleName.equalsIgnoreCase("PHARMACIST");

                // ⭐ Rule mới: customer của room HOẶC bất kỳ dược sĩ nào
                if (!isCustomerOfRoom && !isPharmacist) {
                        throw new ApiException("Bạn không có quyền xem phòng chat này", HttpStatus.FORBIDDEN);
                }

                return chatMessageRepository.findByRoomOrderBySentAtAsc(room)
                                .stream()
                                .map(ChatMessageResponse::fromEntity)
                                .toList();
        }
}
