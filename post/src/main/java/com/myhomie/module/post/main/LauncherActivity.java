package com.myhomie.module.post.main;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.myhomie.module.common.base.BaseActivity;
import com.myhomie.module.post.R;

@Route(path = "/post/detail")
public class LauncherActivity extends BaseActivity {
    @Autowired
    public int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_launcher);

        ARouter.getInstance().inject(this);
        System.out.println(id);

    }
}
