package com.myhomie.module.main.bean;

public class PostBean {
    private int id;
    private String content;
    private int imageId;

    public PostBean(int id, String content, int imageId) {
        this.id = id;
        this.content = content;
        this.imageId = imageId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
