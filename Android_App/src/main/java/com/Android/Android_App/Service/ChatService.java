package com.Android.Android_App.Service;

import com.Android.Android_App.Dto.ChatMessageDto;
import com.Android.Android_App.Entity.Conversation;
import com.Android.Android_App.Entity.Message;
import com.Android.Android_App.Entity.User;
import com.Android.Android_App.Repository.ConversationRepository;
import com.Android.Android_App.Repository.MessageRepository;
import com.Android.Android_App.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

@Service
public class ChatService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository; // To find users

    public ChatService(MessageRepository messageRepository, ConversationRepository conversationRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
    }

    public Message saveMessage(ChatMessageDto chatMessage) {
        // 1. Convert String IDs to Long
        Long convoId = Long.parseLong(chatMessage.getConversationId());
        Long senderId = Long.parseLong(chatMessage.getSenderId());

        // 2. Find the entities
        Conversation conversation = conversationRepository.findById(convoId).orElseThrow();
        User sender = userRepository.findById(senderId).orElseThrow();

        // 3. Create and save the message
        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setContent(chatMessage.getContent());
        message.setCreatedAt(LocalDateTime.now());
        
        return messageRepository.save(message);
    }
    
    public List<Message> getMessagesForConversation(Long conversationId) {
        return messageRepository.findByConversationId(conversationId);
    }
    
    public List<Conversation> getAllConversations() {
        return conversationRepository.findAll(); // Placeholder
    }
    
    // Implement getConversationsForUser(User user)
    public Conversation createConversation(User user1, User user2) {
        Conversation conversation = new Conversation();
        
        Set<User> participants = new HashSet<>();
        participants.add(user1);
        participants.add(user2);
        
        conversation.setParticipants(participants);
        return conversationRepository.save(conversation);
    }
}