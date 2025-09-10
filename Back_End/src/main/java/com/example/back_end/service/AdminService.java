package com.example.back_end.service;

import com.example.back_end.dto.*;

import java.util.List;

public interface AdminService {
    List<UserSummaryDto> getAllUsersSummary();
    List<PendingAdDto> getAllPendingAds();
    public PendingAdDetailDto getPendingAdDetails(Long adId);
    public void deleteAd(Long adId);
    List<ActiveAdDto> getAllActiveAds();
    public void activeDeleteAd(Long adId);
    public ActiveAdDetailDto getActiveAdDetails(Long adId);

  //  UserSummaryDto getUserSummaryById(Long id);
}
