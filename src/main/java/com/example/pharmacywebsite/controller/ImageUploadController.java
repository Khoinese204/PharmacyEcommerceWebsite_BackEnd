// package com.example.pharmacywebsite.controller;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.multipart.MultipartFile;

// import java.io.*;
// import java.nio.file.*;

// @RestController
// @RequestMapping("/api/upload")
// public class ImageUploadController {

// @Value("${frontend.image.path}")
// private String frontendImagePath;

// private static final String PREFIX = "product";
// private static final String EXT = ".jpg";

// @PostMapping("/medicine-image")
// public String uploadImage(@RequestParam("file") MultipartFile file) {
// try {
// if (file.isEmpty())
// throw new RuntimeException("File is empty");

// int nextId = getNextImageId();
// String fileName = PREFIX + nextId + EXT;
// Path dir = Paths.get(frontendImagePath);
// Files.createDirectories(dir); // Ensure dir exists

// Path target = dir.resolve(fileName);
// Files.copy(file.getInputStream(), target,
// StandardCopyOption.REPLACE_EXISTING);

// String imageUrl = "/images/products/" + fileName;
// return imageUrl;

// } catch (Exception e) {
// e.printStackTrace();
// throw new RuntimeException("Upload failed");
// }
// }

// private int getNextImageId() {
// File dir = new File(frontendImagePath);
// File[] files = dir.listFiles((d, name) -> name.startsWith(PREFIX) &&
// name.endsWith(EXT));

// int max = 0;
// if (files != null) {
// for (File f : files) {
// try {
// String num = f.getName().replace(PREFIX, "").replace(EXT, "");
// max = Math.max(max, Integer.parseInt(num));
// } catch (NumberFormatException ignored) {
// }
// }
// }
// return max + 1;
// }
// }
