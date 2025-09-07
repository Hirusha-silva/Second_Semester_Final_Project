package com.example.back_end.service;

import com.example.back_end.dto.UserSummaryDto;

import java.util.List;

public interface AdminService {
    List<UserSummaryDto> getAllUsersSummary();

  //  UserSummaryDto getUserSummaryById(Long id);
}
