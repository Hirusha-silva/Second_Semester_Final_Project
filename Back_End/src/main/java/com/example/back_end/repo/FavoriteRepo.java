package com.example.back_end.repo;

import com.example.back_end.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepo extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUser_UserId(Long userId);

    // check if ad is already in favorites
    boolean existsByUser_UserIdAndAd_AdId(Long userId, Long adId);
}
