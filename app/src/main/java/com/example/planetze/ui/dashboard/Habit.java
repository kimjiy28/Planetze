package com.example.planetze.ui.dashboard;

public class Habit {
    private String id; // Firestore document ID
    private String name; // Name of the habit
    private String description; // Description of the habit
    private String category; // Category of the habit
    private int impactLevel; // Impact level (e.g., High, Medium, Low)
    private int daysCompleted; // Number of days the habit has been completed
    private String status;

    // No-argument constructor required for Firestore
    public Habit() {
    }

    // Constructor
    public Habit(String name, String description, String category, int impactLevel, int daysCompleted) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.impactLevel = impactLevel;
        this.daysCompleted = daysCompleted;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getImpactLevel() {
        return impactLevel;
    }

    public void setImpactLevel(Integer impactLevel) {
        this.impactLevel = impactLevel;
    }

    public int getDaysCompleted() {
        return daysCompleted;
    }

    public void setDaysCompleted(int daysCompleted) {
        this.daysCompleted = daysCompleted;
    }
}
