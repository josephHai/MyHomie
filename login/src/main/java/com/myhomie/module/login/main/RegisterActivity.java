package com.myhomie.module.login.main;

import androidx.annotation.Nullable;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.google.android.material.textfield.TextInputEditText;
import com.myhomie.module.common.base.BaseActivity;
import com.myhomie.module.common.http.HttpClient;
import com.myhomie.module.common.http.OnResultListener;
import com.myhomie.module.login.R;

import java.io.File;

public class RegisterActivity extends BaseActivity {
    public final int CODE_SELECT_IMAGE = 2;//相册RequestCode
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initListener();

    }

    private void initListener() {
        TextInputEditText nicknameText = (TextInputEditText) findViewById(R.id.nickname);
        TextInputEditText usernameText = (TextInputEditText) findViewById(R.id.username);
        TextInputEditText passwordText = (TextInputEditText) findViewById(R.id.password);
        Button registerBtn = (Button) findViewById(R.id.registerBtn);
        Button fileBtn = (Button) findViewById(R.id.fileBtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject data = new JSONObject();
                data.put("username", usernameText.getText().toString());
                data.put("password", passwordText.getText().toString());
                data.put("nickname", nicknameText.getText().toString());

                HttpClient.Builder builder = new HttpClient.Builder()
                        .url("test")
                        .data(data.toString())
                        .tag("REGISTER");
                if (file != null) {
                    builder.file("avatar", file);
                }
                HttpClient client = builder.build();
                client.post(new OnResultListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        JSONObject res = JSONObject.parseObject(result);
                        System.out.println(res.toString());
                    }
                });
            }
        });

        fileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    int REQUEST_CODE_CONTACT = 101;
                    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    for (String str : permissions) {
                        if (RegisterActivity.this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                            RegisterActivity.this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                        }
                    }
                }
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "选择图片"), CODE_SELECT_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CODE_SELECT_IMAGE:
                if (resultCode == RESULT_OK) {
                    selectPic(data);
                }
                break;
        }
    }

    private void selectPic(Intent intent) {
        Uri selectImgUri = intent.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectImgUri, filePathColumn, null,
                null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        ImageView avatar = (ImageView) findViewById(R.id.avatar);
        avatar.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        file =  new File(picturePath);
    }
}
