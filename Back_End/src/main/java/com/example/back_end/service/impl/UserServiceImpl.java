package com.example.back_end.service.impl;

import com.example.back_end.dto.LoginDto;
import com.example.back_end.dto.LoginResponseDto;
import com.example.back_end.dto.RegisterDto;
import com.example.back_end.entity.Role;
import com.example.back_end.entity.User;
import com.example.back_end.repo.UserRepo;
import com.example.back_end.service.UserService;
import com.example.back_end.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


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

    public LoginResponseDto authenticate(LoginDto loginDto) {
        User user = userRepo.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new RuntimeException("Username not found")
        );
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }
        String token = jwtUtil.generateToken(user.getUsername());

       return new LoginResponseDto(
               token, user.getUserId(), user.getUsername()
       );
    }
}
