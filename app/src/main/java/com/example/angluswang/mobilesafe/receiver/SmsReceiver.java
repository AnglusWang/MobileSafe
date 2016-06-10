package com.example.angluswang.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;

import com.example.angluswang.mobilesafe.R;

/**
 * Created by Jeson on 2016/6/10.
 * 拦截短信
 */

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Object[] objects = (Object[]) intent.getExtras().get("pdus");

        for (Object object : objects) {
            // 短信最多140字节,
            // 超出的话,会分为多条短信发送,所以是一个数组,因为我们的短信指令很短,所以for循环只执行一次
            SmsMessage message = SmsMessage.createFromPdu((byte[]) object);
            String originatingAddress = message.getOriginatingAddress();  // 短信来源号码
            String messageBody = message.getMessageBody();  // 短信内容

            System.out.println(originatingAddress + ":" + messageBody);

            if ("#*alarm*#".equals(messageBody)) {
                // 播放报警音乐, 即使手机调为静音,也能播放音乐, 因为使用的是媒体声音的通道,和铃声无关
                MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
                player.setVolume(1f, 1f);
                player.setLooping(true);
                player.start();

                abortBroadcast();// 中断短信的传递, 从而系统短信app就收不到内容了
            }
        }
    }
}
