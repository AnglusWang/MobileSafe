package com.example.angluswang.mobilesafe.db.dao;

import android.content.Context;

/**
 * Created by Jeson on 2016/8/1.
 */

public class BlackNumberDao {
    private final BlackNumberOpenHelper helper;

    public BlackNumberDao(Context context) {
        helper = new BlackNumberOpenHelper(context);
    }

    /**
     * 增加黑名单 及 拦截模式
     *
     * @param number
     * @param mode
     * @return
     */
    public boolean add(String number, String mode) {
        return false;
    }

    /**
     * 把号码移黑名单
     * @param number
     * @return
     */
    public boolean delete(String number) {
        return false;
    }

    /**
     * 改变拦截模式
     * @param number
     * @return
     */
    public boolean changeMode(String number) {
        return false;
    }

    /**
     * 查找黑名单
     * @return
     */
    public String queryNumber() {
        return null;
    }

}
