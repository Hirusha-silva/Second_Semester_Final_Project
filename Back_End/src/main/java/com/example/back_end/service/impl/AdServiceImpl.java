package com.example.back_end.service.impl;

import com.example.back_end.dto.AdRequestDto;
import com.example.back_end.entity.Ad;
import com.example.back_end.entity.AdPhoto;
import com.example.back_end.entity.AdStatus;
import com.example.back_end.repo.*;
import com.example.back_end.service.AdService;
import com.example.back_end.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {
    private final AdRepo adRepo;
    private final AdPhotoRepo adPhotoRepo;
    private final UserRepo userRepo;
    private final CategoryRepo categoryRepo;
    private final VehicleModelRepo vehicleModelRepo;
    @Override
    public Ad createAdWithPhotos(AdRequestDto dto, List<MultipartFile> photos) throws IOException {
        Ad ad = new Ad();
        ad.setTitle(dto.getTitle());
        ad.setDescription(dto.getDescription());
        ad.setPrice(dto.getPrice());
        ad.setLocation(dto.getLocation());
        ad.setStatus(AdStatus.valueOf("PENDING"));



        ad.setUser(userRepo.findById(dto.getUser_id())
                .orElseThrow(() -> new RuntimeException("User not found")));
        ad.setCategory(categoryRepo.findById(dto.getCategory_id())
                .orElseThrow(() -> new RuntimeException("Category not found")));
        ad.setVehicleModel(vehicleModelRepo.findById(dto.getModel_id())
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
}
