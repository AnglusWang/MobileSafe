package com.angluswang.mobilesafe.engine;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug;

import com.angluswang.mobilesafe.R;
import com.angluswang.mobilesafe.entity.TaskInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AnglusWang on 2016/8/6.
 * TaskInfo 逻辑类
 */

public class TaskInfoParser {

    public static List<TaskInfo> getTaskInfos(Context context) {

        PackageManager packageManager = context.getPackageManager();
        List<TaskInfo> TaskInfos = new ArrayList<>();

        // 获取到进程管理器
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        // 获取到手机上面所有运行的进程
        List<ActivityManager.RunningAppProcessInfo> appProcesses =
                activityManager.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo
                : appProcesses) {

            TaskInfo taskInfo = new TaskInfo();
            // 获取到进程的名字
            String processName = runningAppProcessInfo.processName;
            taskInfo.setPackageName(processName);

            try {
                Debug.MemoryInfo[] memoryInfo =
                        activityManager.getProcessMemoryInfo(new int[]{runningAppProcessInfo.pid});
                // 设置占用内存大小
                int totalPrivateDirty = memoryInfo[0].getTotalPrivateDirty() * 1024;
                taskInfo.setMemorySize(totalPrivateDirty);

                PackageInfo packageInfo = packageManager.getPackageInfo(processName, 0);

                // /获取到图片
                Drawable icon = packageInfo.applicationInfo.loadIcon(packageManager);
                taskInfo.setIcon(icon);

                // 获取到应用的名字
                String appName =
                        packageInfo.applicationInfo.loadLabel(packageManager).toString();
                taskInfo.setAppName(appName);

                int flags = packageInfo.applicationInfo.flags;
                //ApplicationInfo.FLAG_SYSTEM 表示系统应用程序
                if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0) {    //系统应用
                    taskInfo.setUserApp(false);
                } else {
                    taskInfo.setUserApp(true);
                }

            } catch (Exception e) {
                e.printStackTrace();

                // 系统核心库里面有些系统没有图标。必须给一个默认的图标
                taskInfo.setAppName(processName);
                taskInfo.setIcon(context.getResources().getDrawable(R.mipmap.ic_launcher));
            }
            TaskInfos.add(taskInfo);
        }
        return TaskInfos;
    }
}
