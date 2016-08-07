package com.angluswang.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.angluswang.mobilesafe.R;
import com.angluswang.mobilesafe.utils.SharedPreferencesUtils;

/**
 * Created by AnglusWang on 2016/8/7.
 * 任务管理器的设置界面
 */

public class TaskManagerSettingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager_setting);

        initView();
    }

    private void initView() {

        CheckBox cbShowStatus = (CheckBox) findViewById(R.id.cb_show_system);

        //设置是否选中
        cbShowStatus.setChecked(SharedPreferencesUtils.getBoolean(
                TaskManagerSettingActivity.this, "is_show_system", false));
        cbShowStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferencesUtils.saveBoolean(
                        TaskManagerSettingActivity.this, "is_show_system", isChecked);
            }
        });
    }
}
