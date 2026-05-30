package com.aashushaikh.chat.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChatMemberResponse {
    private String userId;
    private String username;
    private String displayName;
    private String profilePicture;
    private LocalDateTime joinedAt;
}
