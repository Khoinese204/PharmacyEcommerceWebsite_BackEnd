package com.example.pharmacywebsite.config; // Đặt vào package config của bạn

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadPath = Paths.get(uploadDir);
        String absoluteUploadPath = uploadPath.toFile().getAbsolutePath();

        // Ánh xạ URL /uploads/categories/** tới thư mục vật lý trên server
        registry
                .addResourceHandler("/uploads/categories/**")
                .addResourceLocations("file:/" + absoluteUploadPath + "/");
    }
}