package com.example.back_end.dto;

import com.example.back_end.entity.AdStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PendingAdDetailDto {
    private Long adId;
    private String title;
    private String description;
    private String location;
    private Double price;
    private AdStatus status;
    private String username;
    private String email;
    private String phone;
    private List<String> photos;
}
