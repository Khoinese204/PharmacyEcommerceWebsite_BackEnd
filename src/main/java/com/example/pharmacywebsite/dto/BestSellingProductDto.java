package com.example.pharmacywebsite.dto;

public class BestSellingProductDto {
    private Integer id;
    private String name;
    private String imageUrl;
    private Double originalPrice;
    private Double price;
    private String unit;

    public BestSellingProductDto(Integer id, String name, String imageUrl,
            Double originalPrice, Double price, String unit) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.originalPrice = originalPrice;
        this.price = price;
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

    public Double getPrice() {
        return price;
    }

    public String getUnit() {
        return unit;
    }
}
