package com.Android.Android_App.Repository;

import com.Android.Android_App.Entity.Claim;
import com.Android.Android_App.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClaimRepository extends JpaRepository<Claim, Long> {
    // For the "My Claims" screen
    List<Claim> findByClaimant(User claimant);
}