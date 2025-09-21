package com.example.back_end.controller;

import com.example.back_end.dto.*;
import com.example.back_end.entity.User;
import com.example.back_end.service.UserService;
import com.example.back_end.service.impl.EmailService;
import com.example.back_end.service.impl.EmailServiceImpl;
import com.example.back_end.service.impl.UserServiceImpl;
import com.example.back_end.util.JwtUtil;
import jakarta.mail.MessagingException;
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
    private final EmailService emailService;
    private final EmailServiceImpl emailServiceImpl;

//    @PostMapping("/register")
//    public ResponseEntity<ApiResponseDto> register(@RequestBody RegisterDto registerDto) {
//       emailService.sendEmail(registerDto.getEmail(),
//               "Welcome to Our Website!",
//               "Hello "+registerDto.getName() + ",\n\n"+"Thank you for registering with us. We're excited to have you onboard!"
//               );
//        return ResponseEntity.ok(
//                new ApiResponseDto(
//                        200,
//                        "User Registered Successfully",
//                        userService.registerUser(registerDto)
//                )
//        );
//    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto> register(@RequestBody RegisterDto registerDto) {
        // Register user first

        // Prepare HTML email
        String htmlBody = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "  body { font-family: Arial, sans-serif; background-color: #f0f4f8; margin:0; padding:0; }" +
                "  .container { max-width: 600px; margin: 40px auto; background: #ffffff; border-radius: 15px; box-shadow: 0 4px 15px rgba(0,0,0,0.1); overflow: hidden; }" +
                "  .header { background: linear-gradient(135deg, #3b82f6, #1d4ed8); color: white; padding: 30px; text-align: center; font-size: 1.5rem; font-weight: bold; }" +
                "  .content { padding: 30px; color: #1e293b; line-height: 1.6; }" +
                "  .btn { display: inline-block; margin-top: 20px; padding: 12px 25px; background: linear-gradient(135deg, #3b82f6, #1d4ed8); color: #fff; text-decoration: none; border-radius: 12px; font-weight: bold; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "  <div class='container'>" +
                "    <div class='header'>Welcome to SpareWay !</div>" +
                "    <div class='content'>" +
                "      <p>Hello " + registerDto.getName() + ",</p>" +
                "      <p>Thank you for registering with SpareWay! We're thrilled to have you join our community of users who are passionate about finding and sharing quality spare parts easily and efficiently.</p>" +
                "      <p>With your new account, you'll be able to explore a wide range of products, post your own ads, communicate with other members, and take advantage of our latest features designed to make your experience seamless and enjoyable.</p>" +
                "      <p>We hope you enjoy all the benefits of being a part of SpareWay. If you have any questions, feel free to reach out to our support team — we're always here to help!</p>" +
                "      <p style='margin-top:20px;'>Best regards,<br>The SpareWay Team</p>" +
                "    </div>" +
                "  </div>" +
                "</body>" +
                "</html>";

        MailRequestDto mailRequest = new MailRequestDto();
        mailRequest.setTo(registerDto.getEmail());
        mailRequest.setSubject("Welcome to Our Website!");
        mailRequest.setBody(htmlBody);

        try {
            emailServiceImpl.sendMail(mailRequest);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

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
