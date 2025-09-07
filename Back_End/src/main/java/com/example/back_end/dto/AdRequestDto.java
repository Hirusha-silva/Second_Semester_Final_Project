package com.example.back_end.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdRequestDto {
    private String title;
    private String description;
    private Double price;
    private String location;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
//    private LocalDate  postDate = LocalDate.now();

    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("categoryId")
    private Long categoryId;

    @JsonProperty("modelId")
    private Long modelId;
}
