package com.aashushaikh.chat.service;

import com.aashushaikh.chat.dto.ChatResponse;
import com.aashushaikh.chat.dto.CreateChatRequest;

import java.util.List;

public interface ChatService {
    ChatResponse getOrCreateDirectChat(String currentUserId, CreateChatRequest request);
    List<ChatResponse> getMyChats(String userId);
    ChatResponse getChatById(String chatId, String userId);
    boolean isMember(String chatId, String userId);
}
