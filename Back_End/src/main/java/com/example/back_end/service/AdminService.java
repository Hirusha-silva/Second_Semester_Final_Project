package com.example.back_end.service;

import com.example.back_end.dto.PendingAdDetailDto;
import com.example.back_end.dto.PendingAdDto;
import com.example.back_end.dto.UserSummaryDto;

import java.util.List;

public interface AdminService {
    List<UserSummaryDto> getAllUsersSummary();
    List<PendingAdDto> getAllPendingAds();
    public PendingAdDetailDto getPendingAdDetails(Long adId);
    public void deleteAd(Long adId);

  //  UserSummaryDto getUserSummaryById(Long id);
}
