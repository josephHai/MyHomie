package com.myhomie.module.common.http;

import android.util.Log;

import com.myhomie.module.common.base.BaseApplication;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RequestInterceptor extends BaseApplication implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .addHeader("HTTP_TOKEN", getIns().getToken())
                .build();
        Log.v("header", "request headers:" + request.headers().toString());
        return chain.proceed(request);
    }
}
