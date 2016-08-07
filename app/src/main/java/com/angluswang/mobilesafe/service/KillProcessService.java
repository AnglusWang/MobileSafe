package com.angluswang.mobilesafe.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import java.util.List;

/**
 * Created by AnglusWang on 2016/8/7.
 * 自动清理进程服务
 */

public class KillProcessService extends Service {

    private LockScreenReceiver receiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class LockScreenReceiver extends BroadcastReceiver {
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
                Log.e("killInfos ======= :", runningAppProcessInfo.processName);
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        receiver = new LockScreenReceiver();
        //锁屏的过滤器
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        //注册一个锁屏的广播
        registerReceiver(receiver, filter);

//		Timer timer = new Timer();
//		TimerTask task = new TimerTask() {
//			@Override
//			public void run() {
//				// 写我们的业务逻辑
//				System.out.println("我被调用了");
//			}
//		};
//
//		/**
//		 * 第一个参数  表示用那个类进行调度
//		 * 第二个参数 表示时间
//		 */
//		timer.schedule(task, 0,1000);  //进行定时调度
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unregisterReceiver(receiver);
        receiver = null;
    }
}
