package com.example.back_end.service;

import com.example.back_end.dto.AdRequestDto;
import com.example.back_end.dto.AdUpdateDto;
import com.example.back_end.dto.UserActiveAdDto;
import com.example.back_end.dto.UserAdDto;
import com.example.back_end.entity.Ad;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AdService {
    public Ad createAdWithPhotos(AdRequestDto dto, List<MultipartFile> photos) throws IOException;
    public List<Ad> getAllActiveAds();
    public UserActiveAdDto getUserActiveAds(Long adId);
    public List<Ad> searchAds(String keyword, Long categoryId, String brand, String model, String location);
    List<UserAdDto> getUserAds(Long userId);
    public Ad updateAdWithPhotos(Long adId, AdUpdateDto dto, List<MultipartFile> newPhotos) throws IOException;
    void deleteAd(Long adId);

}
