package com.example.angluswang.mobilesafe.activity;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.example.angluswang.mobilesafe.R;
import com.example.angluswang.mobilesafe.utils.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FlashActivity extends Activity {

    private TextView mTvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvVersion = (TextView) findViewById(R.id.tv_version);
        mTvVersion.setText("版本号： " + getVersionName());

        checkVersion();
    }

    /**
     * 获取版本号名
     * @return
     */
    private String getVersionName() {

        PackageManager packageManager = getPackageManager();
        try {
            // 获取包的信息
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);

            int versionCode = packageInfo.versionCode;
            String versionName = packageInfo.versionName;

            System.out.println("versionName = " + versionName + "; versionCode = "
                    + versionCode);

            return versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
    /**
     * 从服务器获取版本信息进行校验
     */
    private void checkVersion() {

        // 启动子线程异步加载数据
        new Thread() {

            @Override
            public void run() {
                URL url = null;
                HttpURLConnection conn = null;
                try {
                    //本地主机用localhost，模拟器上加载本机的地址
                    url = new URL("http://192.168.1.104:8080/update.json");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    conn.connect();

                    int responseCode = conn.getResponseCode();  //获取响应码
                    if (responseCode == 200) {
                        InputStream in = conn.getInputStream();
                        String result = StreamUtils.ReadFromStream(in);

                        System.out.println("网络请求：" + result);

                    }

                } catch (MalformedURLException e) {

                    e.printStackTrace();
                }
                catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }.start();
    }
}
