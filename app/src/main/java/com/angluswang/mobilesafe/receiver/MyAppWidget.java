package com.angluswang.mobilesafe.receiver;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.angluswang.mobilesafe.service.KillProcessWidgetService;

/**
 * Created by AnglusWang on 2016/8/8.
 * 桌面小部件 广播接受者
 */

public class MyAppWidget extends AppWidgetProvider {

    /**
     * 第一次创建的时候才会调用当前的生命周期的方法
     *
     * 当前的广播的生命周期只有10秒钟。
     * 不能做耗时的操作
     */
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

        Intent intent = new Intent(context,KillProcessWidgetService.class);
        context.startService(intent);
        System.out.println("onEnabled");
    }

    /**
     * 当桌面上面所有的桌面小控件都删除的时候才调用当前这个方法
     */
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);

        Intent intent = new Intent(context,KillProcessWidgetService.class);
        context.stopService(intent);
        System.out.println("onDisabled");
    }
}
