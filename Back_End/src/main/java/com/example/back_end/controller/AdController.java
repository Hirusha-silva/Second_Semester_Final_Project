package com.example.back_end.controller;

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
}
