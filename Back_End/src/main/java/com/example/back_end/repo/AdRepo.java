package com.example.back_end.repo;

import com.example.back_end.dto.ActiveAdDto;
import com.example.back_end.dto.PendingAdDto;
import com.example.back_end.entity.Ad;
import com.example.back_end.entity.AdPhoto;
import com.example.back_end.entity.AdStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdRepo extends JpaRepository<Ad, Long> {

    @Query("SELECT new com.example.back_end.dto.PendingAdDto(a.adId, a.title, a.description, a.location, a.price, a.status, a.user.username) " +
            "FROM Ad a WHERE a.status = 'PENDING'")
    List<PendingAdDto> findAllPendingAds();

    List<Ad> findByStatus(AdStatus status);

    @Query("SELECT new com.example.back_end.dto.ActiveAdDto(a.adId, a.title, a.description, a.location, a.price, a.status, a.user.username) " +
            "FROM Ad a WHERE a.status = 'ACTIVE'")
    List<ActiveAdDto> findAllActiveAds();


    @Query("SELECT a FROM Ad a WHERE a.status = 'ACTIVE' AND (:keyword IS NULL OR LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(a.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND (:categoryId IS NULL OR a.category.categoryId = :categoryId) AND (:brand IS NULL OR LOWER(a.vehicleModel.brand) = LOWER(:brand)) AND (:model IS NULL OR a.vehicleModel.model = :model) AND (:location IS NULL OR LOWER(a.location) LIKE LOWER(CONCAT('%', :location, '%')))")
    List<Ad> searchAds(@Param("keyword") String keyword, @Param("categoryId") Long categoryId, @Param("brand") String brand, @Param("model") String model, @Param("location") String location);

    @Query("SELECT a FROM Ad a WHERE a.user.userId = :userId")
    List<Ad> findByUserId(@Param("userId") Long userId);

    List<Ad> findByUserUserId(Long userId);

    @Query("SELECT a.photos FROM Ad a WHERE a.adId = :adId")
    List<AdPhoto> findPhotosByAdId(@Param("adId") Long adId);

    long countByStatus(AdStatus status);
}
