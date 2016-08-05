package com.angluswang.mobilesafe.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by Jeson on 2016/7/28.
 */

public class ServiceStatusUtils {

    public static boolean isServiceRunning(Context context, String serviceName) {

        ActivityManager manager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceInfos = manager.getRunningServices(100);// 拿到真在运行的服务，100 表示最大100个。
        for (ActivityManager.RunningServiceInfo serviceInfo : serviceInfos) {
            String className = serviceInfo.getClass().getName();
            if (serviceName.equals(className)) {    // 服务正在运行。。。
                return true;
            }
        }
        return false;
    }
}
