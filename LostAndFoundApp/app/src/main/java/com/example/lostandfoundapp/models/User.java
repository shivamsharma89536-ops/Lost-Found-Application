package com.example.lostandfoundapp.models;


public class User {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String profileImageUrl;
    private String role;

    // Getters and Setters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getProfileImageUrl() { return profileImageUrl; }
    public String getRole() { return role; }

    // Setters (needed for update)
    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
}
