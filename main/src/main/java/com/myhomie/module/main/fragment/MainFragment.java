package com.myhomie.module.main.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.myhomie.module.main.R;
import com.myhomie.module.main.adapter.PostAdapter;
import com.myhomie.module.main.bean.PostBean;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    private List<PostBean> postList = new ArrayList<>();
    private RecyclerView recyclerView;
    private Toolbar mToolbar;
    private View view;
    private Callbacks mCallbacks;

    //定义一个回调接口，在Activity中需要实现
    //该Fragment将通过该接口与他它所在的Activity交互
    public interface Callbacks{
        void onItemSelectedListener(Integer id);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, null);
        setHasOptionsMenu(true);
        mToolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);

        initList();
        initDrawer();

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

    private void initList() {
        PostAdapter adapter = new PostAdapter(R.layout.activity_post_item, postList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView = view.findViewById(R.id.list);

        for (int i = 0; i < 10; i++){
            PostBean apple = new PostBean(100-i,"apple", R.drawable.apple);
            postList.add(apple);

            PostBean watermelon = new PostBean(50-i, "watermelon", R.drawable.watermelon);
            postList.add(watermelon);
        }

        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((adapter1, view, position) -> mCallbacks.onItemSelectedListener(position));
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
}
