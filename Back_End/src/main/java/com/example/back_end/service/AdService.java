package com.example.back_end.service;

import com.example.back_end.dto.AdRequestDto;
import com.example.back_end.dto.UserActiveAdDto;
import com.example.back_end.entity.Ad;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AdService {
    public Ad createAdWithPhotos(AdRequestDto dto, List<MultipartFile> photos) throws IOException;
    public List<Ad> getAllActiveAds();
    public UserActiveAdDto getUserActiveAds(Long adId);

}
