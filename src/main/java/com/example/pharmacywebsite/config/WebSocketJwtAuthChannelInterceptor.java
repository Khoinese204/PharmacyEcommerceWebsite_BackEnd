package com.example.pharmacywebsite.config;

import com.example.pharmacywebsite.domain.User;
import com.example.pharmacywebsite.service.JwtService;
import com.example.pharmacywebsite.repository.UserRepository;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class WebSocketJwtAuthChannelInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;
    private final UserRepository userRepo;

    public WebSocketJwtAuthChannelInterceptor(JwtService jwtService, UserRepository userRepo) {
        this.jwtService = jwtService;
        this.userRepo = userRepo;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null)
            return message;

        // ⭐ Chỉ xử lý lúc client CONNECT WebSocket
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            // Lấy Authorization header từ STOMP
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            if (authHeader == null) {
                authHeader = accessor.getFirstNativeHeader("authorization");
            }

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                try {
                    // ⭐ Dùng đúng JwtService của bạn
                    String email = jwtService.extractEmail(token);
                    if (email != null) {
                        User u = userRepo.findByEmail(email).orElse(null);
                        if (u != null) {

                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                    email, // principal = email
                                    null,
                                    null // bạn không cần quyền ở WebSocket
                            );

                            accessor.setUser(authentication);

                            System.out.println("WS AUTH OK -> " + email);
                        }
                    }
                } catch (Exception ex) {
                    System.out.println("WS AUTH FAILED: " + ex.getMessage());
                }
            }
        }

        return message;
    }
}
