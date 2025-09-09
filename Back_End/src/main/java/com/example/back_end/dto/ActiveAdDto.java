package com.example.back_end.dto;

import com.example.back_end.entity.AdStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActiveAdDto {
    private Long id;
    private String title;
    private String description;
    private String location;
    private double price;
    private AdStatus status;
    private String username;
}
