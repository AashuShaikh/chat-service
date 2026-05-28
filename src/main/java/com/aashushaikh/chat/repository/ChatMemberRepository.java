package com.aashushaikh.chat.repository;

import com.aashushaikh.chat.model.ChatMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMemberRepository extends JpaRepository<ChatMember, String> {

    List<ChatMember> findByChatId(String chatId);

    boolean existsByChatIdAndUserId(String chatId, String userId);

    List<ChatMember> findByChatIdIn(List<String> chatIds);
}
