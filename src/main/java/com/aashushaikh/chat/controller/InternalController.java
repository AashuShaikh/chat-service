package com.aashushaikh.chat.controller;

import com.aashushaikh.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class InternalController {

    private final ChatService chatService;

    @GetMapping("/{chatId}/is-member")
    public ResponseEntity<Boolean> isMember(
            @PathVariable String chatId,
            @RequestParam String userId) {
        return ResponseEntity.ok(chatService.isMember(chatId, userId));
    }
}
