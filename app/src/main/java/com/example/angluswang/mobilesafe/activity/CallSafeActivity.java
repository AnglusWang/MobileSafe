package com.example.angluswang.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.angluswang.mobilesafe.R;

/**
 * Created by Jeson on 2016/7/31.
 * 通讯卫士
 */
public class CallSafeActivity extends Activity {
    private ListView lvBlackPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_safe);

        initView();
    }

    private void initView() {
        lvBlackPhone = (ListView) findViewById(R.id.lv_black_number);
    }
}
