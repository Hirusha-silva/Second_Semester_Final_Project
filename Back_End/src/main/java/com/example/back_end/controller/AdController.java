package com.example.back_end.controller;

import com.example.back_end.dto.AdDto;
import com.example.back_end.dto.AdRequestDto;
import com.example.back_end.entity.Ad;
import com.example.back_end.entity.Category;
import com.example.back_end.entity.VehicleModel;
import com.example.back_end.service.AdService;
import com.example.back_end.service.CategoryService;
import com.example.back_end.service.VehicalModelService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ads")
@RequiredArgsConstructor
@CrossOrigin
public class AdController {
    private final AdService adService;
    private final CategoryService categoryService;
    private final VehicalModelService vehicalModelService;


    // Create Ad with Photos
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Ad> createAd(
            @RequestPart("ad") String adJson, // get JSON as string
            @RequestPart("photos") List<MultipartFile> photos) throws Exception {

        // Convert JSON string to DTO
        ObjectMapper objectMapper = new ObjectMapper();
        AdRequestDto adRequestDTO = objectMapper.readValue(adJson, AdRequestDto.class);

        Ad newAd = adService.createAdWithPhotos(adRequestDTO, photos);
        return ResponseEntity.ok(newAd);

    }

    //Get all categories
    @GetMapping("/category")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategory();
        return ResponseEntity.ok(categories);
    }

    //get all vehical models
    @GetMapping("/models")
    public List<VehicleModel> getAllVehicleModels() {
        return vehicalModelService.getAllVehicalModels();
    }

    @GetMapping("/active")
    public List<AdDto> getAllActiveAds() {
        List<Ad> ads = adService.getAllActiveAds();

        return ads.stream().map(ad -> AdDto.builder()
                .adId(ad.getAdId())
                .title(ad.getTitle())
                .description(ad.getDescription())
                .price(ad.getPrice())
                .location(ad.getLocation())
                .categoryName(ad.getCategory() != null ? ad.getCategory().getName() : "")
                .brand(ad.getVehicleModel() != null ? ad.getVehicleModel().getBrand() : "")
                .model(ad.getVehicleModel() != null ? ad.getVehicleModel().getModel() : "")
                .photoUrls(ad.getPhotos().stream().map(p -> p.getPhotoUrl()).toList())
                .build()
        ).collect(Collectors.toList());
    }
}
