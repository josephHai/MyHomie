package com.myhomie.module.common.http;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DataType {

    public static final int STRING = 1;
    public static final int JSON_OBJECT = 2;
    public static final int JSON_ARRAY = 3;

    /**
     * 自定义状态注解
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STRING, JSON_OBJECT, JSON_ARRAY})
    public @interface Type {}
}
