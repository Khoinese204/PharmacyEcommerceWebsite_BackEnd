package com.example.pharmacywebsite.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path rootLocation;

    // Lấy đường dẫn từ application.properties
    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
        this.rootLocation = Paths.get(uploadDir);
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    // Lưu file
    public String store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file.");
            }
            // Tạo tên file duy nhất
            String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String filename = UUID.randomUUID().toString() + extension;

            Path destinationFile = this.rootLocation.resolve(filename).toAbsolutePath();

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
            return filename; // Trả về tên file đã lưu
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }

    // Xoá file (dùng khi update hoặc delete)
    public void delete(String filename) {
        try {
            if (filename == null || filename.isEmpty()) {
                return;
            }
            Path file = rootLocation.resolve(filename);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            // Không ném lỗi nghiêm trọng nếu không xoá được file cũ
            System.err.println("Failed to delete file: " + filename);
        }
    }
}