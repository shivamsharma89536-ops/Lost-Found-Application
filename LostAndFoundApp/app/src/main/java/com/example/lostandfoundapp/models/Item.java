package com.example.lostandfoundapp.models;


public class Item {
    private String id;
    private String title;
    private String description;
    private String type;
    private String imageUrl;
    private String category;
    private String date;
    private String location;
    private User postedBy;

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getType() { return type; }
    public String getImageUrl() { return imageUrl; }
    public String getCategory() { return category; }
    public String getDate() { return date; }
    public String getLocation() { return location; }
    public User getPostedBy() { return postedBy; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(String category) { this.category = category; }
    public void setDate(String date) { this.date = date; }
    public void setLocation(String location) { this.location = location; }
    public void setType(String type) { this.type = type; }
}