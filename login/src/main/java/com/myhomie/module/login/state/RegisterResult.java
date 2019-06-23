package com.myhomie.module.login.state;

import androidx.annotation.Nullable;


public class RegisterResult {
    @Nullable
    private String errorMsg;
    @Nullable
    private Boolean success;

    public RegisterResult(@Nullable String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public RegisterResult(@Nullable Boolean success) {
        this.success = success;
    }

    @Nullable
    public String getErrorMsg() {
        return errorMsg;
    }

    @Nullable
    public Boolean getSuccess() {
        return success;
    }
}
