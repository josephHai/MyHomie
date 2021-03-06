package com.myhomie.module.login.avtivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.myhomie.module.common.ARouterConfig;
import com.myhomie.module.common.base.BaseActivity;
import com.myhomie.module.login.R;

@Route(path = ARouterConfig.LOGIN_ACTIVITY)
public class LauncherActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_launcher);

        Button loginBtn = findViewById(R.id.login);
        Button registerBtn = findViewById(R.id.register);

        loginBtn.setOnClickListener(v -> {
            Intent intent = new Intent(LauncherActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        registerBtn.setOnClickListener(v -> {
            Intent intent = new Intent(LauncherActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
