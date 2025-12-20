package com.Android.Android_App.Repository;

import com.Android.Android_App.Entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    // Matches: GET /api/conversations/{id}/messages
    List<Message> findByConversationId(Long conversationId);
}