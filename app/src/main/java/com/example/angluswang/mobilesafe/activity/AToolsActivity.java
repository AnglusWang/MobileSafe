package com.example.angluswang.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.angluswang.mobilesafe.R;
import com.example.angluswang.mobilesafe.utils.SmsUtils;

/**
 * 高级工具
 */
public class AToolsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
    }

    /**
     * 电话号码归属地查询
     * @param view
     */
    public void numberAddressQuery(View view) {
        startActivity(new Intent(this, AddressActivity.class));
    }

    /**
     * 短信备份
     * @param view
     */
    public void messagingBackup(View view) {
        boolean result = SmsUtils.backUp(this);
    }
}
