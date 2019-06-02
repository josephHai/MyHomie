package com.myhomie.module.login.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.myhomie.module.common.http.HttpClient;
import com.myhomie.module.common.http.OnResultListener;
import com.myhomie.module.login.R;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText telText = findViewById(R.id.tel);
        EditText passwordText = findViewById(R.id.password);
        EditText code = findViewById(R.id.code);
        ImageView imageView = findViewById(R.id.imgCode);
        Button nextBtn = findViewById(R.id.next);
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true);

        Glide.with(this)
                .load("http://myhomie.chinaxueyun.com/info_platform/public/index.php/login/index/getImageCode")
                .apply(requestOptions)
                .into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(RegisterActivity.this)
                        .load("http://myhomie.chinaxueyun.com/info_platform/public/index.php/login/index/getImageCode")
                        .apply(requestOptions)
                        .into(imageView);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject data = new JSONObject();
                data.put("captcha", code.getText().toString());
                data.put("tel", telText.getText().toString());

                HttpClient client = new HttpClient.Builder()
                        .url("login/index/sendSMS")
                        .data(data.toString())
                        .tag("SMS")
                        .build();
                System.out.println(data.toString());
                client.post(new OnResultListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        JSONObject res = JSONObject.parseObject(result);
                        System.out.println(res.toString());
                    }
                });
            }
        });

    }

    private void handleNext() {

    }
}
