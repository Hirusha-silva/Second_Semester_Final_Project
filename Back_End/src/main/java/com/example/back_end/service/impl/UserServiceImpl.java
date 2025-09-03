package com.example.back_end.service.impl;

import com.example.back_end.dto.RegisterDto;
import com.example.back_end.entity.Role;
import com.example.back_end.entity.User;
import com.example.back_end.repo.UserRepo;
import com.example.back_end.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

   // User Register
    @Override
    public String registerUser(RegisterDto registerDto) {

        if (userRepo.findByUsername(registerDto.getUsername()).isPresent()) {
            throw new RuntimeException("Username is already in use");
        }

        User user = User.builder()
                .username(registerDto.getUsername())
                .name(registerDto.getName())
                .email(registerDto.getEmail())
                .phone(registerDto.getPhone())
                .address(registerDto.getAddress())
                .role(Role.valueOf(registerDto.getRole()))
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .build();

        userRepo.save(user);

        return "User registered successfully";
    }
}
