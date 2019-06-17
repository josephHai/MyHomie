package com.myhomie.module.login.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.myhomie.module.common.base.BaseActivity;
import com.myhomie.module.login.R;

@Route(path = "/login/login")
public class LauncherActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        Button loginBtn = findViewById(R.id.login);
        Button registerBtn = findViewById(R.id.register);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LauncherActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LauncherActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
