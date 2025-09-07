package com.example.back_end.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PendingAdDto {
    private Long id;
    private String title;
    private String description;
    private String location;
    private double price;
    private String status;
    private String postedBy;
}
