package com.myhomie.module.login.state;

import androidx.annotation.Nullable;

public class RegisterFormState {
    @Nullable
    private Integer nicknameError;
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer rePasswordError;
    private boolean isDataValid;

    public RegisterFormState(@Nullable Integer nicknameError, @Nullable Integer usernameError, @Nullable Integer passwordError, @Nullable Integer rePasswordError) {
        this.nicknameError = nicknameError;
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.rePasswordError = rePasswordError;
        this.isDataValid = false;
    }

    public RegisterFormState(boolean isDataValid) {
        this.isDataValid = isDataValid;
    }

    @Nullable
    public Integer getNicknameError() {
        return nicknameError;
    }

    @Nullable
    public Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    public Integer getPasswordError() {
        return passwordError;
    }

    @Nullable
    public Integer getRePasswordError() {
        return rePasswordError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}
