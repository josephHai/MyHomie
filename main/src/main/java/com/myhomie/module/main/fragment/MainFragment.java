package com.myhomie.module.main.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.myhomie.module.common.MyDBHelper;
import com.myhomie.module.common.http.HttpClient;
import com.myhomie.module.common.http.OnResultListener;
import com.myhomie.module.common.model.User;
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
        void onDrawItemSelectedListener(Integer resId);
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
    public void onAttach(@NonNull Context context) {
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

    private OnCheckedChangeListener checkedChangeListener = (drawerItem, buttonView, isChecked) -> {
        if(drawerItem instanceof Nameable) {
            Toast.makeText(getActivity(),((Nameable)drawerItem).getName() + "'s check is" + isChecked,Toast.LENGTH_SHORT).show();
        }

    };

    private void initDrawer() {
        MyDBHelper dbHelper = new MyDBHelper(getContext());
        String nickname;
        String avatar;
        List<User> userList = dbHelper.find(User.class);
        Toolbar toolbar = view.findViewById(R.id.toolbar);

        if (!userList.isEmpty()){
            nickname = userList.get(0).getNickname();
            avatar = userList.get(0).getAvatar();
        }else {
            nickname = getString(R.string.default_nickname);
            avatar = getString(R.string.default_avatar);
        }

        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                super.set(imageView, uri, placeholder);
                Glide.with(imageView.getContext()).load(uri).into(imageView);
            }
        });

        IProfile profile = new ProfileDrawerItem()
                .withName(nickname)
                .withIcon(avatar)
                .withIdentifier(100);

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(getActivity())
                .withTranslucentStatusBar(true)
                .withSelectionListEnabled(false)
                .withHeaderBackground(R.color.transparent)
                .addProfiles(profile)
                .withOnAccountHeaderListener((view, profile1, current) -> {
                    if ((int) profile1.getIdentifier() == 100) {
                        System.out.println("this icon is clicked");
                    }
                    return true;
                })
                .build();

        new DrawerBuilder()
                .withActivity(getActivity())
                .withAccountHeader(headerResult)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                        .withIdentifier(1)
                        .withName(R.string.drawer_item_home)
                        .withSelectable(false),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem()
                        .withIdentifier(2)
                        .withName(R.string.drawer_item_person),
                        new SecondaryDrawerItem()
                        .withIdentifier(3)
                        .withName(R.string.drawer_item_settings)
                )
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    switch ((int) drawerItem.getIdentifier()){
                        case 1:
                            mCallbacks.onDrawItemSelectedListener(R.string.drawer_item_home);
                            break;
                        case 2:
                            mCallbacks.onDrawItemSelectedListener(R.string.drawer_item_person);
                            break;
                        case 3:
                            mCallbacks.onDrawItemSelectedListener(R.string.drawer_item_settings);
                        default:
                            break;
                    }
                    return false;
                })
                .withShowDrawerOnFirstLaunch(true)  //设置为默认启动抽屉菜单
                .build();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
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
                                mEasyRefreshLayout.loadMoreComplete(() -> {
                                    postCardAdapter.getData().addAll(JSONObject.parseArray(data, PostCardBean.class));
                                    postCardAdapter.notifyDataSetChanged();
                                    currentPage++;
                                    mCurrentCounter = postCardAdapter.getData().size();
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

    @Override
    public void onResume() {
        super.onResume();
        initDrawer();
    }
}
