package com.example.pharmacywebsite.dto;

public class BestSellingProductDto {
    private Integer id;
    private String name;
    private String imageUrl;
    private Double originalPrice;
    private Double discountedPrice;
    private String unit;

    public BestSellingProductDto(Integer id, String name, String imageUrl,
            Double originalPrice, Double discountedPrice, String unit) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.originalPrice = originalPrice;
        this.discountedPrice = discountedPrice;
        this.unit = unit;
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public Double getDiscountedPrice() {
        return discountedPrice;
    }

    public String getUnit() {
        return unit;
    }
}
