package com.example.angluswang.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.angluswang.mobilesafe.R;
import com.example.angluswang.mobilesafe.engine.AppInfos;
import com.example.angluswang.mobilesafe.entity.AppInfo;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private class AppManagerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return appInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return appInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if (convertView == null) {

                holder = new ViewHolder();
                convertView = View.inflate(AppManagerActivity.this, R.layout.item_app_manager, null);
                holder.imgIcon = (ImageView) convertView.findViewById(R.id.img_icon);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tvLocation = (TextView) convertView.findViewById(R.id.tv_location);
                holder.tvApkSize = (TextView) convertView.findViewById(R.id.tv_apk_size);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            AppInfo info = appInfos.get(position);
            holder.imgIcon.setBackground(info.getIcon());
            holder.tvName.setText(info.getApkName());

            if (info.isRom()) {
                holder.tvLocation.setText("手机内存");
            } else {
                holder.tvLocation.setText("存储卡");
            }
            holder.tvApkSize.setText(
                    Formatter.formatFileSize(AppManagerActivity.this, info.getApkSize()));

            return convertView;
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
