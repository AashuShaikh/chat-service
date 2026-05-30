package com.aashushaikh.chat.client;

import lombok.Data;
import lombok.NoArgsConstructor;

// Mirrors the fields we need from PublicUserResponse (user service).
// Feign deserializes the response into this — we only map what we use.
@Data
@NoArgsConstructor
public class UserProfileDto {
    private String id;
    private String username;
    private String displayName;
    private String profilePicture;
}
