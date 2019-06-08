package com.myhomie.module.post.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.myhomie.module.post.bean.PostBean;

import com.myhomie.module.post.R;

import java.util.List;

public class PostAdapter extends BaseQuickAdapter<PostBean, BaseViewHolder> {
    public PostAdapter(int layoutResId, @Nullable List<PostBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PostBean item) {
        helper.setText(R.id.itemText, item.getContent())
                .setImageResource(R.id.itemImg, item.getImageId());
    }
}
