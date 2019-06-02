package com.myhomie.module.login.main;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.myhomie.module.common.http.HttpClient;
import com.myhomie.module.common.http.OnResultListener;
import com.myhomie.module.login.R;
import com.myhomie.module.login.data.LoginRepository;
import com.myhomie.module.login.data.model.LoggedInUser;
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
        String data = "{\"username\": "+ username + ", \"password\": \"" + password + "\"}";
        HttpClient client = new HttpClient.Builder()
                .url("login/index/login")
                .tag("LOGIN")
                .data(data)
                .build();
        client.post(new OnResultListener<String>() {
            @Override
            public void onSuccess(String response) {
                JSONObject res = JSON.parseObject(response);
                if (res.getString("status_code").equals("0")) {
                    loginResult.setValue(new LoginResult(res.getString("msg")));
                }else {
                    getUserInfo();
                    LoggedInUser data =
                            new LoggedInUser(
                                    java.util.UUID.randomUUID().toString(),
                                    "Jane Doe");
                    loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
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

    public void loginDataChanged(String username, String password) {
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
        if (username == null) {
            return false;
        }
        return true;
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    private void getUserInfo() {
        HttpClient client = new HttpClient.Builder()
                .url("user/index/getUserInfo")
                .tag("USER_INFO")
                .build();
        client.post(new OnResultListener<String>(){
            @Override
            public void onSuccess(String result) {
                JSONObject res = JSONObject.parseObject(result);
                Logger.e(result);
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