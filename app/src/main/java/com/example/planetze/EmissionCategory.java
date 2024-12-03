package com.example.planetze;

public class EmissionCategory {
    private String category;
    private Double percentage;

    public EmissionCategory(String category, Double percentage) {
        this.category = category;
        this.percentage = percentage;
    }

    public String getCategory() {
        return category;
    }

    public Double getPercentage() {
        return percentage;
    }
}

