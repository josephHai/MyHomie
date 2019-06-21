package com.myhomie.module.login.data.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import com.myhomie.module.common.MyDBHelper;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {
    private MyDBHelper dbHelper;
    private SQLiteDatabase db;

    private String id;
    private String username;
    private String password;
    private String nickname;
    private String avatar;

    public LoggedInUser() {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(MyDBHelper.TABLE_USER, MyDBHelper.TABLE_USER_FIELDS, null,
                null, null, null, null);
        if (cursor.moveToFirst()) {
            this.id = cursor.getString(0);
            this.username = cursor.getString(1);
            this.password = cursor.getString(2);
            this.nickname = cursor.getString(3);
            this.avatar = cursor.getString(4);
        }
        cursor.close();
    }

    public LoggedInUser(String id, String username, String password, String nickname, String avatar) {
        db = dbHelper.getReadableDatabase();
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void insert() {
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("username", username);
        values.put("password", password);
        values.put("nickname", nickname);
        values.put("avatar", avatar);
        db.insert(MyDBHelper.TABLE_USER, null, values);
        db.close();
    }

    @NonNull
    @Override
    public String toString() {
        return "LoggedInUser{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
