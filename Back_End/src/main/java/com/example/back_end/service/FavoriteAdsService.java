package com.example.back_end.service;

import com.example.back_end.entity.Ad;
import com.example.back_end.entity.Favorite;
import com.example.back_end.entity.User;

import java.util.List;

public interface FavoriteAdsService {
    Favorite addFavorite(User user, Ad ad);
    void removeFavorite(User user, Ad ad);
    List<Ad> getFavoritesByUser(User user);
}
