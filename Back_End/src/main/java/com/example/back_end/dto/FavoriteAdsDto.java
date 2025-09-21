package com.example.back_end.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FavoriteAdsDto {
    private Long adId;
    private String title;
    private String description;
    private Double price;
    private String location;
    private String categoryName;
    private String brand;
    private String model;
    private List<String> photoUrls;

    // ✅ Make sure this exists and is Boolean type
    private Boolean isFavorite;
}
