package com.angluswang.mobilesafe.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;

import com.angluswang.mobilesafe.activity.activity.EnterPwdActivity;
import com.angluswang.mobilesafe.dao.AppLockDao;

import java.util.List;

/**
 * Created by AnglusWang on 2016/8/9.
 * 看门狗服务
 */

public class WatchDogService extends Service {

    private ActivityManager am; // 进程管理器
    private AppLockDao dao;

    private boolean falg = false;
    private List<String> appLockInfos;
    private WatchDogReceiver receiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //临时停止保护的包名
    private String tempStopProtectPackageName;

    private class WatchDogReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().
                    equals("com.angluswang.mobilesafe.stopprotect")) {
                //获取到停止保护的对象
                tempStopProtectPackageName = intent.getStringExtra("packageName");
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                tempStopProtectPackageName = null;
                // 让狗休息
                falg = false;
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                //让狗继续干活
                if (!falg) {
                    startWatchDog();
                }
            }
        }
    }

    private class AppLockContentObserver extends ContentObserver {

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public AppLockContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            appLockInfos = dao.findAll();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // 注册一个内容观察者
        getContentResolver().registerContentObserver(
                Uri.parse("content://com.angluswang.mobilesafe.change"), true,
                new AppLockContentObserver(new Handler()));

        // 获得进程管理器
        am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        dao = new AppLockDao(this);

        appLockInfos = dao.findAll();
        //注册广播接受者
        receiver = new WatchDogReceiver();
        IntentFilter filter = new IntentFilter();
        //停止保护
        filter.addAction("com.angluswang.mobilesafe.stopprotect");
        /**
         * 注册一个锁屏的广播
         * 当屏幕锁住的时候。狗就休息
         * 屏幕解锁的时候。让狗活过来
         */
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(receiver, filter);

        /**
         * 1. 获得当前任务栈
         * 2. 得到最上面的进程
         */
        startWatchDog();
    }

    private boolean flag;

    private void startWatchDog() {
        new Thread() {
            @Override
            public void run() {
                flag = true;
                while (flag) {
                    // 为避免发生阻塞，开启子线程
                    // 获得当前正在运行的任务栈
                    List<ActivityManager.RunningTaskInfo> runningTasks = am.getRunningTasks(1);
                    // 获得最上面的进程
                    ActivityManager.RunningTaskInfo taskInfo = runningTasks.get(0);
                    String packageName = taskInfo.topActivity.getPackageName();
                    //让狗休息一会
                    SystemClock.sleep(30);
                    if (appLockInfos.contains(packageName)) {
                        if (!(packageName.equals(tempStopProtectPackageName))) {
                            // 进入输密码界面
                            Intent intent = new Intent(WatchDogService.this,
                                    EnterPwdActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            //停止保护的对象
                            intent.putExtra("packageName", packageName);
                            startActivity(intent);
                        }
                    }
                }
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        flag = false;
        unregisterReceiver(receiver);
        receiver = null;
    }
}
