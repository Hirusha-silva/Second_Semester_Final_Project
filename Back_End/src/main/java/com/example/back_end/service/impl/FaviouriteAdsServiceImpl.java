package com.example.back_end.service.impl;

import com.example.back_end.entity.Ad;
import com.example.back_end.entity.Favorite;
import com.example.back_end.entity.User;
import com.example.back_end.repo.FavoriteAdsRepo;
import com.example.back_end.service.FavoriteAdsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
@RequiredArgsConstructor
public class FaviouriteAdsServiceImpl implements FavoriteAdsService {
    private final FavoriteAdsRepo favoriteAdsRepo;
    @Override
    public Favorite addFavorite(User user, Ad ad) {
        return favoriteAdsRepo.findByUserAndAd(user, ad)
                .orElseGet(() -> {
                    Favorite favorite = Favorite.builder()
                            .user(user)
                            .ad(ad)
                            .createdDate(new Date())
                            .build();
                    return favoriteAdsRepo.save(favorite);
                });
    }

    @Override
    public void removeFavorite(User user, Ad ad) {
        favoriteAdsRepo.findByUserAndAd(user, ad)
                .ifPresent(favoriteAdsRepo::delete);
    }

    @Override
    public List<Ad> getFavoritesByUser(User user) {
        return favoriteAdsRepo.findAllByUser(user).stream().map(Favorite::getAd).toList();
    }
}
