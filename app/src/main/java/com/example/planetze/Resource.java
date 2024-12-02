package com.example.planetze;

public class Resource {
    private String type;
    private String title;
    private String description;
    private String url;
    private String image;

    public Resource(String type, String title, String description, String url, String image) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.url = url;
        this.image = image;
    }

    public String getType() { return type; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getUrl() { return url; }
    public String getImage() { return image; }
}

