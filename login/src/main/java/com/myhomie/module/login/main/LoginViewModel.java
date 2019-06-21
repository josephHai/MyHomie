package com.myhomie.module.login.main;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.myhomie.module.common.base.BaseApplication;
import com.myhomie.module.common.http.HttpClient;
import com.myhomie.module.common.http.OnResultListener;
import com.myhomie.module.login.R;
import com.myhomie.module.login.data.LoginRepository;
import com.orhanobut.logger.Logger;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        String data = "{\"username\": \""+ username + "\", \"password\": \"" + password + "\"}";
        HttpClient client = new HttpClient.Builder()
                .url("login")
                .tag("LOGIN")
                .data(data)
                .build();
        client.post(new OnResultListener<String>() {
            @Override
            public void onSuccess(String response) {
                JSONObject res = JSON.parseObject(response);
                if (res.getString("status").equals("0")) {
                    loginResult.setValue(new LoginResult(res.getString("msg")));
                }else {
                    JSONObject data = JSONObject.parseObject(res.getString("data"));
                    BaseApplication.getIns().setToken(data.getString("token"));
                    System.out.println(data.getString("token"));
                    getUserInfo();
                }
            }

            @Override
            public void onError(int code, String message) {
                loginResult.setValue(new LoginResult(R.string.login_failed));
                Logger.e(message);
            }

            @Override
            public void onFailure(String message) {
                loginResult.setValue(new LoginResult(R.string.login_failed));
                Logger.e(message);
            }
        });

    }

    void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        return username != null;
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    private void getUserInfo() {
        HttpClient client = new HttpClient.Builder()
                .url("getUserInfo")
                .tag("USER_INFO")
                .build();
        client.get(new OnResultListener<String>(){
            @Override
            public void onSuccess(String result) {
                JSONObject res = JSONObject.parseObject(result);
                if (res.getString("status").equals("1")) {
                    LoggedInUserView userView = JSONObject.parseObject(res.getString("data")
                            , LoggedInUserView.class);
                    loginResult.setValue(new LoginResult(userView));
                    Logger.e(result);
                }else {
                    Logger.e(res.getString("msg"));
                }
            }

            @Override
            public void onError(int code, String message) {
                loginResult.setValue(new LoginResult(R.string.get_info_failed));
                Logger.e(message);
            }

            @Override
            public void onFailure(String message) {
                loginResult.setValue(new LoginResult(R.string.get_info_failed));
                Logger.e(message);
            }
        });
    }
}
