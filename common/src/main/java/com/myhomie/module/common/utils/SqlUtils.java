package com.myhomie.module.common.utils;

import android.content.ContentValues;

import java.lang.reflect.Field;

public class SqlUtils {

    public ContentValues parseObj(Object object) {
        Class clazz = object.getClass();
        ContentValues res = new ContentValues();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                if (field.getName().equals("$change") || field.getName().equals("serialVersionUID")){
                    continue;
                }
                String value = field.get(object) == null ? "" : field.get(object).toString();
                res.put(field.getName(), value);
            }catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return res;
    }
}
