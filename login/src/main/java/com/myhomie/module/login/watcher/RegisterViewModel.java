package com.myhomie.module.login.watcher;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSONObject;
import com.myhomie.module.common.http.HttpClient;
import com.myhomie.module.common.http.OnResultListener;
import com.myhomie.module.login.R;
import com.myhomie.module.login.data.RegisterRepository;
import com.myhomie.module.login.state.RegisterFormState;
import com.myhomie.module.login.state.RegisterResult;

public class RegisterViewModel extends ViewModel {

    private MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();
    private MutableLiveData<RegisterResult> registerResult = new MutableLiveData<>();
    private RegisterRepository registerRepository;

    RegisterViewModel(RegisterRepository repository) {
        this.registerRepository = repository;
    }

    public LiveData<RegisterFormState> getRegisterFormState() {
        return registerFormState;
    }

    public LiveData<RegisterResult> getRegisterResult() {
        return registerResult;
    }

    public void register(HttpClient client) {
        client.post(new OnResultListener<String>() {
            @Override
            public void onSuccess(String result) {
                JSONObject res = JSONObject.parseObject(result);
                if (res.getString("status").equals("0")) {
                    registerResult.setValue(new RegisterResult(res.getString("msg")));
                }else {
                    registerResult.setValue(new RegisterResult(true));
                }
            }
        });
    }

    public void registerDataChange(String nickname, String username, String password, String rePassword){
        if (!isNicknameValid(nickname)) {
            registerFormState.setValue(new RegisterFormState(R.string.invalid_nickname, null, null, null));
        }else if(!isUsernameValid(username)) {
            registerFormState.setValue(new RegisterFormState(null, R.string.invalid_username, null, null));
        }else if(!isPasswordValid(password)) {
            registerFormState.setValue(new RegisterFormState(null, null, R.string.invalid_password, null));
        }else if(!isRePasswordValid(password, rePassword)) {
            registerFormState.setValue(new RegisterFormState(null, null, null, R.string.invalid_re_password));
        }else {
            registerFormState.setValue(new RegisterFormState(true));
        }
    }

    private boolean isNicknameValid(String nickname) {
        return nickname.trim().length() < 10;
    }

    private boolean isUsernameValid(String username) {
        return username != null;
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    private boolean isRePasswordValid(String password, String rePassword) {
        return password != null && password.equals(rePassword);
    }
}
