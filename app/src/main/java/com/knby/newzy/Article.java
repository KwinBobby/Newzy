package com.knby.newzy;

public class Article {

    // A class which is used to store the various data members required

    private String imageUrl;
    private String title;
    private String URL;
    private String sortBy;
    private String id;

    //boom
    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getURL() {
        return URL;

    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
