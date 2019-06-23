package com.myhomie.module.main.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.myhomie.module.common.ARouterConfig;
import com.myhomie.module.common.MyDBHelper;
import com.myhomie.module.common.base.BaseActivity;
import com.myhomie.module.common.http.HttpClient;
import com.myhomie.module.common.model.User;
import com.myhomie.module.main.R;
import com.myhomie.module.main.adapter.ListAdapter;
import com.myhomie.module.main.bean.ListBean;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PersonFragment extends Fragment {
    private ListAdapter listAdapter;
    private List<ListBean> mList;
    private View view;
    private RecyclerView mRecyclerView;


    public PersonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_person, null);
        mList = new ArrayList<>();

        initAdapter();
        initDraw();

        return view;
    }

    private void initAdapter() {
        listAdapter = new ListAdapter(R.layout.list, mList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView = view.findViewById(R.id.person_list);

        mList.add(new ListBean(R.drawable.add, "退出登录"));

        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(listAdapter);

        listAdapter.setOnItemClickListener((adapter, view, position) -> {
            System.out.println(position);
            switch (position) {
                case 0:
                    ARouter.getInstance().build(ARouterConfig.LOGIN_ACTIVITY).navigation();
                    break;
                default:
                    break;
            }
        });
    }

    private void initDraw() {
        MyDBHelper dbHelper = new MyDBHelper(getContext());
        List<User> userList = dbHelper.find(User.class);
        if (!userList.isEmpty()){
            User own = userList.get(0);
            ImageView avatar = view.findViewById(R.id.person_avatar);
            TextView nickname = view.findViewById(R.id.person_nickname);

            Glide.with(getActivity())
                    .load(own.getAvatar())
                    .apply(RequestOptions.circleCropTransform())
                    .into(avatar);
            nickname.setText(own.getNickname());
        }
    }

}
