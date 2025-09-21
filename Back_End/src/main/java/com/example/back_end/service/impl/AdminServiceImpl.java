package com.example.back_end.service.impl;

import com.example.back_end.dto.*;
import com.example.back_end.entity.Ad;
import com.example.back_end.entity.AdStatus;
import com.example.back_end.repo.AdRepo;
import com.example.back_end.repo.UserRepo;
import com.example.back_end.service.AdminService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepo userRepo;
    private final AdRepo adRepo;

    //get all users for admin page
    @Override
    public List<UserSummaryDto> getAllUsersSummary() {
        return userRepo.findAllUserSummaries()
                .stream()
                .map(obj -> new UserSummaryDto(
                        (String) obj[0],
                        (String) obj[1],
                        (String) obj[2],
                        (String) obj[3]
                ))
                .collect(Collectors.toList());
    }

    //get all pending ads
    @Override
    public List<PendingAdDto> getAllPendingAds() {
        return adRepo.findAllPendingAds();
    }

    //get pending ads details for popup window
    @Override
    public PendingAdDetailDto getPendingAdDetails(Long adId) {
        Ad ad = adRepo.findById(adId).orElseThrow(() -> new EntityNotFoundException("Ad not found"));
        return new PendingAdDetailDto(
                ad.getAdId(),
                ad.getTitle(),
                ad.getDescription(),
                ad.getLocation(),
                ad.getPrice(),
                ad.getStatus(),
                ad.getUser().getUsername(),
                ad.getUser().getEmail(),
                ad.getUser().getPhone(),
                ad.getPhotos().stream().map(photo -> photo.getPhotoUrl()).collect(Collectors.toList())
        );
    }

    //pending delete ad
    @Override
    public void deleteAd(Long adId) {
        Ad ad = adRepo.findById(adId)
                .orElseThrow(() -> new RuntimeException("Ad not found with id: " + adId));
        adRepo.delete(ad);
    }

    //get all active ads
    @Override
    public List<ActiveAdDto> getAllActiveAds() {
        return adRepo.findAllActiveAds();
    }

    //active ads delete
    @Override
    public void activeDeleteAd(Long adId) {
        Ad ad = adRepo.findById(adId).orElseThrow(() -> new RuntimeException("Ad not found with id: " + adId));
        adRepo.delete(ad);
    }

    //get all active ads detals for popup window
    @Override
    public ActiveAdDetailDto getActiveAdDetails(Long adId) {
        Ad ad = adRepo.findById(adId).orElseThrow(() -> new EntityNotFoundException("Ad not found"));
        return new ActiveAdDetailDto(
                ad.getAdId(),
                ad.getTitle(),
                ad.getDescription(),
                ad.getLocation(),
                ad.getPrice(),
                ad.getStatus(),
                ad.getUser().getUsername(),
                ad.getUser().getEmail(),
                ad.getUser().getPhone(),
                ad.getPhotos().stream().map(photo -> photo.getPhotoUrl()).collect(Collectors.toList())
        );
    }

    //status card
    @Override
    public StatusCardDto getStatusCards() {
        long totalUsers = userRepo.count();
        long totalAds = adRepo.count();
        long totalPendingAds = adRepo.countByStatus(AdStatus.PENDING);
        long totalActiveAds = adRepo.countByStatus(AdStatus.ACTIVE);

        return new StatusCardDto(totalUsers, totalAds, totalPendingAds, totalActiveAds);
    }

//    @Override
//    public UserSummaryDto getUserSummaryById(Long id) {
//       return userRepo.findUserSummaryById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
//    }

    // Get ad by ID
    public Ad getAdById(Long adId) {
        return adRepo.findById(adId).orElseThrow(() -> new RuntimeException("Ad not found"));
    }

    // Activate ad
    @Transactional
    public Ad activateAd(Long adId) {
        Ad ad = getAdById(adId);
        ad.setStatus(AdStatus.ACTIVE);
        return adRepo.save(ad);
    }


}
