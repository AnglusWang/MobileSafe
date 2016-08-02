package com.example.angluswang.mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;

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
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        SystemClock.sleep(3000);
        return blackNumInfos;
    }

    /**
     * 分页加载数据
     *
     * @param pageNumber 当前页数
     * @param pageSize   每一页的数据个数
     * @return
     */
    public List<BlackNumberInfo> findPage(int pageNumber, int pageSize) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select number,mode from black_number limit ? offset ?",
                new String[]{String.valueOf(pageSize),
                String.valueOf(pageSize * pageNumber)});
        List<BlackNumberInfo> blackNumberInfos = new ArrayList<>();
        while (cursor.moveToNext()) {
            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
            blackNumberInfo.setMode(cursor.getString(1));
            blackNumberInfo.setNumber(cursor.getString(0));
            blackNumberInfos.add(blackNumberInfo);
        }
        cursor.close();
        db.close();
        return blackNumberInfos;
    }

    /**
     * 分批加载数据
     * @param startIndex  开始的位置
     * @param maxCount    每页展示的最大的条目
     * @return
     */
    public List<BlackNumberInfo> findBatch(int startIndex, int maxCount) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select number,mode from black_number limit ? offset ?",
                new String[]{String.valueOf(maxCount),
                String.valueOf(startIndex)});
        List<BlackNumberInfo> blackNumberInfos = new ArrayList<>();
        while (cursor.moveToNext()) {
            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
            blackNumberInfo.setMode(cursor.getString(1));
            blackNumberInfo.setNumber(cursor.getString(0));
            blackNumberInfos.add(blackNumberInfo);
        }
        cursor.close();
        db.close();
        return blackNumberInfos;
    }

    /**
     * 获取总的记录数
     * @return counts
     */
    public int getTotalNumber(){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from black_number", null);
        cursor.moveToNext();
        int counts = cursor.getInt(0);
        cursor.close();
        db.close();
        return counts;
    }

}
