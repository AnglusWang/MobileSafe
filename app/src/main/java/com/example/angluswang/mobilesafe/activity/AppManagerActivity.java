package com.example.angluswang.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
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
public class AppManagerActivity extends Activity
        implements View.OnClickListener {

    @ViewInject(R.id.lv_app_manager)
    private ListView lvApp;
    @ViewInject(R.id.tv_rom)
    private TextView tvRom;
    @ViewInject(R.id.tv_sd)
    private TextView tvSd;
    @ViewInject(R.id.tv_app)
    private TextView tvApp;

    private TextView tvUninstall;
    private TextView tvRun;
    private TextView tvShare;
    private TextView tvDetail;

    private List<AppInfo> appInfos;
    private ArrayList<AppInfo> userAppInfos; //用户程序
    private ArrayList<AppInfo> systemAppInfos; //系统程序
    private PopupWindow pw;     // 弹出窗口(卸载、运行、分享)

    private AppInfo clickAppInfo; // 被点击的应用信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_popup_uninstall:
//                Log.i("tv_popup_uninstall:", "被点击了");
                Intent uninstallIntent = new Intent("android.intent.action.DELETE",
                        Uri.parse("package:" + clickAppInfo.getApkPackageName()));
                startActivity(uninstallIntent);
                popupWindowDismis();
                break;
            case R.id.tv_popup_run:
//                Log.i("tv_popup_run:", "被点击了");
                Intent runIntent = getPackageManager().
                        getLaunchIntentForPackage(clickAppInfo.getApkPackageName());
                startActivity(runIntent);
                popupWindowDismis();
                break;
            case R.id.tv_popup_share:
//                Log.i("tv_popup_share:", "被点击了");
                Intent shareIntent = new Intent("android.intent.action.SEND");
                shareIntent.setType("text/plain");
                shareIntent.putExtra("android.intent.extra.SUBJECT", "f分享");
                shareIntent.putExtra("android.intent.extra.TEXT",
                        "Hi！推荐您使用软件：" + clickAppInfo.getApkName() +
                                "下载地址:" + "https://play.google.com/store/apps/details?id=" +
                                clickAppInfo.getApkPackageName());
                startActivity(Intent.createChooser(shareIntent, "分享"));
                popupWindowDismis();
                break;
            case R.id.tv_popup_detail:
//                Log.i("tv_popup_detail:", "被点击了");
                Intent detailIntent = new Intent();
                detailIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                detailIntent.addCategory(Intent.CATEGORY_DEFAULT);
                detailIntent.setData(Uri.parse("package:" + clickAppInfo.getApkPackageName()));
                startActivity(detailIntent);
                break;
            default:
                break;
        }
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
        tvRom.setText("内存卡可用：" + Formatter.formatFileSize(this, RomFreeSpace));
        tvSd.setText("SD卡可用：" + Formatter.formatFileSize(this, sdFreeSpace));

        // 应用App ListView 滑动监听
        lvApp.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                popupWindowDismis();

                if (userAppInfos != null && systemAppInfos != null) {
                    if (firstVisibleItem > (userAppInfos.size() + 1)) {
                        tvApp.setText("系统程序(" + systemAppInfos.size() + ")");
                    } else {
                        tvApp.setText("用户程序(" + userAppInfos.size() + ")");
                    }
                }
            }
        });

        // 为ListView 设置点击侦听
        lvApp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取当前点击的 Item 对象
                Object obj = lvApp.getItemAtPosition(position);
                if (obj != null && obj instanceof AppInfo) { // 判断当前对象不为空，且为AppInfo 对象
                    View contentView = View.inflate(AppManagerActivity.this,
                            R.layout.item_popup_app, null);

                    clickAppInfo = (AppInfo) obj;
                    // 为运行、卸载、分析、详细 设置 点击侦听
                    tvUninstall = (TextView) contentView.findViewById(R.id.tv_popup_uninstall);
                    tvRun = (TextView) contentView.findViewById(R.id.tv_popup_run);
                    tvShare = (TextView) contentView.findViewById(R.id.tv_popup_share);
                    tvDetail = (TextView) contentView.findViewById(R.id.tv_popup_detail);

                    tvUninstall.setOnClickListener(AppManagerActivity.this);
                    tvRun.setOnClickListener(AppManagerActivity.this);
                    tvShare.setOnClickListener(AppManagerActivity.this);
                    tvDetail.setOnClickListener(AppManagerActivity.this);

                    // Window 上只有一个 popupWindow 给用户
                    popupWindowDismis();

                    pw = new PopupWindow(contentView, -2, -2); // -2 表示包裹内容
                    //需要注意：使用PopupWindow 必须设置背景。不然没有动画
                    pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 透明背景

                    int[] location = new int[2];
                    view.getLocationInWindow(location); // location 0，1 表示 x,y 坐标

                    pw.showAtLocation(parent, Gravity.START + Gravity.TOP, 60, location[1]);

                    // 为 popupWindow 添加动画效果
                    ScaleAnimation scaleAnim = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

                    scaleAnim.setDuration(200);
                    contentView.startAnimation(scaleAnim);
                }
            }
        });
    }

    private void popupWindowDismis() {
        if (pw != null && pw.isShowing()) {
            pw.dismiss();
            pw = null;
        }
    }

    @Override
    protected void onDestroy() {
        popupWindowDismis();

        super.onDestroy();
    }
}
