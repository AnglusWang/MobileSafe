package com.example.angluswang.mobilesafe.activity;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.example.angluswang.mobilesafe.R;

public class FlashActivity extends Activity {

    private TextView mTvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvVersion = (TextView) findViewById(R.id.tv_version);
        mTvVersion.setText("版本号： " + getVersionName());
    }

    private String getVersionName() {

        PackageManager pm = getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);

            String versionName = pi.versionName;

            return versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
