package com.Android.Android_App.Dto;

// This models the WebSocket message
public class ChatMessageDto {
    private String content;
    private String conversationId; 
    private String senderId;
    // Getters and Setters...
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getConversationId() { return conversationId; }
    public void setConversationId(String conversationId) { this.conversationId = conversationId; }
    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }
}