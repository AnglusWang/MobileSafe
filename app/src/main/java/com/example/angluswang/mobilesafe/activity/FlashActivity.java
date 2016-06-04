package com.example.angluswang.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import com.example.angluswang.mobilesafe.R;
import com.example.angluswang.mobilesafe.entity.VersionInfo;
import com.example.angluswang.mobilesafe.utils.StreamUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FlashActivity extends Activity {

    private static final int CODE_UPDATE_DIALOG = 0;
    private static final int CODE_URL_ERROR = 1;
    private static final int CODE_NET_ERROR = 2;

    private TextView mTvVersion;

    private VersionInfo mVersionInfo;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_UPDATE_DIALOG:
                    showUpdateDialog();
                    break;
                case CODE_URL_ERROR:
                    Toast.makeText(FlashActivity.this, "Url异常", Toast.LENGTH_SHORT).show();
                    break;
                case CODE_NET_ERROR:
                    Toast.makeText(FlashActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

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

            String versionName = packageInfo.versionName;

            return versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 获取版本号
     * @return
     */
    private int getVersionCode() {

        PackageManager packageManager = getPackageManager();
        try {
            // 获取包的信息
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);

            int versionCode = packageInfo.versionCode;

            return versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * 从服务器获取版本信息进行校验
     */
    private void checkVersion() {

        // 启动子线程异步加载数据
        new Thread() {

            @Override
            public void run() {

                Message msg = Message.obtain();
                HttpURLConnection conn = null;
                try {
                    //本地主机用localhost，模拟器上加载本机的地址
                    URL url = new URL("http://192.168.1.104:8080/update.json");
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

                        //解析JSON(使用GSON工具解析)
                        Gson gson = new Gson();
                        mVersionInfo = gson.fromJson(result, VersionInfo.class);

                        //判断是否有更新
                        if (mVersionInfo.getVersionCode() > getVersionCode()) {

                            //弹出升级对话框
                            msg.what = CODE_UPDATE_DIALOG;
                        }

                    }

                } catch (MalformedURLException e) {
                    msg.what = CODE_URL_ERROR;
                    e.printStackTrace();
                } catch (IOException e) {
                    msg.what = CODE_NET_ERROR;
                    e.printStackTrace();
                } finally {
                    mHandler.sendMessage(msg);

                    if (conn != null) {
                        conn.disconnect();  //关闭网络连接
                    }
                }
            }
        }.start();
    }

    /**
     * 显示升级对话框
     */
    private void showUpdateDialog() {
        AlertDialog.Builder builder  = new AlertDialog.Builder(this);
        builder.setTitle("最新版本：" + mVersionInfo.getVersionName());
        builder.setMessage(mVersionInfo.getDescription());
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("立即更新");
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("取消");
            }
        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
        builder.show();

    }
}
