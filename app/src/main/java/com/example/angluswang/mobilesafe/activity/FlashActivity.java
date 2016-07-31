package com.example.angluswang.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.angluswang.mobilesafe.R;
import com.example.angluswang.mobilesafe.entity.VersionInfo;
import com.example.angluswang.mobilesafe.utils.StreamUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FlashActivity extends Activity {

    private static final int CODE_UPDATE_DIALOG = 0;
    private static final int CODE_URL_ERROR = 1;
    private static final int CODE_NET_ERROR = 2;
    private static final int CODE_ENTER_HOME = 3;

    private TextView mTvVersion, tvProgress;

    private VersionInfo mVersionInfo;

    private SharedPreferences mPref;

    private RelativeLayout rfRoot;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_UPDATE_DIALOG:
                    showUpdateDialog();
                    break;
                case CODE_URL_ERROR:
                    Toast.makeText(FlashActivity.this, "Url异常", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case CODE_NET_ERROR:
                    Toast.makeText(FlashActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case CODE_ENTER_HOME:
                    enterHome();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);

        mTvVersion = (TextView) findViewById(R.id.tv_version);
        mTvVersion.setText("版本号： " + getVersionName());

        tvProgress = (TextView) findViewById(R.id.tv_progress);

        rfRoot = (RelativeLayout) findViewById(R.id.rl_root);

        mPref = getSharedPreferences("config", MODE_PRIVATE);

        copyDB("address.db");// 拷贝归属地查询数据库

        Boolean autoUpdate = mPref.getBoolean("auto_update", true);
        if (autoUpdate) {
            checkVersion();
        } else {
            mHandler.sendEmptyMessageDelayed(CODE_ENTER_HOME, 2000);
        }

        //给闪屏页加一个渐变的动画
        AlphaAnimation anim = new AlphaAnimation(0.3f, 1.0f);
        anim.setDuration(2000);
        rfRoot.startAnimation(anim);
    }

    /**
     * 获取版本号名
     *
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
     *
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

            final long startTime = System.currentTimeMillis();

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
                        } else {
                            msg.what = CODE_ENTER_HOME;
                        }

                    }

                } catch (MalformedURLException e) {
                    msg.what = CODE_URL_ERROR;
                    e.printStackTrace();
                } catch (IOException e) {
                    msg.what = CODE_NET_ERROR;
                    e.printStackTrace();
                } finally {

                    long endTime = System.currentTimeMillis();
                    long timeUsed = endTime - startTime;

                    //强制休眠，保证闪屏界面展示
                    if (timeUsed < 2000) {
                        try {
                            Thread.sleep(2000 - timeUsed);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("最新版本：" + mVersionInfo.getVersionName());
        builder.setMessage(mVersionInfo.getDescription());
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("立即更新");
                downLoad();
            }
        });
        builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("以后再说");
                enterHome();
            }
        });

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                System.out.println("取消");
                enterHome();
            }
        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
        builder.show();

    }

    /**
     * 下载Apk
     */
    private void downLoad() {

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {

            tvProgress.setVisibility(View.VISIBLE);

            String target = Environment.getExternalStorageDirectory() + "/update.apk";

            HttpUtils utils = new HttpUtils();
            utils.download(mVersionInfo.getDownloadUrl(), target, new RequestCallBack<File>() {

                //下载文件的进度
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                    System.out.println("下载进度：" + current + "/" + total);

                    tvProgress.setText("下载进度：" + current * 100 / total + "%");
                }

                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    System.out.println("下载成功");
                    //进入下载页面
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setDataAndType(Uri.fromFile(responseInfo.result),
                            "application/vnd.android.package-archive");
//                    startActivity(intent);    //该方法启动时如果不点击下载，会停留在闪屏页面
                    startActivityForResult(intent, 0);  //该方法会回调onActivityResult()
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    System.out.println("下载失败");
                }
            });
        } else {
            Toast.makeText(FlashActivity.this, "没有检测到SD卡", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        enterHome();
        super.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * 进入主界面
     */
    private void enterHome() {
        Intent intent = new Intent(FlashActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 拷贝数据库
     *
     * @param dbName
     */
    private void copyDB(String dbName) {
//        File filesDir = getFilesDir();
//        System.out.println("路径:" + filesDir.getAbsolutePath());
//         要拷贝的目标地址

//        方式一：
//        String path = this.getApplicationContext().getFilesDir()
//                .getAbsolutePath() + "/" + dbName;   // data/data目录
//        File destFile = new File(path);
//
//        方式二：

        File destFile = new File(getFilesDir(), dbName);
        if (destFile.exists()) {
            Log.i("db:", "数据库 " + dbName + " 已存在!");
        } else {
            Log.i("db:", "拷贝路径为： " + destFile.getAbsolutePath());

            FileOutputStream out = null;
            InputStream in = null;

            try {
                in = getResources().getAssets().open(dbName);
                out = new FileOutputStream(destFile);

                int len = 0;
                byte[] buffer = new byte[1024];

                while ((len = in.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
                out.flush();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }
}
