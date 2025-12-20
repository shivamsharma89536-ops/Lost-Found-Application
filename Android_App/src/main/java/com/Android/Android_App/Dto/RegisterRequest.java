package com.Android.Android_App.Dto;

// This class models the JSON for a registration request
public class RegisterRequest {
    private String name;
    private String email;
    private String phone;
    private String password;
    // Getters
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getPassword() { return password; }
}