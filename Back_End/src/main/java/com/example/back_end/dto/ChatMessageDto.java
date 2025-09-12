package com.example.back_end.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {
    private Long adId;
    private Long senderId;
    private Long receiverId;
    private String senderName;
    private String content;
    private Date sentDate;
}
