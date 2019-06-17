package com.myhomie.module.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.myhomie.module.common.base.BaseActivity;
import com.myhomie.module.main.adapter.PostAdapter;
import com.myhomie.module.main.bean.PostBean;

import java.util.ArrayList;
import java.util.List;

public class LauncherActivity extends BaseActivity {
    private SearchView searchView;
    private List<PostBean> postList = new ArrayList<PostBean> ();
    private RecyclerView recyclerView;
    Toolbar mToolbar;
    SearchView searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        initDrawer();
        initList();
    }

    private void initList() {
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

    private void initDrawer() {
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.drawer_item_home);
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.drawer_item_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        //create the drawer and remember the `Drawer` result object
        Drawer result = new DrawerBuilder()
                .withAccountHeader(headerResult)
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                        item2,
                        new SecondaryDrawerItem().withName(R.string.drawer_item_settings)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        System.out.println(position);
                        // do something with the clicked item :D
                        return true;
                    }
                })
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_view_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_search) {
            Intent intent = new Intent(LauncherActivity.this, SearchActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
