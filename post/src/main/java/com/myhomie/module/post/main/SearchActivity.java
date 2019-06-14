package com.myhomie.module.post.main;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import com.myhomie.module.common.base.BaseActivity;
import com.myhomie.module.post.R;
import com.myhomie.module.post.adapter.PostAdapter;
import com.myhomie.module.post.bean.PostBean;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity {
    private List<PostBean> postList = new ArrayList<PostBean>();
    private RecyclerView recyclerView;
    Toolbar mToolbar;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            System.out.println(query);
        }

        initListener();
        modifiedList();
    }

    private void modifiedList() {
        PostAdapter adapter = new PostAdapter(R.layout.activity_post_item, postList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.list);

        for (int i = 0; i < 10; i++){
            PostBean apple = new PostBean("apple", R.drawable.apple);
            postList.add(apple);

            PostBean watermelon = new PostBean("watermelon", R.drawable.watermelon);
            postList.add(watermelon);
        }

        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void initListener() {
        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                System.out.println("submit");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println(newText);
                return false;
            }
        });
    }
}
