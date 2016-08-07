package com.angluswang.mobilesafe.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by AnglusWang on 2016/8/6.
 * 系统相关信息获取 工具类
 */

public class SystemInfoUtils {

    // 判断服务是否正在运行
    public static boolean isServiceRunning(Context context, String className) {
        ActivityManager am = (ActivityManager) context.
                getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> infos = am.getRunningServices(200);

        for (ActivityManager.RunningServiceInfo info : infos) {
            String serviceClassName = info.service.getClassName();
            if (className.equals(serviceClassName)) {
                return true;
            }
        }
        return false;
    }

    public static int getProcessCount(Context context) {
        // 得到进程管理者
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        // 获取手机上所有在运行的进程
        List<ActivityManager.RunningAppProcessInfo> runningAppProcess =
                activityManager.getRunningAppProcesses();
        return runningAppProcess.size();
    }

    public static long getAvailMem(Context context) {

        // 得到进程管理者
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();

        // 获取到内存的基本信息
        activityManager.getMemoryInfo(memoryInfo);
        // 获取到剩余内存
        return memoryInfo.availMem;
    }

    public static long getTotalMem() {
        // 获取到总内存
        try {
            //  "/proc/meminfo" 配置文件的路径 , 解决低版本获取 TotalMem 的问题
            FileInputStream fis = new FileInputStream(new File("/proc/meminfo"));

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(fis));
            String readLine = reader.readLine();
            StringBuffer sb = new StringBuffer();

            for (char c : readLine.toCharArray()) {
                if (c >= '0' && c <= '9') {
                    sb.append(c);
                }
            }
            return Long.parseLong(sb.toString()) * 1024;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
