package com.myhomie.module.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.myhomie.module.common.MyDBHelper;
import com.myhomie.module.common.base.BaseApplication;
import com.myhomie.module.common.http.HttpClient;
import com.myhomie.module.common.http.OnResultListener;
import com.myhomie.module.common.model.User;

import java.util.List;

/**
 * 用户状态工具类
 */
public class StatusUtils {
    private MyDBHelper myDBHelper = new MyDBHelper(Utils.getContext());

    public boolean isLogin() {
        return !BaseApplication.getIns().getToken().equals("");
    }

    /***
     *
     * 登录
     */
    public void login() {
        List<User> userList = myDBHelper.find(User.class);
        System.out.println("login");
        if (!userList.isEmpty()) {
            System.out.println("auto");
            User user = userList.get(0);
            HttpClient client = new HttpClient.Builder()
                    .url("login")
                    .params("username", user.getUsername())
                    .params("password", user.getPassword())
                    .build();
            client.post(new OnResultListener<String>(){
                @Override
                public void onSuccess(String result) {
                    JSONObject res = JSONObject.parseObject(result);
                    if (res.getString("status").equals("1")) {
                        String token = JSONObject.parseObject(res.getString("data"))
                                .getString("token");
                        BaseApplication.getIns().setToken(token);
                    }
                }
            });
        }
    }

    /***
     *
     * 退出登录
     */
    public void logout() {
        List<User> userList = myDBHelper.find(User.class);
        if (!userList.isEmpty()) {
            User user = userList.get(0);
            myDBHelper.delete("user", user);
            BaseApplication.getIns().setToken("");
        }
    }
}
