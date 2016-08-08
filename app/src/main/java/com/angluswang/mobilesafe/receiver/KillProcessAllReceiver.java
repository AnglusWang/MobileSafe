package com.angluswang.mobilesafe.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.List;

/**
 * Created by AnglusWang on 2016/8/8.
 * 杀掉所有进程
 */

public class KillProcessAllReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //获取到进程管理器
        ActivityManager am =
                (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);

        //获取到手机上面所以正在运行的进程
        List<ActivityManager.RunningAppProcessInfo> appProcesses =
                am.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo
                : appProcesses) {
            am.killBackgroundProcesses(runningAppProcessInfo.processName);
        }
    }
}
