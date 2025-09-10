package com.example.back_end.controller;

import com.example.back_end.dto.*;
import com.example.back_end.entity.Ad;
import com.example.back_end.service.AdminService;
import com.example.back_end.service.EmailServices;
import com.example.back_end.service.impl.AdminServiceImpl;
import com.example.back_end.service.impl.EmailService;
import jakarta.mail.MessagingException;
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
    private final EmailServices emailServices;

    //load all users admin page
    @GetMapping("/users")
    public ResponseEntity<ApiResponseDto> getAllUsers() {
        List<UserSummaryDto> users = adminService.getAllUsersSummary();
        return ResponseEntity.ok(new ApiResponseDto(200, "All users fetched successfully", users));
    }

   // load all pending ads admin page
    @GetMapping("/pending-ads")
    public ResponseEntity<ApiResponseDto> getAllPendingAds() {
        List<PendingAdDto> pendingAds = adminService.getAllPendingAds();
        return ResponseEntity.ok(new ApiResponseDto(200, "Pending Ads Loaded", pendingAds));
    }

//    @GetMapping("/users/{id}")
//    public UserSummaryDto getUserSummary(@PathVariable Long id) {
//        return adminService.getUserSummaryById(id);
//    }

    //Pending ads update to active ads
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

    // Get Pending ads detail
    @GetMapping("/pending-ads/{adId}")
    public ResponseEntity<?> getPendingAdDetails(@PathVariable Long adId) {
        PendingAdDetailDto pendingAdDetailDto = adminService.getPendingAdDetails(adId);
        return ResponseEntity.ok(
                new ApiResponseDto(200, "Pending ad details fetched successfully", pendingAdDetailDto)
        );
    }

    //Pending ads delete
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponseDto> deleteAd(@PathVariable("id") Long id) {
        adminService.deleteAd(id);
        return ResponseEntity.ok(new ApiResponseDto(200, "Ad deleted successfully",id));
    }

   // Get all active ads
    @GetMapping("/active-ads")
    public ResponseEntity<ApiResponseDto> getActiveAds() {
        List<ActiveAdDto> activeAds = adminService.getAllActiveAds();
        return ResponseEntity.ok(new ApiResponseDto(200, "Active ads fetched successfully", activeAds));
    }

    //active ad delete
    @DeleteMapping("/active/delete/{id}")
    public ResponseEntity<ApiResponseDto> deleteActiveAd(@PathVariable("id") Long id) {
        adminService.deleteAd(id);
        return ResponseEntity.ok(new ApiResponseDto(200, "Ad deleted successfully",id));
    }

    //load active ads details for pop up window
    @GetMapping("/active-ads/{adId}")
    public ResponseEntity<ApiResponseDto> getActiveAdDetails(@PathVariable("adId") Long adId) {
        ActiveAdDetailDto activeAdDetailDto = adminService.getActiveAdDetails(adId);
        return ResponseEntity.ok(
                new ApiResponseDto(200, "Pending ad details fetched successfully", activeAdDetailDto)
        );
    }

   // send custom email to user
    @PostMapping("/send-mail")
    public ResponseEntity<?> sendMail(@RequestBody MailRequestDto request){
        try {
            emailServices.sendMail(request);
            return ResponseEntity.ok(new ApiResponseDto(200,"Mail sent",request));
        } catch (MessagingException e) {
            return ResponseEntity.ok(new ApiResponseDto(500,"Mail Failed",e.getMessage()));
        }
    }
}
