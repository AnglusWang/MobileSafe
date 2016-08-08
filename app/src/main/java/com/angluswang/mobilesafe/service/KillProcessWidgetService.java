package com.angluswang.mobilesafe.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.RemoteViews;

import com.angluswang.mobilesafe.R;
import com.angluswang.mobilesafe.receiver.MyAppWidget;
import com.angluswang.mobilesafe.utils.SystemInfoUtils;

import java.util.Timer;
import java.util.TimerTask;

public class KillProcessWidgetService extends Service {

    private AppWidgetManager widgetManager;
    private Timer timer;
    private TimerTask timerTask;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //桌面小控件的管理者
        widgetManager = AppWidgetManager.getInstance(this);

        // 每隔 5 秒更新一下桌面
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                // 相关的处理操作
                // 初始化一个远程 View
                RemoteViews views =
                        new RemoteViews(getPackageName(), R.layout.process_widget);

                // 进程数
                int processCount = SystemInfoUtils.getProcessCount(getApplicationContext());
                views.setTextViewText(R.id.process_count,
                        "正在运行软件: " + String.valueOf(processCount));

                // 可用内存
                long availMem = SystemInfoUtils.getAvailMem(getApplicationContext());
                views.setTextViewText(R.id.process_memory,
                        "可用内存: " + Formatter.formatFileSize(getApplicationContext(), availMem));

                Intent intent = new Intent();
                intent.setAction("com.angluswang.kill_process_all");
                PendingIntent pendingIntent = PendingIntent.
                        getBroadcast(getApplicationContext(), 0, intent, 0);

                //设置点击事件
                views.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);

                // 初始化一个 ComponentName
                // 第二个参数表示 由哪个广播去处理当前的桌面小部件
                ComponentName provider =
                        new ComponentName(getApplicationContext(), MyAppWidget.class);

                // 更新桌面
                widgetManager.updateAppWidget(provider, views);
            }
        };
        timer.schedule(timerTask, 0, 5000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (timer != null || timerTask != null) {
            assert timer != null;
            timer.cancel();
            timerTask.cancel();
            timer = null;
            timerTask = null;
        }
    }

}
