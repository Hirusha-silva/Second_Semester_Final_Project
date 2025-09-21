package com.example.back_end.repo;

import com.example.back_end.entity.Ad;
import com.example.back_end.entity.Favorite;
import com.example.back_end.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteAdsRepo extends JpaRepository<Favorite,Long> {
    Optional<Favorite> findByUserAndAd(User user, Ad ad);
    List<Favorite> findAllByUser(User user);
}
