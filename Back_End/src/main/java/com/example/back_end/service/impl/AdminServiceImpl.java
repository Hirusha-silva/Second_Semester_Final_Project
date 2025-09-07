package com.example.back_end.service.impl;

import com.example.back_end.dto.UserSummaryDto;
import com.example.back_end.repo.UserRepo;
import com.example.back_end.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepo userRepo;
    @Override
    public List<UserSummaryDto> getAllUsersSummary() {
        return userRepo.findAllUserSummaries()
                .stream()
                .map(obj -> new UserSummaryDto(
                        (String) obj[0],
                        (String) obj[1],
                        (String) obj[2],
                        (String) obj[3]
                ))
                .collect(Collectors.toList());
    }

//    @Override
//    public UserSummaryDto getUserSummaryById(Long id) {
//       return userRepo.findUserSummaryById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
//    }
}
