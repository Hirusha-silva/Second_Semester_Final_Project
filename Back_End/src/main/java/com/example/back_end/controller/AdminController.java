package com.example.back_end.controller;

import com.example.back_end.dto.ApiResponseDto;
import com.example.back_end.dto.PendingAdDto;
import com.example.back_end.dto.UserSummaryDto;
import com.example.back_end.entity.Ad;
import com.example.back_end.service.AdminService;
import com.example.back_end.service.impl.AdminServiceImpl;
import com.example.back_end.service.impl.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final AdminServiceImpl adminServiceImpl;
    private final EmailService emailService;

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

    @PutMapping("/pending-ads/{adId}/activate")
    public ResponseEntity<?> activateAd(@PathVariable Long adId) {
        Ad ad = adminServiceImpl.activateAd(adId);

        // Send email
        emailService.sendEmail(
                ad.getUser().getEmail(),
                "Your Ad is Published",
                "Hello " + ad.getUser().getUsername() + ", your ad '" + ad.getTitle() + "' is now ACTIVE!"
        );

        return ResponseEntity.ok(Map.of(
                "status", 200,
                "message", "Ad activated and email sent"
        ));
    }
}
