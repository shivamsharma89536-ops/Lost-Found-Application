package com.Android.Android_App.Repository;

import com.Android.Android_App.Entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    // TODO: Add query to find by participants
}