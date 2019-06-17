package com.myhomie.module.main.bean;

public class PostBean {
    private String content;
    private int imageId;

    public PostBean(String content, int imageId) {
        this.content = content;
        this.imageId = imageId;
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
