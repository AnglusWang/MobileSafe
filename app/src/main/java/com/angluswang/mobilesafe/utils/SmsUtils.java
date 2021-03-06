package com.angluswang.mobilesafe.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Jeson on 2016/8/5.
 * 短信备份 工具类
 */

public class SmsUtils {

    /**
     * 短信备份接口
     */
    public interface BackUpSmsCallBack {

        public void before(int count);

        public void onBackUpSms(int process);

    }

    /**
     * 目的 ： 备份短信
     * <p>
     * 1 判断当前用户的手机上面是否有sd卡
     * 2 权限 ---
     * 使用内容观察者
     * 3 写短信(写到sd卡)
     */
    public static boolean backUp(Context context, BackUpSmsCallBack callBack) {

        // 判断 SD 卡状态
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // 如果能进来就说明用户有SD卡
            ContentResolver resolver = context.getContentResolver();
            // 获取短信的路径
            Uri uri = Uri.parse("content://sms/");
            // type = 1 接收短信    type = 2 发送短信
            Cursor cursor = resolver.query(uri,
                    new String[]{"address", "date", "type", "body"}, null, null, null);

            int count = cursor.getCount();// 获取总的短信数
            // 设置短息备份进度框的一些属性
            callBack.before(count);
            // 进度条默认为 0
            int process = 0;

            // 写文件
            try {
                // 把短信备份到sd卡 第二个参数表示名字
                File file = new File(Environment.getExternalStorageDirectory(), "backup.xml");

                FileOutputStream os = new FileOutputStream(file);
                // 得到序列化器
                // 在android系统里面所有有关xml的解析都是pull解析
                XmlSerializer serializer = Xml.newSerializer();
                // 把短信序列化到sd卡然后设置编码格式
                serializer.setOutput(os, "utf-8");
                // standalone表示当前的xml是否是独立文件 ture 表示文件独立
                serializer.startDocument("utf-8", true);
                // 设置开始的节点 第一个参数是命名空间。第二个参数是节点的名字
                serializer.startTag(null, "smss");
                // 设置 smms 节点上的属性值
                serializer.attribute(null, "size", String.valueOf(count));

                // 游标往下面进行移动
                while (cursor.moveToNext()) {
                    Log.d("SmsBackup:", "--------------------------------");
                    Log.i("SmsBackup:", "address = " + cursor.getString(0));
                    Log.i("SmsBackup:", "date = " + cursor.getString(1));
                    Log.i("SmsBackup:", "type = " + cursor.getString(2));
                    Log.i("SmsBackup:", "body = " + cursor.getString(3));

                    serializer.startTag(null, "sms");

                    serializer.startTag(null, "address");
                    serializer.text(cursor.getString(0)); // 设置文本的内容
                    serializer.endTag(null, "address");

                    serializer.startTag(null, "date");
                    serializer.text(cursor.getString(1));
                    serializer.endTag(null, "date");

                    serializer.startTag(null, "type");
                    serializer.text(cursor.getString(2));
                    serializer.endTag(null, "type");

                    serializer.startTag(null, "body");
                    // 第一个参数为 密钥
                    serializer.text(Crypto.encrypt("789", cursor.getString(3)));
                    serializer.endTag(null, "body");

                    process++; // 序列化完成一条短信后，自加 1
                    callBack.onBackUpSms(process);

                    SystemClock.sleep(200); // 减慢备份数据，便于观察进度框
                }

                serializer.endTag(null, "smss");
                serializer.endDocument();

                os.flush();
                os.close();

                cursor.close();

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return false;
    }
}
