package com.example.back_end.service.impl;

import com.example.back_end.dto.AdRequestDto;
import com.example.back_end.dto.AdUpdateDto;
import com.example.back_end.dto.UserActiveAdDto;
import com.example.back_end.dto.UserAdDto;
import com.example.back_end.entity.Ad;
import com.example.back_end.entity.AdPhoto;
import com.example.back_end.entity.AdStatus;
import com.example.back_end.repo.*;
import com.example.back_end.service.AdService;
import com.example.back_end.util.FileUploadUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {
    private final AdRepo adRepo;
    private final AdPhotoRepo adPhotoRepo;
    private final UserRepo userRepo;
    private final CategoryRepo categoryRepo;
    private final VehicleModelRepo vehicleModelRepo;


   // post ad
    @Override
    public Ad createAdWithPhotos(AdRequestDto dto, List<MultipartFile> photos) throws IOException {
        Ad ad = new Ad();
        ad.setTitle(dto.getTitle());
        ad.setDescription(dto.getDescription());
        ad.setPrice(dto.getPrice());
        ad.setLocation(dto.getLocation());
        ad.setStatus(AdStatus.valueOf("PENDING"));



        ad.setUser(userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found")));
        ad.setCategory(categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found")));
        ad.setVehicleModel(vehicleModelRepo.findById(dto.getModelId())
                .orElseThrow(() -> new RuntimeException("Vehicle model not found")));

        // Save ad first
        Ad savedAd = adRepo.save(ad);


        String uploadDir = System.getProperty("user.dir") + "/uploads/";
        File uploadFolder = new File(uploadDir);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs(); // create folder if not exist
        }

        // Save photos
        for (MultipartFile file : photos) {
            String fileName = file.getOriginalFilename();
            if (fileName == null || fileName.isEmpty()) continue;

            // Full path where the file will be saved
            String filePath = uploadDir + fileName;
            File destFile = new File(filePath);

            // Actually save the file
            file.transferTo(destFile);

            // Save DB record with relative path
            AdPhoto adPhoto = new AdPhoto();
            adPhoto.setPhotoUrl("/uploads/" + fileName);
            adPhoto.setAd(savedAd);

            adPhotoRepo.save(adPhoto);
        }

        return savedAd;
    }

    //load active ads
    @Override
    public List<Ad> getAllActiveAds() {
        return adRepo.findByStatus(AdStatus.ACTIVE);
    }

    //load user active ads details
    @Override
    public  UserActiveAdDto getUserActiveAds(Long adId) {
        Ad ad = adRepo.findById(adId).orElseThrow(() -> new RuntimeException("Ad not found"+adId));

        return new UserActiveAdDto(
                ad.getAdId(),
                ad.getTitle(),
                ad.getDescription(),
                ad.getLocation(),
                ad.getPrice(),
                ad.getStatus(),
                ad.getUser().getName(),
                ad.getUser().getEmail(),
                ad.getUser().getPhone(),
                ad.getCategory().getName(),
                ad.getVehicleModel().getBrand(),
                ad.getVehicleModel().getModel(),
                ad.getPhotos().stream()
                        .map(AdPhoto::getPhotoUrl)
                        .collect(Collectors.toList())
        );
    }

    //search
    @Override
    public List<Ad> searchAds(String keyword, Long categoryId, String brand, String model, String location) {
        return adRepo.searchAds(
                keyword != null && !keyword.isEmpty() ? keyword : null,
                categoryId,
                brand != null && !brand.isEmpty() ? brand : null,
                model != null && !model.isEmpty() ? model : null,
                location != null && !location.isEmpty() ? location : null
        );
    }

    //get user ads
    @Override
    public List<UserAdDto> getUserAds(Long userId) {
        List<Ad> ads = adRepo.findByUserId(userId);

        return ads.stream().map(ad -> new UserAdDto(
                ad.getAdId(),
                ad.getTitle(),
                ad.getDescription(),
                ad.getLocation(),
                ad.getPrice(),
                ad.getStatus(),
                ad.getCategory().getName(),
                ad.getVehicleModel().getBrand(),
                ad.getVehicleModel().getModel(),
                ad.getPhotos().stream().map(AdPhoto::getPhotoUrl).collect(Collectors.toList())
        )).collect(Collectors.toList());
    }


    //update user ads
    @Transactional
    @Override
    public Ad updateAdWithPhotos(Long adId, AdUpdateDto dto, List<MultipartFile> newPhotos) {
        Ad ad = adRepo.findById(adId)
                .orElseThrow(() -> new RuntimeException("Ad not found: " + adId));

        ad.setTitle(dto.getTitle());
        ad.setDescription(dto.getDescription());
        ad.setPrice(dto.getPrice());
        ad.setLocation(dto.getLocation());
        ad.setStatus(AdStatus.PENDING);

        ad.setCategory(categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found")));
        ad.setVehicleModel(vehicleModelRepo.findById(dto.getModelId())
                .orElseThrow(() -> new RuntimeException("Vehicle model not found")));

        try {
            // Delete old photos
            List<AdPhoto> oldPhotos = adRepo.findPhotosByAdId(adId);
            for (AdPhoto old : oldPhotos) {
                String filePath = System.getProperty("user.dir") + old.getPhotoUrl();
                File file = new File(filePath);
                if (file.exists()) file.delete();
                adPhotoRepo.delete(old);
            }

            // Save new photos
            if (newPhotos != null && !newPhotos.isEmpty()) {
                String uploadDir = System.getProperty("user.dir") + "/uploads/";
                File uploadFolder = new File(uploadDir);
                if (!uploadFolder.exists()) uploadFolder.mkdirs();

                for (MultipartFile file : newPhotos) {
                    String fileName = file.getOriginalFilename();
                    if (fileName == null || fileName.isEmpty()) continue;

                    String filePath = uploadDir + fileName;
                    File destFile = new File(filePath);
                    file.transferTo(destFile);

                    AdPhoto adPhoto = new AdPhoto();
                    adPhoto.setPhotoUrl("/uploads/" + fileName);
                    adPhoto.setAd(ad);

                    adPhotoRepo.save(adPhoto);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            // throw new RuntimeException("Failed to save photos"); // optional, comment out to avoid 500
        }

        return adRepo.save(ad);
    }

    //delete user ads
    @Transactional
    @Override
    public void deleteAd(Long adId) {
        Ad ad = adRepo.findById(adId).orElseThrow(() -> new RuntimeException("Ad not found: " + adId));

        // Delete attached photos
        for (AdPhoto photo : ad.getPhotos()) {
            String filePath = System.getProperty("user.dir") + photo.getPhotoUrl();
            File file = new File(filePath);
            if (file.exists()) file.delete();
        }

        adRepo.delete(ad);
    }


    public Ad getAdById(Long adId) {
        return adRepo.findById(adId)
                .orElseThrow(() -> new RuntimeException("Ad not found"+adId));
    }

}
