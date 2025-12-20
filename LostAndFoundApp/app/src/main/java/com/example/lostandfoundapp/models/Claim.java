package com.example.lostandfoundapp.models;

public class Claim {
    private String id;
    private String itemId;
    private String claimantId;
    private String ownerId;
    private String status;
    private String createdAt;



    // Getters
    public String getId() { return id; }
    public String getItemId() { return itemId; }
    public String getClaimantId() { return claimantId; }
    public String getOwnerId() { return ownerId; }
    public String getStatus() { return status; }
    public String getCreatedAt() { return createdAt; }
}