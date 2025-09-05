package com.example.back_end.controller;

import com.example.back_end.dto.AdRequestDto;
import com.example.back_end.entity.Ad;
import com.example.back_end.service.AdService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/ads")
@RequiredArgsConstructor
public class AdController {
    private final AdService adService;

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
}
