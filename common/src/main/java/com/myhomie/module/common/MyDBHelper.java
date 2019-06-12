package com.myhomie.module.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MyDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "myApp.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_USER = "user";

    private static final String USER_CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_USER + "("
            + "id INTEGER PRIMARY KEY,"
            + "username CHAR(20) not null,"
            + "password CHAR(20) not null,"
            + "nickname CHAR(100) not null,"
            + "avatar CHAR(200)"
            + ")";

    MyDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
