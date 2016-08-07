package com.angluswang.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.angluswang.mobilesafe.service.AddressService;
import com.angluswang.mobilesafe.service.CallSafeService;
import com.angluswang.mobilesafe.R;
import com.angluswang.mobilesafe.service.RocketService;
import com.angluswang.mobilesafe.utils.SystemInfoUtils;
import com.angluswang.mobilesafe.view.SettingClickView;
import com.angluswang.mobilesafe.view.SettingItemView;

/**
 * Created by Jeson on 2016/6/6.
 * 设置中心界面
 */

public class SettingActivity extends Activity {

    private SettingItemView sivUpdate;  //自动更新设置
    private SettingItemView sivAddress;  //归属地显示设置
    private SettingItemView sivRocket; // 小火箭设置
    private SettingItemView sivBlackNum; // 黑名单设置

    private SettingClickView scvAddressStyle; // 归属地风格
    private SettingClickView scvAddressLocation; // 归属地位置

    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mPref = getSharedPreferences("config", MODE_PRIVATE);

        initUpdateView();
        initAddressView();
        initRocketView();
        initAddressStyle();
        initAddressLocation();
        initBlackView();
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
                    mPref.edit().putBoolean("auto_update", false).apply();
                } else {
                    sivUpdate.setChecked(true);
//                    sivUpdate.setDesc("自动更新已开启");
                    mPref.edit().putBoolean("auto_update", true).apply();
                }
            }
        });
    }

    /**
     * 初始化归属地显示设置开关
     */
    private void initAddressView() {
        sivAddress = (SettingItemView) findViewById(R.id.siv_address);

        boolean running = SystemInfoUtils.isServiceRunning(this,
                "com.angluswang.mobilesafe.service.AddressService");
        if (running) {
            sivAddress.setChecked(true);
        } else {
            sivAddress.setChecked(false);
        }

        sivAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sivAddress.isChecked()) {
                    sivAddress.setChecked(false);
                    stopService(new Intent(SettingActivity.this,
                            AddressService.class));//停止服务
                } else {
                    sivAddress.setChecked(true);
                    startService(new Intent(SettingActivity.this,
                            AddressService.class));//开启服务
                }
            }
        });

    }

    /**
     * 初始化小火箭 设置开关
     */
    private void initRocketView() {
        sivRocket = (SettingItemView) findViewById(R.id.siv_rocket);

        boolean running = SystemInfoUtils.isServiceRunning(this,
                "com.angluswang.mobilesafe.service.RocketService");
        if (running) {
            sivRocket.setChecked(true);
        } else {
            sivRocket.setChecked(false);
        }

        sivRocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sivRocket.isChecked()) {
                    sivRocket.setChecked(false);
                    stopService(new Intent(SettingActivity.this,
                            RocketService.class)); //开启小火箭
                } else {
                    sivRocket.setChecked(true);
                    startService(new Intent(SettingActivity.this,
                            RocketService.class)); //关闭小火箭
                }
            }
        });
    }

    /**
     * 初始化黑名单
     */
    private void initBlackView() {
        sivBlackNum = (SettingItemView) findViewById(R.id.siv_black_num);

        // 判断拦截服务是否运行
        boolean serviceRunning = SystemInfoUtils.isServiceRunning(this,
                "com.angluswang.mobilesafe.service.CallSafeService");
        if (serviceRunning) {
            sivBlackNum.setChecked(true);
        } else {
            sivBlackNum.setChecked(false);
        }

        sivBlackNum.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (sivBlackNum.isChecked()) {
                    sivBlackNum.setChecked(false);
                    stopService(new Intent(SettingActivity.this,
                            CallSafeService.class));// 停止拦截服务
                } else {
                    sivBlackNum.setChecked(true);
                    startService(new Intent(SettingActivity.this,
                            CallSafeService.class));// 开启拦截服务
                }
            }
        });
    }

    /**
     * 修改提示框显示风格
     */
    private void initAddressStyle() {
        scvAddressStyle = (SettingClickView) findViewById(R.id.scv_address_style);
        scvAddressStyle.setTitle("归属地提示框风格");

        int style = mPref.getInt("address_style", 0);
        scvAddressStyle.setDesc(items[style]);

        scvAddressStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingleChooseDialog();
            }
        });
    }

    private final String[] items = new String[]{"半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿"};

    /**
     * 弹出一个单选框列表 对话框（选择风格）
     */
    private void showSingleChooseDialog() {
        int style = mPref.getInt("address_style", 0); // 读取保存的风格

        new AlertDialog.Builder(this).setTitle("归属地提示框风格")
                .setIcon(R.mipmap.ic_launcher)
                .setSingleChoiceItems(items, style,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 保存选择的风格
                                mPref.edit().putInt("address_style", which).apply();

                                dialog.dismiss(); // 选择后，让dialog 消失

                                scvAddressStyle.setDesc(items[which]); // 更新组合控件的描述
                            }
                        })
                .setNegativeButton("取消", null).show();
    }

    /**
     * 修改归属地提示框显示位置
     */
    private void initAddressLocation() {
        scvAddressLocation = (SettingClickView) findViewById(R.id.scv_address_location);
        scvAddressLocation.setTitle("归属地提示框显示设置");
        scvAddressLocation.setDesc("设置归属地提示框的显示位置");
        scvAddressLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到归属地提示框设置界面
                startActivity(new Intent(SettingActivity.this, DragViewActivity.class));
            }
        });
    }
}
