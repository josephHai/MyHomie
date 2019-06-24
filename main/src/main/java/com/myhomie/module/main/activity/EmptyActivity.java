package com.myhomie.module.main.activity;

import android.content.Intent;
import android.os.Bundle;

import com.myhomie.module.common.base.BaseActivity;

public class EmptyActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startActivity(new Intent(EmptyActivity.this, LauncherActivity.class));
        finish();
    }
}
