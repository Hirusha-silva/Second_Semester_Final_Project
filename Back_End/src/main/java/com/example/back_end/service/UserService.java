package com.example.back_end.service;

import com.example.back_end.dto.RegisterDto;

public interface UserService {
    public String registerUser(RegisterDto registerDto);
}
