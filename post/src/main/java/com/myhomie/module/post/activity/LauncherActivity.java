package com.myhomie.module.post.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.myhomie.module.common.base.BaseActivity;
import com.myhomie.module.common.http.HttpClient;
import com.myhomie.module.common.http.OnResultListener;
import com.myhomie.module.post.R;


@Route(path = "/post/detail")
public class LauncherActivity extends BaseActivity {
    @Autowired
    public String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_launcher);

        ARouter.getInstance().inject(this);

        initDraw();
        initListener();
    }

    private void initDraw() {
        HttpClient client = new HttpClient.Builder()
                .url("post/queryDetailed")
                .params("id", id)
                .build();

        client.get(new OnResultListener<String>() {
            @Override
            public void onSuccess(String result) {
                JSONObject res = JSONObject.parseObject(result);
                if (res.getString("status").equals("1")) {
                    super.onSuccess(result);
                    drawView(res.getString("data"));
                }
            }
        });
    }

    private void drawView(String data) {
        TextView authorNickname = findViewById(R.id.post_nickname),
                seen = findViewById(R.id.post_seen),
                created = findViewById(R.id.post_created),
                title = findViewById(R.id.post_title),
                content = findViewById(R.id.post_content);
        JSONObject pageData = JSONObject.parseObject(data);

        String seenText =  "文章阅读量: " + pageData.getString("seen");
        String createdText = "发布时间: " + pageData.getString("created_time");
        authorNickname.setText(pageData.getString("nickname"));
        seen.setText(seenText);
        created.setText(createdText);
        title.setText(pageData.getString("title"));
        content.setText(pageData.getString("content").replaceAll("<br />", "\n"));
        Glide.with(this)
                .load(pageData.getString("avatar"))
                .apply(RequestOptions.circleCropTransform())
                .into((ImageView) findViewById(R.id.post_author_avatar));
    }

    private void initListener() {
        Toolbar mToolbar = findViewById(R.id.post_toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
