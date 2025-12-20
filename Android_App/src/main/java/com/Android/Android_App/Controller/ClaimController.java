package com.Android.Android_App.Controller;

import com.Android.Android_App.Dto.ClaimResponse;
import com.Android.Android_App.Entity.Claim;
import com.Android.Android_App.Entity.User;
import com.Android.Android_App.Service.ClaimService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ClaimController {

    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    // --- Module 3: "Claim Item" button ---
    // Matches: POST /api/items/{id}/claim
    @PostMapping("/api/items/{id}/claim")
    public ResponseEntity<ClaimResponse> claimItem(
            @PathVariable("id") Long itemId,
            @AuthenticationPrincipal User currentUser) {
        
        ClaimResponse response = claimService.createClaim(itemId, currentUser);
        return ResponseEntity.ok(response);
    }

    // --- Module 4: "My Claims" screen ---
    // Matches: GET /api/claims
    @GetMapping("/api/claims")
    public ResponseEntity<List<Claim>> getMyClaims(@AuthenticationPrincipal User currentUser) {
        List<Claim> claims = claimService.getClaimsForUser(currentUser);
        return ResponseEntity.ok(claims);
    }

    // TODO: Add PATCH /api/claims/{id} for Admin approval
}