package com.Android.Android_App.Controller;

import com.Android.Android_App.Dto.ChatMessageDto;
import com.Android.Android_App.Entity.Conversation;
import com.Android.Android_App.Entity.Message;
import com.Android.Android_App.Service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate; // For sending messages to clients

    // --- WebSocket Message Handler ---
    // Clients send messages to "/app/chat"
    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessageDto chatMessage) {
        Message savedMessage = chatService.saveMessage(chatMessage);
        
        // Broadcast the message to everyone subscribed to this conversation
        // Clients will subscribe to "/topic/conversation/{conversationId}"
        messagingTemplate.convertAndSend("/topic/conversation/" + savedMessage.getConversation().getId(), savedMessage);
    }

    // --- REST API for Conversation History ---
    
    // Matches: GET /api/conversations
    @GetMapping("/api/conversations")
    @ResponseBody
    public ResponseEntity<List<Conversation>> getMyConversations() {
        // TODO: Get logged in user
        // List<Conversation> convos = chatService.getConversationsForUser(currentUser);
        // return ResponseEntity.ok(convos);
        return ResponseEntity.ok(chatService.getAllConversations()); // Placeholder
    }

    // Matches: GET /api/conversations/{id}/messages
    @GetMapping("/api/conversations/{id}/messages")
    @ResponseBody
    public ResponseEntity<List<Message>> getMessageHistory(@PathVariable("id") Long conversationId) {
        return ResponseEntity.ok(chatService.getMessagesForConversation(conversationId));
    }
}