package com.example.back_end.controller;

import com.example.back_end.dto.ApiResponseDto;
import com.example.back_end.dto.PendingAdDto;
import com.example.back_end.dto.UserSummaryDto;
import com.example.back_end.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponseDto> getAllUsers() {
        List<UserSummaryDto> users = adminService.getAllUsersSummary();
        return ResponseEntity.ok(new ApiResponseDto(200, "All users fetched successfully", users));
    }

    @GetMapping("/pending-ads")
    public ResponseEntity<ApiResponseDto> getAllPendingAds() {
        List<PendingAdDto> pendingAds = adminService.getAllPendingAds();
        return ResponseEntity.ok(new ApiResponseDto(200, "Pending Ads Loaded", pendingAds));
    }

//    @GetMapping("/users/{id}")
//    public UserSummaryDto getUserSummary(@PathVariable Long id) {
//        return adminService.getUserSummaryById(id);
//    }
}
