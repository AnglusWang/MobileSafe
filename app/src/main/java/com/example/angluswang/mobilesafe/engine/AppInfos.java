package com.example.angluswang.mobilesafe.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.angluswang.mobilesafe.entity.AppInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeson on 2016/8/2.
 * app 业务逻辑类
 */

public class AppInfos {

    public static List<AppInfo> getAppInfos(Context context) {

        List<AppInfo> packageAppInfos = new ArrayList<>();

        //获取到包的管理者
        PackageManager pm = context.getPackageManager();
        //获取到安装包
        //当发现 flag 为此 “@param flags Additional option flags.” 表示时，统一传入 0 即可
        List<PackageInfo> installedPackages = pm.getInstalledPackages(0);

        for (PackageInfo installedPackage : installedPackages) {

            AppInfo appInfo = new AppInfo();

            //获取到应用程序的图标
            Drawable drawable = installedPackage.applicationInfo.loadIcon(pm);

            appInfo.setIcon(drawable);

            //获取到应用程序的名字
            String apkName = installedPackage.applicationInfo.loadLabel(pm).toString();

            appInfo.setApkName(apkName);

            //获取到应用程序的包名
            String packageName = installedPackage.packageName;

            appInfo.setApkPackageName(packageName);

            //获取到apk资源的路径
            String sourceDir = installedPackage.applicationInfo.sourceDir;

            File file = new File(sourceDir);
            //apk的长度
            long apkSize = file.length();

            appInfo.setApkSize(apkSize);

            Log.i("appInfo:", "---------------------------");
            Log.i("appInfo:", "程序的名字:" + apkName);
            Log.i("appInfo:", "程序的包名:" + packageName);
            Log.i("appInfo:", "程序的大小:" + apkSize);


//          判断应用是系统应用 还是 用户应用
//            方式1： //data/app   system/app
//                      if (sourceDir.startsWith("/system"))...
//            方式2：
            //获取到安装应用程序的标记
            int flags = installedPackage.applicationInfo.flags;

            if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                //表示系统app
                appInfo.setUserApp(false);
            } else {
                //表示用户app
                appInfo.setUserApp(true);
            }

            if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
                //表示在sd卡
                appInfo.setRom(false);
            } else {
                //表示内存
                appInfo.setRom(true);
            }

            packageAppInfos.add(appInfo);
        }

        return packageAppInfos;
    }
}
