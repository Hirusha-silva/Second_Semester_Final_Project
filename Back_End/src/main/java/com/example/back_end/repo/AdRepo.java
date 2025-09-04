package com.example.back_end.repo;

import com.example.back_end.entity.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdRepo extends JpaRepository<Ad, Long> {
    List<Ad> findByCategory_CategoryId(Long categoryId);

    // Example: search by title (contains keyword)
    List<Ad> findByTitleContainingIgnoreCase(String keyword);

    // Example: find ads by user (seller)
    List<Ad> findByUser_UserId(Long userId);
}
