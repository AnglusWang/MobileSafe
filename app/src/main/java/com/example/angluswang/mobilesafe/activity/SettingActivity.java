package com.example.angluswang.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.angluswang.mobilesafe.R;
import com.example.angluswang.mobilesafe.view.SettingItemView;

/**
 * Created by Jeson on 2016/6/6.
 * 设置中心界面
 */

public class SettingActivity extends Activity {

    private SettingItemView sivUpdate;  //自动更新设置

    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mPref = getSharedPreferences("config", MODE_PRIVATE);

        sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
        sivUpdate.setTitle("自动更新设置");
        sivUpdate.setDesc("自动更新已开启");

        Boolean autoUpdate = mPref.getBoolean("auto_update", true);
        if (autoUpdate) {
            sivUpdate.setChecked(true);
            sivUpdate.setDesc("自动更新已开启");
        }else {
            sivUpdate.setChecked(false);
            sivUpdate.setDesc("自动更新已关闭");
        }

        sivUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sivUpdate.isChecked()) {

                    sivUpdate.setChecked(false);
                    sivUpdate.setDesc("自动更新已关闭");
                    mPref.edit().putBoolean("auto_update", false).commit();
                }else {

                    sivUpdate.setChecked(true);
                    sivUpdate.setDesc("自动更新已开启");
                    mPref.edit().putBoolean("auto_update", true).commit();
                }
            }
        });
    }
}
