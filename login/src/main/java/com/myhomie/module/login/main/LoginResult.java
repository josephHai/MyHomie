package com.myhomie.module.login.main;

import androidx.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 */
class LoginResult {
    @Nullable
    private LoggedInUserView success;
    @Nullable
    private Integer error;
    @Nullable
    private String errorMsg;

    LoginResult(@Nullable Integer error) {
        this.error = error;
    }

    LoginResult(@Nullable String errorMsg) {
        this.errorMsg = errorMsg;
    }

    LoginResult(@Nullable LoggedInUserView success) {
        this.success = success;
    }

    @Nullable
    LoggedInUserView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }

    @Nullable
    String getErrorMsg() {
        return errorMsg;
    }
}
