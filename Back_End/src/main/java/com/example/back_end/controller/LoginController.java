package com.example.back_end.controller;

import com.example.back_end.dto.ApiResponseDto;
import com.example.back_end.dto.LoginDto;
import com.example.back_end.dto.RegisterDto;
import com.example.back_end.service.UserService;
import com.example.back_end.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin
public class LoginController {
    private final UserService userService;
    private final UserServiceImpl userServiceImpl;

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
