package com.angluswang.mobilesafe.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.angluswang.mobilesafe.dao.AppLockDao;

import java.util.List;

/**
 * Created by AnglusWang on 2016/8/9.
 * 看门狗服务
 */

public class WatchDogService extends Service {

    private ActivityManager am; // 进程管理器
    private AppLockDao dao;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // 获得进程管理器
        am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        dao = new AppLockDao(this);
        /**
         * 1. 获得当前任务栈
         * 2. 得到最上面的进程
         */
        startWatchDog();
    }

    private void startWatchDog() {
        new Thread(){
            @Override
            public void run() {
                while (true) {
                    // 为避免发生阻塞，开启子线程
                    // 获得当前正在运行的任务栈
                    List<ActivityManager.RunningTaskInfo> runningTasks = am.getRunningTasks(2);
                    // 获得最上面的进程
                    ActivityManager.RunningTaskInfo taskInfo = runningTasks.get(0);

                    String packageName = taskInfo.topActivity.getPackageName();
                    if (dao.find(packageName)) {
                        Log.i("app_lockInfo ===== ", packageName + "在程序锁数据库中");
                    } else {
                        Log.i("app_lockInfo ===== ", packageName + "查无此果~~~");
                    }
                }
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

}
