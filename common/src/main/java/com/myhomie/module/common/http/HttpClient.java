package com.myhomie.module.common.http;

import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.myhomie.module.common.R;
import com.myhomie.module.common.utils.NetworkUtils;
import com.myhomie.module.common.utils.StringUtils;
import com.myhomie.module.common.utils.ToastUtils;
import com.myhomie.module.common.utils.Utils;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Multipart;

public class HttpClient {
    /*用户设置的baseUrl*/
    private static String BASE_URL = "";
    /*本地baseUrl*/
    private String baseUrl = "";
    /*默认baseUrl*/
    private static String defaultBaseUrl = "https://www.buxizhou.com/app/";
    private Retrofit retrofit;
    private static OkHttpClient okHttpClient;
    private Builder mBuilder;
    private Call<ResponseBody> mCall;
    private static final Map<String, Call> CALL_MAP = new HashMap<>();

    private static HttpClient getIns() {
        return HttpClientHolder.sInstance;
    }

    /**
     * 单例模式中的静态内部类写法
     */
    private static class HttpClientHolder {
        private static final HttpClient sInstance = new HttpClient();
    }

    private HttpClient() {
        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(),
                new SharedPrefsCookiePersistor(Utils.getContext()));
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .addInterceptor(new LoggerInterceptor(null, true))
                .cookieJar(cookieJar)
                .build();
    }

    public Builder getBuilder() {
        return mBuilder;
    }

    public void setBuilder(Builder builder) {
        this.mBuilder = builder;
    }

    private void getRetrofit() {
        if (!BASE_URL.equals(baseUrl) || retrofit == null) {
            baseUrl = BASE_URL;
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okHttpClient)
                    .build();
        }
    }

    public void post(final OnResultListener onResultListener) {
        Builder builder = mBuilder;
        if (!builder.files.isEmpty()) {
            Map<String, RequestBody> params = new HashMap<>();
            for (Map.Entry<String, String> entry : builder.data.entrySet()) {
                params.put(entry.getKey(), RequestBody.create(MediaType.parse("text/plain"),
                        entry.getValue()));
            }
            mCall = retrofit.create(ApiService.class).executePost(builder.url, params, builder.files);
        }else {
            mCall = retrofit.create(ApiService.class).executePost(builder.url, builder.data);
        }

        putCall(builder, mCall);
        request(builder, onResultListener);
    }

    public void get(final OnResultListener onResultListener) {
        Builder builder = mBuilder;
        if (!builder.params.isEmpty()) {
            String value = "";
            for (Map.Entry<String, String> entry : builder.params.entrySet()) {
                String mapKey = entry.getKey();
                String mapValue = entry.getValue();
                String span = value.equals(" ") ? "" : "&";
                String part = StringUtils.buffer(span, mapKey, "=", mapValue);
                value = StringUtils.buffer(builder.url, "?", value);
            }
        }
        mCall = retrofit.create(ApiService.class).executeGet(builder.url);
        putCall(builder, mCall);
        request(builder,onResultListener);
    }

    private void request(final Builder builder, final OnResultListener onResultListener) {
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showLongToastSafe(R.string.current_internet_invalid);
            onResultListener.onFailure(Utils.getString(R.string.current_internet_invalid));
            return;
        }
        mCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (200 == response.code()) {
                    try {
                        String result = response.body().string();
                        parseData(result, builder.clazz, builder.bodyType, onResultListener);
                    } catch (IOException | IllegalStateException e) {
                        e.printStackTrace();
                    }
                }
                if (!response.isSuccessful() || 200 != response.code()) {
                    onResultListener.onError(response.code(), response.message());
                }
                if (null != builder.tag) {
                    removeCall(builder.url);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                onResultListener.onFailure(t.getMessage());
                if (null != builder.tag) {
                    removeCall(builder.url);
                }
            }
        });
    }

    private synchronized void putCall(Builder builder, Call call) {
        if (builder.tag == null)
            return;
        synchronized (CALL_MAP) {
            CALL_MAP.put(builder.tag.toString() + builder.url, call);
        }
    }

    /**
     * 取消某个界面都所有请求，或者是取消某个tag的所有请求;
     * 如果要取消某个tag单独请求，tag需要传入tag+url
     *
     * @param tag 请求标签
     */
    private synchronized void cancel(Object tag) {
        if (tag == null)
            return;
        List<String> list = new ArrayList<>();
        synchronized (CALL_MAP) {
            for (String key : CALL_MAP.keySet()) {
                if (key.startsWith(tag.toString())){
                    CALL_MAP.get(key).cancel();
                    list.add(key);
                }
            }
        }
        for (String s : list) {
            removeCall(s);
        }
    }

    private synchronized void removeCall(String url) {
        synchronized (CALL_MAP) {
            for (String key : CALL_MAP.keySet()) {
                if (key.contains(url)) {
                    url = key;
                    break;
                }
            }
            CALL_MAP.remove(url);
        }
    }

    /**
     * 获取的Retrofit的实例，
     * 引起Retrofit变化的因素只有静态变量BASE_URL的改变。
     */
    public static final class Builder {
        private String builderBaseUrl = "";
        private String url;
        private Object tag;
        private Map<String, String> params = new HashMap<>();
        private Map<String, String> data;
        private List<MultipartBody.Part> files = new ArrayList<>();
        /*返回数据的类型 默认是String*/
        @DataType.Type
        private int bodyType = DataType.STRING;
        /*解析类*/
        Class clazz;

        /***
         * 请求地址的baseUrl, 最后会被赋值给HttpClient的静态变量BASE_URL;
         *
         * @param baseUrl : 请求地址的baseUrl
         */
        public Builder baseUrl(String baseUrl) {
            this.builderBaseUrl = baseUrl;
            return this;
        }

        /***
         * 附加path
         *
         * @param url :附加path
         */
        public Builder url(String url) {
            this.url = url;
            return this;
        }

        /**
         * 给当前网络请求添加标签，用于取消这个网络请求
         *
         * @param tag 标签
         */
        public Builder tag(Object tag) {
            this.tag = tag;
            return this;
        }

        /**
         * 添加请求参数
         *
         * @param key   键
         * @param value 值
         */
        public Builder params(String key, String value) {
            this.params.put(key, value);
            return this;
        }

        /***
         * 添加post数据
         *
         * @param data :
         * @return : com.myhomie.module.common.http.HttpClient.Builder
         */
        @SuppressWarnings("unchecked")
        public Builder data(String data) {
            this.data = JSON.parseObject(data, Map.class);
            return this;
        }

        /***
         * 添加一个文件
         *
         * @param description : 参数名
         * @param file : 文件对象
         * @return : com.myhomie.module.common.http.HttpClient.Builder
         */
        public Builder file(String description, File file) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData(description, file.getName(),
                    requestBody);
            this.files.add(body);
            return this;
        }

        /***
         * 添加多个文件
         *
         * @param fileMap : <String, File>
         * @return : com.myhomie.module.common.http.HttpClient.Builder
         */
        public Builder files(Map<String, File> fileMap) {
            RequestBody requestBody;
            MultipartBody.Part body;
            for (Map.Entry<String, File> entry : fileMap.entrySet()) {
                requestBody = RequestBody.create(MediaType.parse("image/*"), entry.getValue());
                body = MultipartBody.Part.createFormData(entry.getKey(), entry.getValue().getName(),
                        requestBody);
                this.files.add(body);
            }
            return this;
        }

        /**
         * 响应体类型设置,如果要响应体类型为STRING，请不要使用这个方法
         *
         * @param bodyType 响应体类型，分别:STRING，JSON_OBJECT,JSON_ARRAY
         * @param clazz    指定的解析类
         * @param <T>      解析类
         */
        public <T> Builder bodyType(@DataType.Type int bodyType, @NonNull Class<T> clazz) {
            this.bodyType = bodyType;
            this.clazz = clazz;
            return this;
        }

        public HttpClient build() {
            if (!TextUtils.isEmpty(builderBaseUrl)) {
                BASE_URL = builderBaseUrl;
            }else {
                BASE_URL = defaultBaseUrl;
            }
            HttpClient client = getIns();
            client.getRetrofit();
            client.setBuilder(this);
            return client;
        }
        
    }

    /**
     * 数据解析方法
     *
     * @param data             要解析的数据
     * @param clazz            解析类
     * @param bodyType         解析数据类型
     * @param onResultListener 回调方数据接口
     */
    @SuppressWarnings("unchecked")
    private void parseData(String data, Class clazz, @DataType.Type int bodyType, OnResultListener onResultListener) {
        switch (bodyType) {
            case DataType.STRING:
                onResultListener.onSuccess(data);
                break;
            case DataType.JSON_OBJECT:
                onResultListener.onSuccess(JSONObject.parseObject(data, clazz));
                break;
            case DataType.JSON_ARRAY:
                onResultListener.onSuccess(JSONObject.parseArray(data, clazz));
            default:
                Logger.e("http parse tip:", "if you want return object, please use bodyType() set data type");
                break;

        }
    }
}
