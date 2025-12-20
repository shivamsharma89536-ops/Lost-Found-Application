package com.Android.Android_App.Dto;

import com.Android.Android_App.Entity.User; // <-- THIS IS THE FIX

public class AuthResponse {
    private String token;
    private User user; 

    public AuthResponse(String token, User user) {
        this.token = token;
        this.user = user;
    }
    
    // Getters
    public String getToken() { return token; }
    public User getUser() { return user; }
}