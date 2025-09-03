package com.example.back_end.dto;

import lombok.Data;

@Data
public class RegisterDto {
    private Long userId;
    private String name;
    private String username;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String role;

}
