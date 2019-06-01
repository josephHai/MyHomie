package com.myhomie.module.common.base;

import androidx.annotation.Keep;

/**
 * <p>类说明</p>
 *
 * @author ctHai
 * @version V1.0.0
 */
@Keep
public interface IApplicationDelegate {


    void onCreate();

    void onTerminate();

    void onLowMemory();

    void onTrimMemory(int level);
}
