package com.angluswang.mobilesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by AnglusWang on 2016/8/7.
 * 数据保存获取 工具类
 */

public class SharedPreferencesUtils {

    private static final String SP_NAME = "config";

    public static void saveBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, 0);
        sp.edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, 0);
        return sp.getBoolean(key, defValue);

    }
}
