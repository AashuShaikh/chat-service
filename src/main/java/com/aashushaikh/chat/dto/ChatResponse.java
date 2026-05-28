package com.aashushaikh.chat.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ChatResponse {
    private String id;
    private List<ChatMemberResponse> members;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
