package com.myhomie.module.main.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.myhomie.module.common.ARouterConfig;
import com.myhomie.module.common.base.BaseActivity;
import com.myhomie.module.common.http.HttpClient;
import com.myhomie.module.common.http.OnResultListener;
import com.myhomie.module.main.R;
import com.myhomie.module.main.adapter.PostAdapter;
import com.myhomie.module.main.bean.PostBean;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity {
    private List<PostBean> postList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    Toolbar mToolbar;
    SearchView searchView;

    PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mRecyclerView = findViewById(R.id.list);
        searchView = findViewById(R.id.searchView);
        searchView.requestFocus();

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            System.out.println(query);
        }

        initAdapter();
        initListener();
    }

    private void initAdapter() {
        postAdapter = new PostAdapter(R.layout.post_item, postList);
        postAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(postAdapter);
        postAdapter.setOnItemClickListener((adapter, view, position) -> ARouter.getInstance()
                .build(ARouterConfig.POST_DETAIL_ACTIVITY)
                .withString("id", postAdapter.getData().get(position).getId().toString())
                .navigation());
    }

    private void initList(String keyword) {
        HttpClient client = new HttpClient.Builder()
                .url("post/queryByKeyword")
                .params("keyword", keyword)
                .build();
        client.get(new OnResultListener<String>() {
            @Override
            public void onSuccess(String result) {
                JSONObject res = JSONObject.parseObject(result);
                if (res.getString("status").equals("0")) {
                    Toast.makeText(new SearchActivity(), res.getString("msg"), Toast.LENGTH_SHORT).show();
                }else {
                    postAdapter.setNewData(JSONArray.parseArray(res.getString("data"), PostBean.class));
                    System.out.println(postList);
                }
            }
        });
    }

    private void initListener() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                initList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) {
                    postAdapter.setNewData(postList);
                }else {
                    initList(newText);
                }
                return false;
            }
        });
    }

}
