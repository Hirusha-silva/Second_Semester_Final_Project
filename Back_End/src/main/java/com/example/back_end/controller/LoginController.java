package com.example.back_end.controller;

import com.example.back_end.dto.ApiResponseDto;
import com.example.back_end.dto.LoginDto;
import com.example.back_end.dto.LoginResponseDto;
import com.example.back_end.dto.RegisterDto;
import com.example.back_end.entity.User;
import com.example.back_end.service.UserService;
import com.example.back_end.service.impl.UserServiceImpl;
import com.example.back_end.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin
public class LoginController {
    private final UserService userService;
    private final UserServiceImpl userServiceImpl;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto> register(@RequestBody RegisterDto registerDto) {
        return ResponseEntity.ok(
                new ApiResponseDto(
                        200,
                        "User Registered Successfully",
                        userService.registerUser(registerDto)
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(
                new ApiResponseDto(
                        200,
                        "ok",
                        userServiceImpl.authenticate(loginDto)
                )
        );


    }
}
