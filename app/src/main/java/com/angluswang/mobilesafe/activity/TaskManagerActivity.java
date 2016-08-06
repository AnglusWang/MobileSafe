package com.angluswang.mobilesafe.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.widget.ListView;
import android.widget.TextView;

import com.angluswang.mobilesafe.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by AnglusWang on 2016/8/6.
 * 进程管理 活动界面
 */

@ContentView(R.layout.activity_task_manager)
public class TaskManagerActivity extends Activity {

    @ViewInject(R.id.tv_task_process_count)
    private TextView tvTpCount; // 运行中进程总数
    @ViewInject(R.id.tv_task_memory)
    private TextView tvTaskMemory;

    @ViewInject(R.id.lv_process)
    private ListView lvProcess;

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
        // 得到进程管理者
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        // 获取手机上所有在运行的进程
        List<ActivityManager.RunningAppProcessInfo> runningAppProcess =
                activityManager.getRunningAppProcesses();
        int count = runningAppProcess.size();
        tvTpCount.setText("运行中的进程：" + count + "个");

        // 获取内存的基本信息 及 剩余内存
        ActivityManager.MemoryInfo memoryInfos = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfos);
        long availMem = memoryInfos.availMem;  // 系统剩余可用内存
        long totalMem = memoryInfos.totalMem;  // 系统总内存
        tvTaskMemory.setText("剩余/总内存 " +
                Formatter.formatFileSize(TaskManagerActivity.this, availMem) + "/" +
                Formatter.formatFileSize(TaskManagerActivity.this, totalMem));

    }
}
