package com.angluswang.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jeson on 2016/8/1.
 */

public class BlackNumberOpenHelper extends SQLiteOpenHelper {

    public BlackNumberOpenHelper(Context context) {
        super(context, "safe.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /**
         * black_number 表名
         * _id 主键 自增长
         * number 电话号码
         * mode 拦截模式：电话拦截、短信拦截
         */
        String sql = "create table black_number ( " +
                "_id integer primary key autoincrement, " +
                "number varchar(20), " +
                "mode varchar(2))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
