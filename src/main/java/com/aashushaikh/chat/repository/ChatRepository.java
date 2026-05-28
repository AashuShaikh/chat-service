package com.aashushaikh.chat.repository;

import com.aashushaikh.chat.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, String> {

    @Query("SELECT c FROM Chat c JOIN ChatMember cm ON c.id = cm.chatId WHERE cm.userId = :userId")
    List<Chat> findAllByUserId(@Param("userId") String userId);

    Optional<Chat> findByDirectChatKey(String directChatKey);
}
