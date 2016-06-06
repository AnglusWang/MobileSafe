package com.example.angluswang.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.angluswang.mobilesafe.R;

/**
 * Created by Jeson on 2016/6/6.
 * 设置中心的自定义控件
 */

public class SettingItemView extends RelativeLayout {

    public SettingItemView(Context context) {
        super(context);
        initView();
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        //将自定义好的布局文件设置给SettingItemView
        View.inflate(getContext(), R.layout.view_setting_item, this);
    }
}
