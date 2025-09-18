package com.example.back_end.dto;

import com.example.back_end.entity.AdStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAdDto {
    private Long adId;
    private String title;
    private String description;
    private String location;
    private Double price;
    private AdStatus status;
    private String category;
    private String brand;
    private String model;
    private List<String> photos;
}
