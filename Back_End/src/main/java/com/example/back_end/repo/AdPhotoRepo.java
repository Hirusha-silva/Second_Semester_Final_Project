package com.example.back_end.repo;

import com.example.back_end.entity.AdPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdPhotoRepo extends JpaRepository<AdPhoto, Long> {
    List<AdPhoto> findByAd_AdId(Long adId);
}
