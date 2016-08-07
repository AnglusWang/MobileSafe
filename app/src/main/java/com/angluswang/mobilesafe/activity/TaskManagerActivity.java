package com.angluswang.mobilesafe.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.angluswang.mobilesafe.R;
import com.angluswang.mobilesafe.engine.TaskInfoParser;
import com.angluswang.mobilesafe.entity.TaskInfo;
import com.angluswang.mobilesafe.utils.SharedPreferencesUtils;
import com.angluswang.mobilesafe.utils.SystemInfoUtils;
import com.angluswang.mobilesafe.utils.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import static com.angluswang.mobilesafe.R.id.tv_task_memory;
import static com.angluswang.mobilesafe.R.id.tv_task_process_count;

/**
 * Created by AnglusWang on 2016/8/6.
 * 进程管理 活动界面
 */

@ContentView(R.layout.activity_task_manager)
public class TaskManagerActivity extends Activity {

    @ViewInject(tv_task_process_count)
    private TextView tvTpCount; // 运行中进程总数
    @ViewInject(tv_task_memory)
    private TextView tvTaskMemory;

    @ViewInject(R.id.lv_process)
    private ListView lvProcess;

    private List<TaskInfo> taskInfos;
    private ArrayList<TaskInfo> userTaskInfos;
    private ArrayList<TaskInfo> systemAppInfos;

    private TaskManagerAdapter adapter;
    private int processCount; // 共有多少个进程

    private long availMem; // 系统剩余可用内存
    private long totalMem; // 系统总内存

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
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
                        adapter = new TaskManagerAdapter();
                        lvProcess.setAdapter(adapter);
                    }
                });
            }
        }.start();
    }

    private class TaskManagerAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            boolean result = SharedPreferencesUtils.getBoolean(
                    TaskManagerActivity.this, "is_show_system", false);
            if (result) {
                return userTaskInfos.size() + systemAppInfos.size() + 2;
            } else {
                return userTaskInfos.size() + 1;
            }
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
            holder.tvAppMemorySize.setText("内存占用:" +
                    Formatter.formatFileSize(
                            TaskManagerActivity.this, taskInfo.getMemorySize()));
            // 设置是否勾选状态
            if (taskInfo.isChecked()) {
                holder.tvAppStatus.setChecked(true);
            } else {
                holder.tvAppStatus.setChecked(false);
            }

            // 判断当前展示的 item 是否是自己的程序。
            // 如果是,就把程序给隐藏
            if (taskInfo.getPackageName().equals(getPackageName())) {
                //隐藏
                holder.tvAppStatus.setVisibility(View.INVISIBLE);
            } else {
                //显示
                holder.tvAppStatus.setVisibility(View.VISIBLE);
            }

            return view;
        }
    }

    static class ViewHolder {
        ImageView imgAppIcon;
        TextView tvAppName;
        TextView tvAppMemorySize;
        CheckBox tvAppStatus;
    }

    private void initView() {

        // 获取手机上所有在运行的进程
        processCount = SystemInfoUtils.getProcessCount(this);
        tvTpCount.setText("运行中的进程：" + processCount + "个");

        // 获取内存的基本信息 及 剩余内存
        availMem = SystemInfoUtils.getAvailMem(this);  // 系统剩余可用内存
        totalMem = SystemInfoUtils.getTotalMem();  // 系统总内存
        tvTaskMemory.setText("剩余/总内存 " +
                Formatter.formatFileSize(TaskManagerActivity.this, availMem) + "/" +
                Formatter.formatFileSize(TaskManagerActivity.this, totalMem));

        // 为 ListView 设置点击事件
        lvProcess.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Object object = lvProcess.getItemAtPosition(position);
                if (object != null && object instanceof TaskInfo) { // 判断是否是进程条目

                    TaskInfo taskInfo = (TaskInfo) object;
                    ViewHolder holder = (ViewHolder) view.getTag();// 拿到 item 条目对象
                    // 如果是自己，不作处理
                    if (taskInfo.getPackageName().equals(getPackageName())) {
                        return;
                    }
                    if (taskInfo.isChecked()) { // 如果勾选了，设置为未勾选，反之。。。
                        taskInfo.setChecked(false);
                        holder.tvAppStatus.setChecked(false);
                    } else {
                        taskInfo.setChecked(true);
                        holder.tvAppStatus.setChecked(true);
                    }
                }
            }
        });
    }

    /**
     * 全选
     *
     * @param view
     */
    public void selectAll(View view) {
        for (TaskInfo info : userTaskInfos) {

            // 判断当前的用户程序是不是自己的程序。
            // 如果是自己的程序。那么就把文本框隐藏
            if (info.getPackageName().equals(getPackageName())) {
                continue;
            }
            info.setChecked(true);
        }
        for (TaskInfo info : systemAppInfos) {
            info.setChecked(true);
        }
        adapter.notifyDataSetChanged(); // 数据发生改变是刷新界面
    }

    /**
     * 反选
     *
     * @param view
     */
    public void selectOppsite(View view) {
        for (TaskInfo info : userTaskInfos) {
            if (info.getPackageName().equals(getPackageName())) {
                continue;
            }
            info.setChecked(!info.isChecked());
        }
        for (TaskInfo info : systemAppInfos) {
            info.setChecked(!info.isChecked());
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 清理进程
     *
     * @param view
     */
    public void killProcess(View view) {

        // 想杀死进程。首先必须得到进程管理器
        ActivityManager activityManager =
                (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        // 清理进程的集合
        List<TaskInfo> killLists = new ArrayList<>();
        // 清理的总共的进程个数
        int totalCount = 0;
        // 清理的进程的大小
        int killMem = 0;
        for (TaskInfo taskInfo : userTaskInfos) {

            if (taskInfo.isChecked()) {
                killLists.add(taskInfo);
                totalCount++;
                killMem += taskInfo.getMemorySize();

            }
        }

        for (TaskInfo taskInfo : systemAppInfos) {

            if (taskInfo.isChecked()) {
                killLists.add(taskInfo);
                totalCount++;
                killMem += taskInfo.getMemorySize();
            }
        }

        // 注意： 当集合在迭代的时候。不能修改集合的大小
        for (TaskInfo taskInfo : killLists) {
            // 判断是否是用户app
            if (taskInfo.isUserApp()) {
                userTaskInfos.remove(taskInfo);
                // 杀死进程 参数表示包名
                activityManager.killBackgroundProcesses(taskInfo
                        .getPackageName());
            } else {
                systemAppInfos.remove(taskInfo);
                // 杀死进程 参数表示包名
                activityManager.killBackgroundProcesses(taskInfo
                        .getPackageName());
            }
        }

        UIUtils.showToast(TaskManagerActivity.this,
                "共清理" + totalCount + "个进程,释放"
                        + Formatter.formatFileSize(TaskManagerActivity.this, killMem)
                        + "内存");

        //processCount 表示总共有多少个进程
        //totalCount 当前清理了多少个进程
        processCount -= totalCount;
        tvTpCount.setText("运行中的进程：" + processCount + "个");
        tvTaskMemory.setText("剩余/总内存 " +
                Formatter.formatFileSize(TaskManagerActivity.this, availMem + killMem)
                + "/" + Formatter.formatFileSize(TaskManagerActivity.this, totalMem));

        // 刷新界面
        adapter.notifyDataSetChanged();
    }

    /**
     * 进程设置
     *
     * @param view
     */
    public void openSetting(View view) {
        startActivity(new Intent(TaskManagerActivity.this,
                TaskManagerSettingActivity.class));
    }

}
