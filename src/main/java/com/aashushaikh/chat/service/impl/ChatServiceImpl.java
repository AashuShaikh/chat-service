package com.aashushaikh.chat.service.impl;

import com.aashushaikh.chat.client.UserServiceClient;
import com.aashushaikh.chat.dto.ChatMemberResponse;
import com.aashushaikh.chat.dto.ChatResponse;
import com.aashushaikh.chat.dto.CreateChatRequest;
import com.aashushaikh.chat.exception.ChatNotFoundException;
import com.aashushaikh.chat.model.Chat;
import com.aashushaikh.chat.model.ChatMember;
import com.aashushaikh.chat.repository.ChatMemberRepository;
import com.aashushaikh.chat.repository.ChatRepository;
import com.aashushaikh.chat.service.ChatService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final UserServiceClient userServiceClient;

    @Override
    @Transactional
    public ChatResponse getOrCreateDirectChat(String currentUserId, CreateChatRequest request) {
        String recipientId = request.getRecipientId();

        if (currentUserId.equals(recipientId)) {
            throw new RuntimeException("Cannot create a chat with yourself");
        }

        try {
            userServiceClient.checkUserExists(recipientId);
        } catch (FeignException.NotFound e) {
            throw new RuntimeException("Recipient not found");
        } catch (FeignException e) {
            throw new RuntimeException("User service unavailable");
        }

        String directChatKey = Stream.of(currentUserId, recipientId).sorted().collect(Collectors.joining(":"));

        Chat existing = chatRepository.findByDirectChatKey(directChatKey).orElse(null);
        if (existing != null) {
            return toResponse(existing);
        }

        try {
            Chat chat = chatRepository.save(Chat.builder().directChatKey(directChatKey).build());
            chatMemberRepository.save(ChatMember.builder().chatId(chat.getId()).userId(currentUserId).build());
            chatMemberRepository.save(ChatMember.builder().chatId(chat.getId()).userId(recipientId).build());
            return toResponse(chat);
        } catch (DataIntegrityViolationException e) {
            return toResponse(chatRepository.findByDirectChatKey(directChatKey).orElseThrow());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatResponse> getMyChats(String userId) {
        List<Chat> chats = chatRepository.findAllByUserId(userId);
        if (chats.isEmpty()) return List.of();

        List<String> chatIds = chats.stream().map(Chat::getId).toList();
        Map<String, List<ChatMember>> membersByChatId = chatMemberRepository.findByChatIdIn(chatIds)
                .stream()
                .collect(Collectors.groupingBy(ChatMember::getChatId));

        return chats.stream()
                .map(chat -> toResponse(chat, membersByChatId.getOrDefault(chat.getId(), List.of())))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ChatResponse getChatById(String chatId, String userId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatNotFoundException(chatId));
        if (!chatMemberRepository.existsByChatIdAndUserId(chatId, userId)) {
            throw new ChatNotFoundException(chatId);
        }
        return toResponse(chat);
    }

    @Override
    public boolean isMember(String chatId, String userId) {
        return chatRepository.existsById(chatId)
                && chatMemberRepository.existsByChatIdAndUserId(chatId, userId);
    }

    private ChatResponse toResponse(Chat chat) {
        return toResponse(chat, chatMemberRepository.findByChatId(chat.getId()));
    }

    private ChatResponse toResponse(Chat chat, List<ChatMember> members) {
        List<ChatMemberResponse> memberResponses = members.stream()
                .map(m -> {
                    // Fetch user profile — fail gracefully so a user service hiccup
                    // doesn't break the entire chat list response.
                    String username = null, displayName = null, profilePicture = null;
                    try {
                        var profile = userServiceClient.getUserProfile(m.getUserId());
                        username       = profile.getUsername();
                        displayName    = profile.getDisplayName();
                        profilePicture = profile.getProfilePicture();
                    } catch (Exception ignored) {}

                    return ChatMemberResponse.builder()
                            .userId(m.getUserId())
                            .username(username)
                            .displayName(displayName)
                            .profilePicture(profilePicture)
                            .joinedAt(m.getCreatedAt())
                            .build();
                })
                .toList();

        return ChatResponse.builder()
                .id(chat.getId())
                .members(memberResponses)
                .createdAt(chat.getCreatedAt())
                .updatedAt(chat.getUpdatedAt())
                .build();
    }
}
