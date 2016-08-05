package com.example.angluswang.mobilesafe.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.angluswang.mobilesafe.R;
import com.example.angluswang.mobilesafe.utils.SmsUtils;
import com.example.angluswang.mobilesafe.utils.UIUtils;

/**
 * 高级工具
 */
public class AToolsActivity extends Activity {

    private ProgressDialog pd; // 短信备份进度框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
    }

    /**
     * 电话号码归属地查询
     *
     * @param view
     */
    public void numberAddressQuery(View view) {
        startActivity(new Intent(this, AddressActivity.class));
    }

    /**
     * 短信备份
     *
     * @param view
     */
    public void messagingBackup(View view) {

        // 初始化一个进度条的对话框
        pd = new ProgressDialog(this);
        pd.setTitle("提示");
        pd.setMessage("正在执行操作，请等待。。。");
        pd.show();

        new Thread() {
            @Override
            public void run() {
                boolean result = SmsUtils.backUp(AToolsActivity.this, pd);
                if (result) {
//                    Looper.prepare(); // 取消息
//                    Toast.makeText(AToolsActivity.this, "短信备份成功",
//                            Toast.LENGTH_SHORT).show();
//                    Looper.loop();
                    UIUtils.showToast(AToolsActivity.this, "短信备份成功");
                } else {
                    UIUtils.showToast(AToolsActivity.this, "短信备份失败");
                }
                pd.dismiss();
            }
        };
    }
}
