package com.Android.Android_App.Controller;

import com.Android.Android_App.Entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users") // A new, secure base path
public class UserController {

    // This is the method we are moving
    // It now maps to GET /api/users/me
    @GetMapping("/me")
    public ResponseEntity<User> getLoggedInUser(@AuthenticationPrincipal User currentUser) {
        // Now, Spring Security will run the JwtAuthFilter, find the user,
        // and 'currentUser' will no longer be null.
        return ResponseEntity.ok(currentUser);
    }
    
    // TODO: Add PATCH /api/users/me for updating profile here
}