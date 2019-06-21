package com.myhomie.module.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.myhomie.module.common.base.BaseActivity;
import com.myhomie.module.main.fragment.PersonFragment;
import com.myhomie.module.main.R;
import com.myhomie.module.main.fragment.MainFragment;
import com.myhomie.module.main.fragment.ReleaseFragment;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/main/main")
public class LauncherActivity extends BaseActivity implements MainFragment.Callbacks {
    private List<Fragment> mFragmentList;
    private Fragment mCurrentFragment;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        initFragment();
        initBottomNavigationView();
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

    private void initFragment() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new MainFragment());
        mFragmentList.add(new ReleaseFragment());
        mFragmentList.add(new PersonFragment());
    }

    private void initBottomNavigationView() {
        addFragment(R.id.layoutPager, mFragmentList.get(0));

        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.bottomHome:
                    addFragment(R.id.layoutPager, mFragmentList.get(0));
                    break;
                case R.id.bottomPost:
                    addFragment(R.id.layoutPager, mFragmentList.get(1));
                    break;
                case R.id.bottomPersonal:
                    addFragment(R.id.layoutPager, mFragmentList.get(2));
                    break;
            }
            return true;
        });
    }



    /***
     * 添加fragment到本activity
     *
     * @param frameLayoutId : 需要展示到的fragment的id
     * @param fragment :
     */
    public void addFragment(int frameLayoutId, Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (fragment.isAdded()) {
                if (mCurrentFragment != null) {
                    transaction.hide(mCurrentFragment).show(fragment);
                } else {
                    transaction.show(fragment);
                }
            } else {
                if (mCurrentFragment != null) {
                    transaction.hide(mCurrentFragment).add(frameLayoutId, fragment);
                } else {
                    transaction.add(frameLayoutId, fragment);
                }
            }
            mCurrentFragment = fragment;
            transaction.commit();
        }
    }

    @Override
    public void onItemSelectedListener(Integer id) {
        System.out.println(id);
    }
}
