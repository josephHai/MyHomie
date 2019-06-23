package com.myhomie.module.main.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.myhomie.module.main.R;
import com.myhomie.module.main.bean.PostBean;

import java.util.List;

public class PostAdapter extends BaseQuickAdapter<PostBean, BaseViewHolder> {
    public PostAdapter(int layoutResId, @Nullable List<PostBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PostBean item) {
        helper.setText(R.id.main_post_item_title, item.getTitle())
                .setText(R.id.main_post_item_created, item.getCreatedTime());
    }
}
