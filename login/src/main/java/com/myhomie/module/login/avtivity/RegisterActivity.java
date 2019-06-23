package com.myhomie.module.login.avtivity;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONObject;
import com.google.android.material.textfield.TextInputEditText;
import com.myhomie.module.common.ARouterConfig;
import com.myhomie.module.common.base.BaseActivity;
import com.myhomie.module.common.http.HttpClient;
import com.myhomie.module.common.http.OnResultListener;
import com.myhomie.module.login.R;
import com.myhomie.module.login.watcher.RegisterViewModel;
import com.myhomie.module.login.watcher.RegisterViewModelFactory;

import java.io.File;

public class RegisterActivity extends BaseActivity {
    public final int CODE_SELECT_IMAGE = 2;//相册RequestCode
    private File file;

    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerViewModel = ViewModelProviders.of(this, new RegisterViewModelFactory())
                .get(RegisterViewModel.class);

        final TextInputEditText nicknameText = findViewById(R.id.nickname);
        final TextInputEditText usernameText = findViewById(R.id.username);
        final TextInputEditText passwordText = findViewById(R.id.password);
        final TextInputEditText rePasswordText = findViewById(R.id.re_password);
        final ImageView avatarFile = findViewById(R.id.login_avatar_file);
        final Button registerButton = findViewById(R.id.registerBtn);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);


        registerViewModel.getRegisterFormState().observe(this, registerFormState -> {
            if (registerFormState == null) {
                return;
            }
            registerButton.setEnabled(registerFormState.isDataValid());
            if (registerFormState.getNicknameError() != null) {
                nicknameText.setError(getString(registerFormState.getNicknameError()));
            }
            if (registerFormState.getUsernameError() != null) {
                usernameText.setError(getString(registerFormState.getUsernameError()));
            }
            if (registerFormState.getPasswordError() != null) {
                passwordText.setError(getString(registerFormState.getPasswordError()));
            }
            if (registerFormState.getRePasswordError() != null) {
                rePasswordText.setError(getString(registerFormState.getRePasswordError()));
            }
        });

        registerViewModel.getRegisterResult().observe(this, registerResult -> {
            if (registerResult == null) {
                return;
            }
            loadingProgressBar.setVisibility(View.GONE);
            if (registerResult.getErrorMsg() != null) {
                showErrorMsg(registerResult.getErrorMsg());
            }
            if (registerResult.getSuccess() != null && registerResult.getSuccess()) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
            setResult(Activity.RESULT_OK);
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                registerViewModel.registerDataChange(nicknameText.getText().toString(), usernameText.getText().toString(),
                        passwordText.getText().toString(), rePasswordText.getText().toString());
            }
        };

        nicknameText.addTextChangedListener(afterTextChangedListener);
        usernameText.addTextChangedListener(afterTextChangedListener);
        passwordText.addTextChangedListener(afterTextChangedListener);
        rePasswordText.addTextChangedListener(afterTextChangedListener);

        registerButton.setOnClickListener(v -> {
            JSONObject data = new JSONObject();
            data.put("username", usernameText.getText().toString());
            data.put("password", passwordText.getText().toString());
            data.put("nickname", nicknameText.getText().toString());

            HttpClient.Builder builder = new HttpClient.Builder()
                    .url("register")
                    .data(data.toString())
                    .tag("REGISTER");
            if (file != null) {
                builder.file("avatar", file);
            }
            HttpClient client = builder.build();
            registerViewModel.register(client);
        });

        avatarFile.setOnClickListener(v -> {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            for (String str : permissions) {
                if (RegisterActivity.this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    RegisterActivity.this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                }
            }
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(Intent.createChooser(intent, "选择图片"), CODE_SELECT_IMAGE);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODE_SELECT_IMAGE) {
            if (resultCode == RESULT_OK) {
                selectPic(data);
            }
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
        ImageView avatar = findViewById(R.id.login_avatar_file);
        avatar.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        file =  new File(picturePath);
    }

    private void showErrorMsg(String errorMsg) {
        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
    }
}
