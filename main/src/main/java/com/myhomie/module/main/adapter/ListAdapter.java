package com.myhomie.module.main.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.myhomie.module.main.R;
import com.myhomie.module.main.bean.ListBean;

import java.util.List;

public class ListAdapter extends BaseQuickAdapter<ListBean, BaseViewHolder> {
    public ListAdapter(int layoutResId, @Nullable List<ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ListBean item) {
        helper.setImageResource(R.id.list_icon, item.getIcon())
                .setText(R.id.list_name, item.getName());
    }
}
