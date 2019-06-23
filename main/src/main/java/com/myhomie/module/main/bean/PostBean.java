package com.myhomie.module.main.bean;

import androidx.annotation.NonNull;

public class PostBean {
    private Integer id;
    private String title;
    private String createdTime;

    public PostBean() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    @NonNull
    @Override
    public String toString() {
        return "PostBean{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", createdTime='" + createdTime + '\'' +
                '}';
    }
}
