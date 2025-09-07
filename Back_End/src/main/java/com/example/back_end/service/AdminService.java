package com.example.back_end.service;

import com.example.back_end.dto.PendingAdDto;
import com.example.back_end.dto.UserSummaryDto;

import java.util.List;

public interface AdminService {
    List<UserSummaryDto> getAllUsersSummary();
    List<PendingAdDto> getAllPendingAds();

  //  UserSummaryDto getUserSummaryById(Long id);
}
