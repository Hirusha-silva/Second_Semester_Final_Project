package com.example.back_end.service;

import com.example.back_end.dto.MailRequestDto;
import jakarta.mail.MessagingException;

public interface EmailServices {
    public void sendMail(MailRequestDto request) throws MessagingException;
}
