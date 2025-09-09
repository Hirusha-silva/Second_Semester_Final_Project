package com.example.back_end.repo;

import com.example.back_end.dto.ActiveAdDto;
import com.example.back_end.dto.PendingAdDto;
import com.example.back_end.entity.Ad;
import com.example.back_end.entity.AdStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdRepo extends JpaRepository<Ad, Long> {
    List<Ad> findByCategory_CategoryId(Long categoryId);

    // Example: search by title (contains keyword)
    List<Ad> findByTitleContainingIgnoreCase(String keyword);

    // Example: find ads by user (seller)
    List<Ad> findByUser_UserId(Long userId);

    @Query("SELECT new com.example.back_end.dto.PendingAdDto(a.adId, a.title, a.description, a.location, a.price, a.status, a.user.username) " +
            "FROM Ad a WHERE a.status = 'PENDING'")
    List<PendingAdDto> findAllPendingAds();

    List<Ad> findByStatus(AdStatus status);

    @Query("SELECT new com.example.back_end.dto.ActiveAdDto(a.adId, a.title, a.description, a.location, a.price, a.status, a.user.username) " +
            "FROM Ad a WHERE a.status = 'ACTIVE'")
    List<ActiveAdDto> findAllActiveAds();
}
