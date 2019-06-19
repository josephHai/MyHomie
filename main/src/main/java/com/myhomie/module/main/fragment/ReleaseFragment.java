package com.myhomie.module.main.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONObject;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.myhomie.module.common.http.HttpClient;
import com.myhomie.module.common.http.OnResultListener;
import com.myhomie.module.main.R;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class ReleaseFragment extends Fragment {
    private View view;
    TextInputEditText titleText;
    EditText categoryText;
    TextInputEditText contentText;

    ImageView imageView;
    Map<String, File> imageMap;

    private final int CODE_SELECT_IMAGE = 2;//相册RequestCode

    MaterialButton sendBtn;

    public ReleaseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_release, null);
        titleText = view.findViewById(R.id.release_title);
        categoryText = view.findViewById(R.id.release_category);
        contentText = view.findViewById(R.id.release_content);

        imageMap = new HashMap<>(3);
        imageView = view.findViewById(R.id.release_pic1);
        sendBtn = view.findViewById(R.id.release_send);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initListener();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initListener() {
        imageView.setOnClickListener(v -> {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            for (String str : permissions) {
                if (getActivity().checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    getActivity().requestPermissions(permissions, REQUEST_CODE_CONTACT);
                }
            }
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(Intent.createChooser(intent, "选择图片"), CODE_SELECT_IMAGE);
        });

        categoryText.setOnClickListener(v -> new MaterialDialog.Builder(getContext())
                .items(R.array.category)
                .itemsCallback((dialog, itemView, position, text) -> {
                    String[] arr = getResources().getStringArray(R.array.category);
                    categoryText.setText(arr[position]);
                }).show());

        sendBtn.setOnClickListener(v -> new HttpClient.Builder()
                .url("post/sendPost")
                .params("title", titleText.getText().toString())
                .params("category", categoryText.getText().toString())
                .params("content", contentText.getText().toString())
                .files(imageMap)
                .build()
                .post(new OnResultListener<String>() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(getActivity(), JSONObject.parseObject(result).getString("msg"),
                        Toast.LENGTH_LONG).show();
            }
        }));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        Cursor cursor = getActivity().getContentResolver().query(selectImgUri, filePathColumn, null,
                null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        imageMap.put("pic1", new File(picturePath));
    }
}
