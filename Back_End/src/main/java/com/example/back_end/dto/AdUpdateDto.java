package com.example.back_end.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdUpdateDto {
    private Long adId;
    private String title;
    private String description;
    private Double price;
    private String location;

    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("categoryId")
    private Long categoryId;

    @JsonProperty("modelId")
    private Long modelId;
}
