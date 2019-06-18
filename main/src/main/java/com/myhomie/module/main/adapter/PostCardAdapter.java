package com.myhomie.module.main.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.myhomie.module.main.R;
import com.myhomie.module.main.bean.PostCardBean;

import java.util.List;

public class PostCardAdapter extends BaseQuickAdapter<PostCardBean, BaseViewHolder> {
    public PostCardAdapter(int layoutResId, @Nullable List<PostCardBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PostCardBean item) {
        //Glide.with(mContext).load(item.getImage()).into((ImageView) helper.getView(R.id.card_image));
        helper.setText(R.id.card_title, item.getTitle())
                .setText(R.id.card_seen, item.getSeen())
                .setText(R.id.card_author, item.getAuthor())
                .setText(R.id.card_created_time, item.getCreatedTime());
    }

    @Override
    public int getItemCount() {
        return getData().size();
    }
}
