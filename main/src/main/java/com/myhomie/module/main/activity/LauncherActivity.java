package com.myhomie.module.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.myhomie.module.common.base.BaseActivity;
import com.myhomie.module.main.R;
import com.myhomie.module.main.fragment.MainFragment;

public class LauncherActivity extends BaseActivity implements MainFragment.Callbacks {
    private Fragment mCurrentFragment;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layoutPager, new MainFragment())
                .commit();
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

    private void initListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            String title = menuItem.getTitle().toString();
            switch (menuItem.getItemId()) {
                case R.id.bottomHome:
                    break;
                case R.id.bottomPost:
                    break;
                case R.id.bottomPersonal:
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
