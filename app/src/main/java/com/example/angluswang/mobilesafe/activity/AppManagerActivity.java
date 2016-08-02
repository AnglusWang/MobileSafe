package com.example.angluswang.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Formatter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.angluswang.mobilesafe.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_app_manager)
public class AppManagerActivity extends Activity {

    @ViewInject(R.id.lv_app_manager)
    private ListView lvApp;
    @ViewInject(R.id.tv_rom)
    private TextView tvRom;
    @ViewInject(R.id.tv_sd)
    private TextView tvSd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
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
