package com.aashushaikh.chat.controller;

import com.aashushaikh.chat.dto.ApiResponse;
import com.aashushaikh.chat.dto.ChatResponse;
import com.aashushaikh.chat.dto.CreateChatRequest;
import com.aashushaikh.chat.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/")
    public ResponseEntity<ApiResponse<ChatResponse>> getOrCreateDirectChat(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CreateChatRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success("Chat ready", chatService.getOrCreateDirectChat(jwt.getSubject(), request)));
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<ChatResponse>>> getMyChats(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(
                ApiResponse.success("Chats fetched", chatService.getMyChats(jwt.getSubject())));
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<ApiResponse<ChatResponse>> getChatById(
            @PathVariable String chatId,
            @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(
                ApiResponse.success("Chat fetched", chatService.getChatById(chatId, jwt.getSubject())));
    }
}
