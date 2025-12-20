package com.Android.Android_App.Dto;

// This is the response for Module 3's "Claim" button
public class ClaimResponse {
    private Long claimId;
    private Long conversationId; // This is for Module 5
    private String status;

    public ClaimResponse(Long claimId, Long conversationId, String status) {
        this.claimId = claimId;
        this.conversationId = conversationId;
        this.status = status;
    }
    // Getters
    public Long getClaimId() { return claimId; }
    public Long getConversationId() { return conversationId; }
    public String getStatus() { return status; }
}