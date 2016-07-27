package com.example.angluswang.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.angluswang.mobilesafe.R;
import com.example.angluswang.mobilesafe.service.AddressService;
import com.example.angluswang.mobilesafe.view.SettingItemView;

/**
 * Created by Jeson on 2016/6/6.
 * 设置中心界面
 */

public class SettingActivity extends Activity {

    private SettingItemView sivUpdate;  //自动更新设置
    private SettingItemView sivAddress;  //归属地显示设置

    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mPref = getSharedPreferences("config", MODE_PRIVATE);

        initUpdateView();
        initAddressView();
    }

    /**
     * 初始化自动更新开关
     */
    private void initUpdateView() {
        sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
//        sivUpdate.setTitle("自动更新设置");
//        sivUpdate.setDesc("自动更新已开启");

        Boolean autoUpdate = mPref.getBoolean("auto_update", true);
        if (autoUpdate) {
            sivUpdate.setChecked(true);
//            sivUpdate.setDesc("自动更新已开启");
        } else {
            sivUpdate.setChecked(false);
//            sivUpdate.setDesc("自动更新已关闭");
        }

        sivUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sivUpdate.isChecked()) {

                    sivUpdate.setChecked(false);
//                    sivUpdate.setDesc("自动更新已关闭");
                    mPref.edit().putBoolean("auto_update", false).commit();
                } else {

                    sivUpdate.setChecked(true);
//                    sivUpdate.setDesc("自动更新已开启");
                    mPref.edit().putBoolean("auto_update", true).commit();
                }
            }
        });
    }

    /**
     * 初始化归属地显示设置开关
     */
    private void initAddressView() {
        sivAddress = (SettingItemView) findViewById(R.id.siv_address);
        sivAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sivAddress.isChecked()) {
                    sivAddress.setChecked(false);
                    stopService(new Intent(SettingActivity.this, AddressService.class));//停止服务
                } else {
                    sivAddress.setChecked(true);
                    startService(new Intent(SettingActivity.this, AddressService.class));//开启服务
                }
            }
        });
    }
}
