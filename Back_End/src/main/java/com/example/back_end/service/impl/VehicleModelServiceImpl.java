package com.example.back_end.service.impl;

import com.example.back_end.entity.VehicleModel;
import com.example.back_end.repo.VehicleModelRepo;
import com.example.back_end.service.VehicalModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class VehicleModelServiceImpl implements VehicalModelService {
    private final VehicleModelRepo vehicleModelRepo;
    @Override
    public List<VehicleModel> getAllVehicalModels() {
        return vehicleModelRepo.findAll();
    }
}
