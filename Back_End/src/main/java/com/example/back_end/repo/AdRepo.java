package com.example.back_end.repo;

import com.example.back_end.entity.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdRepo extends JpaRepository<Ad, Long> {
}
