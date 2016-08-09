package com.angluswang.mobilesafe.activity.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.angluswang.mobilesafe.R;
import com.angluswang.mobilesafe.activity.fragment.LockFragment;
import com.angluswang.mobilesafe.activity.fragment.UnLockFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by AnglusWang on 2016/8/8.
 * 应用程序锁 活动界面
 */
@ContentView(R.layout.activity_app_lock)
public class AppLockActivity extends Activity implements View.OnClickListener {

    @ViewInject(R.id.tv_lock)
    private TextView tvLock;
    @ViewInject(R.id.tv_unlock)
    private TextView tvUnlock;

    private FragmentManager fragmentMg;
    private UnLockFragment unLockFragment;
    private LockFragment lockFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        // 设置点击侦听
        tvLock.setOnClickListener(this);
        tvUnlock.setOnClickListener(this);

        // 获取 Fragment 的管理者
        fragmentMg = getFragmentManager();
        // 开启事务
        FragmentTransaction transaction = fragmentMg.beginTransaction();
        unLockFragment = new UnLockFragment();
        lockFragment = new LockFragment();
        transaction.replace(R.id.fl_content, unLockFragment).commit();

    }

    @Override
    public void onClick(View v) {

        FragmentTransaction transaction = fragmentMg.beginTransaction();
        switch (v.getId()) {
            case R.id.tv_lock: // 已经加锁
                tvLock.setBackgroundResource(R.drawable.tab_left_pressed);
                tvUnlock.setBackgroundResource(R.drawable.tab_right_default);
                // 切换到 加锁 Fragment
                transaction.replace(R.id.fl_content, lockFragment).commit();
                break;
            case R.id.tv_unlock: // 未加锁
                tvLock.setBackgroundResource(R.drawable.tab_left_default);
                tvUnlock.setBackgroundResource(R.drawable.tab_right_pressed);
                // 切换到 为加锁 Fragment
                transaction.replace(R.id.fl_content, unLockFragment).commit();
                break;
        }
    }
}
