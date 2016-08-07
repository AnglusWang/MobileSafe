package com.angluswang.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.angluswang.mobilesafe.R;
import com.angluswang.mobilesafe.service.KillProcessService;
import com.angluswang.mobilesafe.utils.SharedPreferencesUtils;
import com.angluswang.mobilesafe.utils.SystemInfoUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by AnglusWang on 2016/8/7.
 * 任务管理器的设置界面
 */
@ContentView(R.layout.activity_task_manager_setting)
public class TaskManagerSettingActivity extends Activity {

    @ViewInject(R.id.cb_show_system)
    private CheckBox cbShowSystem;
    @ViewInject(R.id.cb_kill_process)
    private CheckBox cbKillProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        initView();
    }

    private void initView() {

        //设置是否选中
        cbShowSystem.setChecked(SharedPreferencesUtils.getBoolean(
                TaskManagerSettingActivity.this, "is_show_system", false));
        cbShowSystem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferencesUtils.saveBoolean(
                        TaskManagerSettingActivity.this, "is_show_system", isChecked);
            }
        });

        // 定时清理进程
        final Intent intent = new Intent(TaskManagerSettingActivity.this,
                KillProcessService.class);
        cbKillProcess.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startService(intent); // 开启服务
                } else {
                    stopService(intent);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        boolean status = SystemInfoUtils.isServiceRunning(this,
                "com.angluswang.mobilesafe.service.KillProcessService");
        if (status) {
            cbKillProcess.setChecked(true);
        } else {
            cbKillProcess.setChecked(false);
        }
    }
}
