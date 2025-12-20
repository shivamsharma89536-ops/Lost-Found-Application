package com.Android.Android_App.Service;

import com.Android.Android_App.Dto.ClaimResponse;
import com.Android.Android_App.Entity.Claim;
import com.Android.Android_App.Entity.ClaimStatus;
import com.Android.Android_App.Entity.Conversation; // <-- Add this import
import com.Android.Android_App.Entity.Item;
import com.Android.Android_App.Entity.User;
import com.Android.Android_App.Repository.ClaimRepository;
import com.Android.Android_App.Repository.ItemRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClaimService {

    private final ClaimRepository claimRepository;
    private final ItemRepository itemRepository;
    private final ChatService chatService; // <-- We need the ChatService

    // Update the constructor to include ChatService
    public ClaimService(ClaimRepository claimRepository, ItemRepository itemRepository, ChatService chatService) {
        this.claimRepository = claimRepository;
        this.itemRepository = itemRepository;
        this.chatService = chatService; // <-- Add this
    }

    public ClaimResponse createClaim(Long itemId, User claimant) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        Claim claim = new Claim();
        claim.setItem(item);
        claim.setClaimant(claimant);
        claim.setOwner(item.getPostedBy()); // The owner is who posted the item
        claim.setStatus(ClaimStatus.PENDING);
        claim.setCreatedAt(LocalDateTime.now());

        Claim savedClaim = claimRepository.save(claim);

        // --- THIS IS THE FIX ---
        // Create a new conversation between the claimant and the item owner
        Conversation convo = chatService.createConversation(claimant, item.getPostedBy());
        Long conversationId = convo.getId();
        
        // We return the new, real conversation ID
        return new ClaimResponse(savedClaim.getId(), conversationId, savedClaim.getStatus().toString());
    }

    public List<Claim> getClaimsForUser(User claimant) {
        return claimRepository.findByClaimant(claimant);
    }
}