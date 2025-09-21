package com.example.back_end.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusCardDto {
    private long totalUsers;
    private long totalListings;
    private long pendingAds;
    private long activeAds;
}
