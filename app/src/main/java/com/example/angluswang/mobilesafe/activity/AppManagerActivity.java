package com.example.angluswang.mobilesafe.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.angluswang.mobilesafe.R;
import com.example.angluswang.mobilesafe.engine.AppInfos;
import com.example.angluswang.mobilesafe.entity.AppInfo;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_app_manager)
public class AppManagerActivity extends Activity {

    @ViewInject(R.id.lv_app_manager)
    private ListView lvApp;
    @ViewInject(R.id.tv_rom)
    private TextView tvRom;
    @ViewInject(R.id.tv_sd)
    private TextView tvSd;

    private List<AppInfo> appInfos;
    private ArrayList<AppInfo> userAppInfos; //用户程序
    private ArrayList<AppInfo> systemAppInfos; //系统程序

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private class AppManagerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return userAppInfos.size() + 1 + systemAppInfos.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            if (position == 0) {
                return null;
            } else if (position == userAppInfos.size() + 1) {
                return null;
            }

            AppInfo appInfo;
            if (position < userAppInfos.size() + 1) {
                //把多出来的特殊的条目减掉
                appInfo = userAppInfos.get(position - 1);

            } else {

                int location = userAppInfos.size() + 2;
                appInfo = systemAppInfos.get(position - location);
            }

            return appInfo;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (position == 0) { //如果当前的position等于0 表示应用程序

                TextView textView = new TextView(AppManagerActivity.this);
                textView.setTextColor(Color.WHITE);
                textView.setBackgroundColor(Color.GRAY);
                textView.setText("用户程序(" + userAppInfos.size() + ")");

                return textView;

            } else if (position == userAppInfos.size() + 1) { //表示系统程序

                TextView textView = new TextView(AppManagerActivity.this);
                textView.setTextColor(Color.WHITE);
                textView.setBackgroundColor(Color.GRAY);
                textView.setText("系统程序(" + systemAppInfos.size() + ")");

                return textView;

            }

            View view;
            ViewHolder holder;
            if (convertView != null && convertView instanceof LinearLayout) {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            } else {
                holder = new ViewHolder();
                view = View.inflate(AppManagerActivity.this, R.layout.item_app_manager, null);
                holder.imgIcon = (ImageView) view.findViewById(R.id.img_icon);
                holder.tvName = (TextView) view.findViewById(R.id.tv_name);
                holder.tvLocation = (TextView) view.findViewById(R.id.tv_location);
                holder.tvApkSize = (TextView) view.findViewById(R.id.tv_apk_size);

                view.setTag(holder);
            }

            // 特殊条目处理
            AppInfo appInfo;
            if (position < userAppInfos.size() + 1) {
                //把多出来的特殊的条目减掉
                appInfo = userAppInfos.get(position - 1);
            } else {
                int location = userAppInfos.size() + 2;
                appInfo = systemAppInfos.get(position - location);
            }

            holder.imgIcon.setBackground(appInfo.getIcon());
            holder.tvName.setText(appInfo.getApkName());

            if (appInfo.isRom()) {
                holder.tvLocation.setText("手机内存");
            } else {
                holder.tvLocation.setText("外部存储");
            }
            holder.tvApkSize.setText(
                    Formatter.formatFileSize(AppManagerActivity.this, appInfo.getApkSize()));

            return view;
        }
    }

    private static class ViewHolder {
        ImageView imgIcon;
        TextView tvName;
        TextView tvLocation;
        TextView tvApkSize;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            AppManagerAdapter Adapter = new AppManagerAdapter();
            lvApp.setAdapter(Adapter);
        }
    };

    private void initData() {

        new Thread() {
            @Override
            public void run() {
                // 获取所有安装到手机上的应用程序
                appInfos = AppInfos.getAppInfos(AppManagerActivity.this);

                //appInfos拆成 用户程序的集合 + 系统程序的集合

                //用户程序的集合
                userAppInfos = new ArrayList<>();
                //系统程序的集合
                systemAppInfos = new ArrayList<>();

                for (AppInfo appInfo : appInfos) {
                    //用户程序
                    if (appInfo.isUserApp()) {
                        userAppInfos.add(appInfo);
                    } else {
                        systemAppInfos.add(appInfo);
                    }
                }

                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    private void initView() {
        ViewUtils.inject(this);

        // 获取 ROM 内存运行的剩余空间
        long RomFreeSpace = Environment.getDataDirectory().getFreeSpace();
        // 获取 SD 卡的剩余空间
        long sdFreeSpace = Environment.getExternalStorageDirectory().getFreeSpace();

        // 格式化大小，并设置文本内容
        tvRom.setText(Formatter.formatFileSize(this, RomFreeSpace));
        tvSd.setText(Formatter.formatFileSize(this, sdFreeSpace));
    }
}
