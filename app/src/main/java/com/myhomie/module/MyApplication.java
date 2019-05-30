package com.myhomie.module;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.myhomie.module.common.base.BaseApplication;

/**
 * <p>这里仅需做一些初始化的工作</p>
 *
 * @author 2019/5/29
 * @version V1.0.0
 */
public class MyApplication extends BaseApplication {


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // dex突破65535的限制
        MultiDex.install(this);
    }
}
