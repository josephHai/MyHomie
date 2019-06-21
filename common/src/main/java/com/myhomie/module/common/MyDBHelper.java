package com.myhomie.module.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.myhomie.module.common.utils.SqlUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class MyDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "myApp.db";
    private static final int DB_VERSION = 1;
    public static final String TABLE_USER = "user";
    public static final String[] TABLE_USER_FIELDS = {"id", "username", "password", "nickname", "avatar"};

    private String conditionStr = "";

    private static final String USER_CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_USER + "("
            + "id INTEGER PRIMARY KEY,"
            + "username CHAR(20) not null,"
            + "password CHAR(20) not null,"
            + "nickname CHAR(100) not null,"
            + "avatar CHAR(200)"
            + ")";

    public MyDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public MyDBHelper where(String conditionStr) {
        this.conditionStr = conditionStr;
        return this;
    }

    public void insert(String tableName, Object object) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + tableName);

        SqlUtils sqlUtils = new SqlUtils();
        ContentValues values = sqlUtils.parseObj(object);

        String sql = "SELECT * FROM " + tableName + " WHERE id = " + values.get("id");
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.getCount() == 0) {
            db.insert(tableName, null, values);
        } else {
            db.update(tableName, values, "id = ?", new String[] {values.get("id").toString()});
        }

        cursor.close();
    }

    public <T> List<T> find(Class<T> clazz) {
        String sql = "SELECT * FROM " + clazz.getSimpleName().toLowerCase() + conditionStr;
        Cursor cursor = this.getReadableDatabase().rawQuery(sql, null);
        Field[] fields = clazz.getDeclaredFields();
        List<String> fieldNames = new ArrayList<>();

        for (Field field : fields) {
            fieldNames.add(field.getName());
        }
        List<Method> setMethods = getSetMethods(clazz);

        List<T> list = getList(clazz, cursor, setMethods, fieldNames, fields);
        cursor.close();
        conditionStr = "";
        return list;
    }

    private <T> List<T> getList(Class clazz, Cursor cursor, List<Method> setMethods, List<String> fieldNames, Field[] fields) {
        List<T> list = new ArrayList<>();
        Constructor<?> constructor = findBestSuitConstructor(clazz);
        while (cursor.moveToNext()) {
            T data = null;
            try {
                data = (T) constructor.newInstance();
            }catch (InstantiationException | IllegalAccessException | InvocationTargetException e){
                e.printStackTrace();
            }

            for (Method method : setMethods) {
                String name = method.getName();
                String valueName = name.substring(3).substring(0, 1).toLowerCase() + name.substring(4);
                String type = null;
                int index = 0;
                if (fieldNames.contains(valueName)) {
                    index = fieldNames.indexOf(valueName);
                    type = fields[index].getGenericType().toString();
                }
                Object value = new Object();
                if (type != null) {
                    if (type.contains("String")) {
                        value = cursor.getString(cursor.getColumnIndex(valueName.toLowerCase()));
                    } else if (type.equals("int")) {
                        value = cursor.getInt(cursor.getColumnIndex(valueName.toLowerCase()));
                    } else if (type.equals("double")) {
                        value = cursor.getDouble(cursor.getColumnIndex(valueName.toLowerCase()));
                    } else if (type.equals("float")) {
                        value = cursor.getFloat(cursor.getColumnIndex(valueName.toLowerCase()));
                    } else if (type.equals("boolean")) {
                        value = cursor.getInt(cursor.getColumnIndex(valueName.toLowerCase())) == 1;
                    } else if (type.equals("long")) {
                        value = cursor.getLong(cursor.getColumnIndex(valueName.toLowerCase()));
                    } else if (type.equals("short")) {
                        value = cursor.getShort(cursor.getColumnIndex(valueName.toLowerCase()));
                    }
                    try {
                        fields[index].setAccessible(true);
                        fields[index].set(data, value);
                    } catch (IllegalAccessException e) {
                        Log.e("data", e.toString());
                    }
                }
                list.add(data);
            }

            cursor.close();
        }
        return list;
    }

    private List<Method> getSetMethods(Class clazz) {
        List<Method> setMethods = new ArrayList<>();
        for (Method method : clazz.getMethods()) {
            String name = method.getName();
            if (name.contains("set") && !name.equals("offset")) {
                setMethods.add(method);
            }
        }
        return setMethods;
    }

    private Constructor<?> findBestSuitConstructor(Class<?> modelClass) {
        Constructor<?> finalConstructor = null;
        Constructor<?>[] constructors = modelClass.getConstructors();
        for (Constructor<?> constructor : constructors) {
            if (finalConstructor == null) {
                finalConstructor = constructor;
            } else {
                int finalParamLength = finalConstructor.getParameterTypes().length;
                int newParamLength = constructor.getParameterTypes().length;
                if (newParamLength < finalParamLength) {
                    finalConstructor = constructor;
                }
            }
        }
        assert finalConstructor != null;
        finalConstructor.setAccessible(true);
        return finalConstructor;
    }
}
