package com.angluswang.mobilesafe.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.angluswang.mobilesafe.db.AppLockOpenHelper;

/**
 * Created by AnglusWang on 2016/8/8.
 * 程序锁
 */

public class AppLockDao {

    private AppLockOpenHelper mHelper;

    public AppLockDao(Context context) {
        mHelper = new AppLockOpenHelper(context);
    }

    /**
     * 将加锁的程序 添加到数据库中
     */
    public void add(String packageName) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("package_name", packageName);
        db.insert("info", null, values);
        db.close();
    }

    /**
     * 删除 从数据库中移除该程序
     */
    public void delete(String packageName) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        db.delete("info", "package_name=?", new String[]{packageName});
        db.close();
    }

    /**
     * 查询 当前包是否在程序锁的数据库中
     */
    public boolean find(String packageName) {
        boolean result = false;
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query("info", null,
                "package_name=?", new String[]{packageName}, null, null, null);
        while (cursor.moveToNext()) {
            result = true;
        }
        cursor.close();
        db.close();
        return result;
    }
}
