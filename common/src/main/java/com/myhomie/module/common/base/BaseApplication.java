package com.myhomie.module.common.base;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.facebook.stetho.Stetho;
import com.myhomie.module.common.utils.Utils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.List;

import okhttp3.OkHttpClient;

/**
 * 要想使用BaseApplication，必须在组件中实现自己的Application，并且继承BaseApplication；
 * 组件中实现的Application必须在debug包中的AndroidManifest.xml中注册，否则无法使用；
 * 组件的Application需置于java/debug文件夹中，不得放于主代码；
 * 组件中获取Context的方法必须为:Utils.getContext()，不允许其他写法；
 *
 * @author 2019/5/29
 * @version V1.0.0
 */
public class BaseApplication extends Application {

    public static final String ROOT_PACKAGE = "com.myhomie.module";

    private static BaseApplication sInstance;

    private List<IApplicationDelegate> mAppDelegateList;

    private String token = "";

    public static BaseApplication getIns() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        Logger.addLogAdapter(new AndroidLogAdapter());
        Utils.init(this);
        mAppDelegateList = ClassUtils.getObjectsWithInterface(this, IApplicationDelegate.class,
                ROOT_PACKAGE);
        for (IApplicationDelegate delegate : mAppDelegateList) {
            delegate.onCreate();
        }

        if (Utils.isAppDebug()) {
            ARouter.openLog();     // 打印日志
            ARouter.openDebug(); // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this);

        Stetho.initializeWithDefaults(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        for (IApplicationDelegate delegate : mAppDelegateList) {
            delegate.onTerminate();
        }
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        for (IApplicationDelegate delegate : mAppDelegateList) {
            delegate.onLowMemory();
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        for (IApplicationDelegate delegate : mAppDelegateList) {
            delegate.onTrimMemory(level);
        }
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
