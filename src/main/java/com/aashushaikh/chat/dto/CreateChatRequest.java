package com.aashushaikh.chat.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateChatRequest {

    @NotBlank(message = "Recipient ID is required")
    private String recipientId;
}
