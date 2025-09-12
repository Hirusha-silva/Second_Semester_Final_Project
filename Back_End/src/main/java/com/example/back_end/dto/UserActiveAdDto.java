package com.example.back_end.dto;

import com.example.back_end.entity.AdStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserActiveAdDto {
    private Long adId;
    private String title;
    private String description;
    private String location;
    private Double price;
    private AdStatus status;
    private String name;
    private String email;
    private String phone;
    private String categoryName;
    private String brand;
    private String model;
    private List<String> photos;
}
