package com.myhomie.module.post.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.myhomie.module.post.R;

@Route(path = "/release/index")
public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
    }
}
