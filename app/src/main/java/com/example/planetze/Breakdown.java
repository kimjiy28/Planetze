package com.example.planetze;

public class Breakdown  {
    String id;
    String category;
    String activity;
    int emission;

    public Breakdown() {
    }

    public Breakdown(String category, String activity, int emission) {
        this.category = category;
        this.activity = activity;
        this.emission = emission;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public int getEmission() {
        return emission;
    }

    public void setEmission(int emission) {
        this.emission = emission;
    }

}

