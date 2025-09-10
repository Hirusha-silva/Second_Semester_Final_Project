package com.example.back_end.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdDto {
    private Long adId;
    private String title;
    private String description;
    private Double price;
    private String location;
    private String categoryName;
    private String brand;
    private String model;
    private List<String> photoUrls;
}
