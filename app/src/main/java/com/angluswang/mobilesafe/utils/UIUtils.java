package com.angluswang.mobilesafe.utils;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by AnglusWang on 2016/8/5.
 *  UI 工具类
 */

public class UIUtils {

    /**
     * 弹出 Toast ： Toast 不能在子线程中弹出
     * @param context
     * @param msg
     */
    public static void showToast(final Activity context, final String msg) {
        if ("main".equals(Thread.currentThread().getName())) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        } else {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
