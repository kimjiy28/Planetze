package com.example.planetze;

public class EmissionCategory {
    private String category;
    private int percentage;

    public EmissionCategory(String category, int percentage) {
        this.category = category;
        this.percentage = percentage;
    }

    public String getCategory() {
        return category;
    }

    public int getPercentage() {
        return percentage;
    }
}

