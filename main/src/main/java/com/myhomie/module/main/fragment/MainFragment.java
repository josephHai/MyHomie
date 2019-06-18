package com.myhomie.module.main.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ajguan.library.EasyRefreshLayout;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.myhomie.module.common.http.HttpClient;
import com.myhomie.module.common.http.OnResultListener;
import com.myhomie.module.main.R;
import com.myhomie.module.main.adapter.PostCardAdapter;
import com.myhomie.module.main.bean.PostCardBean;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    private View view;
    private Callbacks mCallbacks;

    private RecyclerView mRecyclerView;
    private List<PostCardBean> postCardList = new ArrayList<>();
    private PostCardAdapter postCardAdapter;
    private EasyRefreshLayout mEasyRefreshLayout;

    private int delayMillis = 1000;

    private static final Integer PAGE_SIZE = 6;
    private Integer currentPage = 1;

    private int mCurrentCounter = 0;

    //定义一个回调接口，在Activity中需要实现
    //该Fragment将通过该接口与他它所在的Activity交互
    public interface Callbacks{
        void onItemSelectedListener(Integer id);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 私有属性初始化
        view = inflater.inflate(R.layout.fragment_main, null);
        mEasyRefreshLayout = view.findViewById(R.id.easy_layout);

        // 使菜单栏显示
        setHasOptionsMenu(true);
        Toolbar mToolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);

        initAdapter();
        initDrawer();
        initRefresh();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof Callbacks)) {
            throw new IllegalStateException("必须实现Callbacks接口");
        }
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mCallbacks = null;
    }

    private void initAdapter() {
        postCardAdapter = new PostCardAdapter(R.layout.post_card, postCardList);
        postCardAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView = view.findViewById(R.id.main_list);

        initList();
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(postCardAdapter);

        postCardAdapter.setOnItemClickListener((adapter1, view, position)
                -> mCallbacks.onItemSelectedListener(postCardAdapter.getData().get(position).getId()));
    }

    private void initList() {
        HttpClient client = new HttpClient.Builder()
                .params("limit", PAGE_SIZE.toString())
                .params("page", "1")
                .url("post/queryPosts")
                .tag("QUERY_POSTS")
                .build();

        client.get(new OnResultListener<String>() {
            @Override
            public void onSuccess(String result) {
                JSONObject res = JSONObject.parseObject(result);
                if (res.getString("status").equals("0")) {
                    Toast.makeText(view.getContext(), res.getString("msg"), Toast.LENGTH_LONG).show();
                }else {
                    String data = JSONObject.parseObject(res.getString("data")).getString("list");
                    postCardAdapter.setNewData(JSONObject.parseArray(data, PostCardBean.class));
                    mCurrentCounter = PAGE_SIZE;
                    currentPage = 1;
                    mEasyRefreshLayout.refreshComplete();
                    Toast.makeText(getActivity(), "refresh success", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initDrawer() {
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.drawer_item_home);
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.drawer_item_settings);

        Toolbar toolbar = view.findViewById(R.id.toolbar);

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(getActivity())
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile))
                )
                .withOnAccountHeaderListener((view, profile, currentProfile) -> false)
                .build();

        //create the drawer and remember the `Drawer` result object
        Drawer result = new DrawerBuilder()
                .withAccountHeader(headerResult)
                .withActivity(getActivity())
                .withToolbar(toolbar)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                        item2,
                        new SecondaryDrawerItem().withName(R.string.drawer_item_settings)
                )
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    System.out.println(position);
                    // do something with the clicked item :D
                    return true;
                })
                .build();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.main_menu, menu);
    }

    private void initRefresh() {
        mEasyRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {

                new Handler().postDelayed(() -> {
                    Integer page = (currentPage + 1);
                    HttpClient client = new HttpClient.Builder()
                            .params("limit", PAGE_SIZE.toString())
                            .params("page", page.toString())
                            .url("post/queryPosts")
                            .tag("QUERY_POSTS")
                            .build();

                    client.get(new OnResultListener<String>() {
                        @Override
                        public void onSuccess(String result) {
                            JSONObject res = JSONObject.parseObject(result);
                            if (res.getString("status").equals("0")) {
                                Toast.makeText(view.getContext(), res.getString("msg"), Toast.LENGTH_LONG).show();
                            }else {
                                String data = JSONObject.parseObject(res.getString("data")).getString("list");
                                mEasyRefreshLayout.loadMoreComplete(new EasyRefreshLayout.Event() {
                                    @Override
                                    public void complete() {
                                        postCardAdapter.getData().addAll(JSONObject.parseArray(data, PostCardBean.class));
                                        postCardAdapter.notifyDataSetChanged();
                                        currentPage++;
                                        mCurrentCounter = postCardAdapter.getData().size();
                                    }
                                }, 500);
                            }
                        }
                    });
                }, 2000);
            }

            @Override
            public void onRefreshing() {
                new Handler().postDelayed(() -> initList(), delayMillis);
            }
        });
    }
}
