package com.angluswang.mobilesafe.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Jeson on 2016/6/11.
 * 归属地查询工具
 */

public class AddressDao {

    // 注意,该路径必须是data/data目录的文件,否则数据库访问不到
    private static final String PATH =
            "/data/data/com.angluswang.mobilesafe/files/address.db";

    public static String getAddress(String number) {

        String address = "未知号码";
        SQLiteDatabase database = SQLiteDatabase.openDatabase(PATH, null,
                SQLiteDatabase.OPEN_READONLY);

        // 手机号特点 1 + (3,4,5,6,7,8) + (9 位数字)
        // 正则表达式  ^1[3-8]\d{9}$
        if (number.matches("^1[3-8]\\d{9}$")) { // 匹配手机号码
            Cursor cursor = database.rawQuery(
                    "select location from data2 where id=(select outkey from data1 where id=?)",
                    new String[]{number.substring(0, 7)});
            if (cursor.moveToNext()) {
                address = cursor.getString(0);
            }
            cursor.close();
        } else if (number.matches("^\\d+$")) { //匹配数字
            switch (number.length()) {
                case 3:
                    address = "报警电话";
                    break;
                case 4:
                    address = "模拟器号码";
                    break;
                case 5:
                    address = "客服电话";
                    break;
                case 7:
                case 8:
                    address = "本地号码";
                    break;
                default:
                    // 查询长途电话
                    if (number.startsWith("0") && number.length() > 10) { //可能是长途
                        // 有些区号是4位，有些是3位 （包括0）
                        // 先查4位的
                        Cursor cursor = database.rawQuery(
                                "select location from data2 where area=?",
                                new String[]{number.substring(1, 4)});
                        if (cursor.moveToNext()) {
                            address = cursor.getString(0);
                        } else {
                            cursor.close();

                            cursor = database.rawQuery(
                                    "select location from data2 where area=?",
                                    new String[]{number.substring(1, 3)});
                            if (cursor.moveToNext()) {
                                address = cursor.getString(0);
                            }
                            cursor.close();
                        }
                    }

                    break;
            }
        }
        database.close();
        return address;
    }
}
