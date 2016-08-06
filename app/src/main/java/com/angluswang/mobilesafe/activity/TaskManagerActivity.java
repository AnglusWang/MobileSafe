package com.angluswang.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.angluswang.mobilesafe.R;
import com.angluswang.mobilesafe.engine.TaskInfoParser;
import com.angluswang.mobilesafe.entity.TaskInfo;
import com.angluswang.mobilesafe.utils.SystemInfoUtils;
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

    private List<TaskInfo> taskInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        initView();
        initData();
    }

    private void initData() {

        new Thread() {
            public void run() {
                taskInfos = TaskInfoParser.getTaskInfos(TaskManagerActivity.this);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TaskManagerAdapter adapter = new TaskManagerAdapter();
                        lvProcess.setAdapter(adapter);
                    }
                });
            }
        }.start();
    }

    private void initView() {

        // 获取手机上所有在运行的进程
        int count = SystemInfoUtils.getProcessCount(this);
        tvTpCount.setText("运行中的进程：" + count + "个");

        // 获取内存的基本信息 及 剩余内存
        long availMem = SystemInfoUtils.getAvailMem(this);  // 系统剩余可用内存
        long totalMem = SystemInfoUtils.getTotalMem();  // 系统总内存
        tvTaskMemory.setText("剩余/总内存 " +
                Formatter.formatFileSize(TaskManagerActivity.this, availMem) + "/" +
                Formatter.formatFileSize(TaskManagerActivity.this, totalMem));

    }

    private class TaskManagerAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }
}
