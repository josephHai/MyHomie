package com.myhomie.module.main.bean;

import androidx.annotation.NonNull;

public class PostCardBean {
    private int id;
    private String image;
    private String title;
    private String seen;
    private String author;
    private String createdTime;
    private String category;
    private String location;

    PostCardBean() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    @NonNull
    @Override
    public String toString() {
        return "PostCardBean{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", seen='" + seen + '\'' +
                ", author='" + author + '\'' +
                ", createdTime='" + createdTime + '\'' +
                ", category='" + category + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
