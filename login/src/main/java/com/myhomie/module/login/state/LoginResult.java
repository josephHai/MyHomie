package com.myhomie.module.login.state;

import androidx.annotation.Nullable;

import com.myhomie.module.login.watcher.LoggedInUserView;

/**
 * Authentication result : success (user details) or error message.
 */
public class LoginResult {
    @Nullable
    private LoggedInUserView success;
    @Nullable
    private Integer error;
    @Nullable
    private String errorMsg;

    public LoginResult(@Nullable Integer error) {
        this.error = error;
    }

    public LoginResult(@Nullable String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public LoginResult(@Nullable LoggedInUserView success) {
        this.success = success;
    }

    @Nullable
    public LoggedInUserView getSuccess() {
        return success;
    }

    @Nullable
    public Integer getError() {
        return error;
    }

    @Nullable
    public String getErrorMsg() {
        return errorMsg;
    }
}
