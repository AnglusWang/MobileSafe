package com.angluswang.mobilesafe.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Jeson on 2016/8/1.
 */

public abstract class MyBaseAdapter<T> extends BaseAdapter {

    public List<T> lists;
    public Context mContext;

    protected MyBaseAdapter() {
    }

    protected MyBaseAdapter(List<T> lists, Context mContext) {
        this.lists = lists;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
