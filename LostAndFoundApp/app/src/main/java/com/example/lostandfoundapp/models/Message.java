package com.example.lostandfoundapp.models;

public class Message {
    private String id;
    private String conversationId;
    private String senderId;
    private String content;
    private String imageUrl;
    private String createdAt;
    private boolean readStatus;

    // Getters
    public String getId() { return id; }
    public String getConversationId() { return conversationId; }
    public String getSenderId() { return senderId; }
    public String getContent() { return content; }
    public String getImageUrl() { return imageUrl; }
    public String getCreatedAt() { return createdAt; }
    public boolean isReadStatus() { return readStatus; }


    public Message(String conversationId, String senderId, String content) {
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.content = content;
    }
}