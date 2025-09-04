package com.example.back_end.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdRequestDto {
    private String title;
    private String description;
    private Double price;
    private String location;
    private Long user_id;
    private Long category_id;
    private Long model_id;
}
