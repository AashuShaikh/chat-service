package com.aashushaikh.chat.client;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserProfileDto {
    private String id;
    private String username;
    private String displayName;
    private String profilePicture;
    private String status; // "ONLINE" | "OFFLINE" | "AWAY"
}
