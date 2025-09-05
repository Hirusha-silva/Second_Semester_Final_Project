package com.example.back_end.service.impl;

import com.example.back_end.entity.Category;
import com.example.back_end.repo.CategoryRepo;
import com.example.back_end.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepo categoryRepo;

    @Override
    public List<Category> getAllCategory() {
        return categoryRepo.findAll();
    }
}
