package com.angluswang.mobilesafe.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.angluswang.mobilesafe.R;
import com.angluswang.mobilesafe.engine.TaskInfoParser;
import com.angluswang.mobilesafe.entity.TaskInfo;
import com.angluswang.mobilesafe.utils.SystemInfoUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
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
    private ArrayList<TaskInfo> userTaskInfos;
    private ArrayList<TaskInfo> systemAppInfos;

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

                userTaskInfos = new ArrayList<>();
                systemAppInfos = new ArrayList<>();
                for (TaskInfo taskInfo : taskInfos) {

                    if (taskInfo.isUserApp()) {
                        userTaskInfos.add(taskInfo);
                    } else {
                        systemAppInfos.add(taskInfo);
                    }

                }

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

    private class TaskManagerAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return taskInfos.size();
        }

        @Override
        public Object getItem(int position) {

            if (position == 0) {
                return null;
            } else if (position == userTaskInfos.size() + 1) {
                return null;
            }

            TaskInfo taskInfo;
            if (position < (userTaskInfos.size() + 1)) {    // 用户程序
                taskInfo = userTaskInfos.get(position - 1);  // 多了一个textView的标签
            } else {    // 系统程序
                int location = position - userTaskInfos.size() - 2;
                taskInfo = systemAppInfos.get(location);
            }

            return taskInfo;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (position == 0) {
                // 第0个位置显示的应该是 用户程序的个数的标签
                TextView tv = new TextView(getApplicationContext());
                tv.setBackgroundColor(Color.GRAY);
                tv.setTextColor(Color.WHITE);
                tv.setText("用户程序：" + userTaskInfos.size() + "个");
                return tv;
            } else if (position == (userTaskInfos.size() + 1)) {
                TextView tv = new TextView(getApplicationContext());
                tv.setBackgroundColor(Color.GRAY);
                tv.setTextColor(Color.WHITE);
                tv.setText("系统程序：" + systemAppInfos.size() + "个");
                return tv;
            }

            ViewHolder holder;
            View view;
            if (convertView != null && convertView instanceof LinearLayout) {

                view = convertView;
                holder = (ViewHolder) view.getTag();

            } else {
                view = View.inflate(TaskManagerActivity.this,
                        R.layout.item_task_manager, null);
                holder = new ViewHolder();

                holder.imgAppIcon = (ImageView) view.findViewById(R.id.img_app_icon);
                holder.tvAppName = (TextView) view.findViewById(R.id.tv_app_name);
                holder.tvAppMemorySize = (TextView) view.findViewById(R.id.tv_app_memory_size);
                holder.tvAppStatus = (CheckBox) view.findViewById(R.id.tv_app_status);

                view.setTag(holder);
            }

            TaskInfo taskInfo;
            if (position < (userTaskInfos.size() + 1)) {    // 用户程序
                taskInfo = userTaskInfos.get(position - 1);
            } else {    // 系统程序
                int location = position - userTaskInfos.size() - 2;
                taskInfo = systemAppInfos.get(location);
            }

            // 这个是设置图片控件的大小
            // holder.iv_app_icon.setBackgroundDrawable(d)
            // 设置图片本身的大小
            holder.imgAppIcon.setImageDrawable(taskInfo.getIcon());
            holder.tvAppName.setText(taskInfo.getAppName());
            holder.tvAppMemorySize.setText("内存占用:" + Formatter.formatFileSize(
                    TaskManagerActivity.this, taskInfo.getMemorySize()));

            return view;
        }
    }

    static class ViewHolder {
        ImageView imgAppIcon;
        TextView tvAppName;
        TextView tvAppMemorySize;
        TextView tvAppStatus;
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
}
