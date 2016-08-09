package com.angluswang.mobilesafe.activity.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.angluswang.mobilesafe.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by AnglusWang on 2016/8/9.
 * 清理缓存垃圾 活动界面
 */

@ContentView(R.layout.activity_clean_cache)
public class CleanCacheActivity extends Activity {

    @ViewInject(R.id.lv_clean_cache)
    private ListView lvClean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        initView();
    }

    private void initView() {

    }
}
