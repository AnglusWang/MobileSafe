package com.example.angluswang.mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.angluswang.mobilesafe.entity.BlackNumberInfo;

import java.util.ArrayList;
import java.util.List;

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
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("number", number);
        cv.put("mode", mode);
        long rowId = db.insert("black_number", null, cv);
        return rowId != -1;
    }

    /**
     * 把号码移黑名单
     *
     * @param number
     * @return
     */
    public boolean delete(String number) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int rowNumber = db.delete("black_number", "number=?", new String[]{number});
        return rowNumber == 1;
    }

    /**
     * 改变拦截模式
     *
     * @param number
     * @return
     */
    public boolean changeMode(String number, String mode) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("mode", mode);
        int rowNumber = db.update("black_number", cv, "number=?", new String[]{number});
        return rowNumber != 0;
    }

    /**
     * 查找黑名单的拦截模式
     *
     * @return
     */
    public String findNumber(String number) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String mode = "";
        Cursor cursor = db.query("black_number", new String[]{"mode"},
                "number=?", new String[]{number}, null, null, null);
        if (cursor.moveToNext()) {
            mode = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return mode;
    }

    /**
     * 查询所有的黑名单
     */
    public List<BlackNumberInfo> findAllNumber() {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<BlackNumberInfo> blackNumInfos = new ArrayList<>();
        Cursor cursor = db.query("black_number", new String[]{"number", "mode"},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            BlackNumberInfo info = new BlackNumberInfo();
            info.setNumber(cursor.getString(0));
            info.setMode(cursor.getString(1));
            blackNumInfos.add(info);
        }
        cursor.close();
        db.close();
        return blackNumInfos;
    }

}
