package com.example.lostandfoundapp.models;

import com.example.lostandfoundapp.models.Message;
import com.example.lostandfoundapp.models.User;
import java.util.Set;
public class Conversation {
    private String id;
    private Set<User> participants;

    private Message lastMessage;

    // Getters
    public String getId() { return id; }
    public Message getLastMessage() { return lastMessage; }
    public Set<User> getParticipants() { return participants; }

}