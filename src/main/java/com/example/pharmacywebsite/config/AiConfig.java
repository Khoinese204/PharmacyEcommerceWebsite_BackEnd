package com.example.pharmacywebsite.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ai.chat.client.ChatClient;

@Configuration
public class AiConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        // Builder này do Spring AI auto-config dựa trên spring.ai.openai.* trong
        // properties
        return builder.build();
    }
}
