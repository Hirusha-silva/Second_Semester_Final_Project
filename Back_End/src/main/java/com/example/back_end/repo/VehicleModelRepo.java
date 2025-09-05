package com.example.back_end.repo;

import com.example.back_end.entity.VehicleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleModelRepo extends JpaRepository<VehicleModel, Long> {
}
