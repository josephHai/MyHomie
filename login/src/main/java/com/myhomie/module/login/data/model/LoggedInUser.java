package com.myhomie.module.login.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String id;
    private String nickname;

    public LoggedInUser(String id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    public String getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }
}
