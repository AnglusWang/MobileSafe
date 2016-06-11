package com.example.angluswang.mobilesafe.db.dao;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Jeson on 2016/6/11.
 * 归属地查询工具
 */

public class AddressDao {

    // 注意,该路径必须是data/data目录的文件,否则数据库访问不到
    private static final String PATH =
            "data/data/com.example.angluswang.mobilesafe/files/address.db";

    public static String getAddress(String number) {
        //获取数据对象
        SQLiteDatabase database = SQLiteDatabase.openDatabase(PATH, null,
                SQLiteDatabase.OPEN_READONLY);

        return null;
    }
}
