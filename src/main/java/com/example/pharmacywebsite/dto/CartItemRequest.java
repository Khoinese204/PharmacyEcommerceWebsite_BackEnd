// 📄 src/main/java/com/example/pharmacywebsite/dto/CartItemRequest.java
package com.example.pharmacywebsite.dto;

import lombok.Data;

@Data
public class CartItemRequest {
    private Integer id;
    private Integer quantity;
}
