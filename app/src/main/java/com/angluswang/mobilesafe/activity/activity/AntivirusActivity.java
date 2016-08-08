package com.angluswang.mobilesafe.activity.activity;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.angluswang.mobilesafe.R;
import com.angluswang.mobilesafe.dao.AntivirusDao;
import com.angluswang.mobilesafe.utils.MD5Utils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

import static com.angluswang.mobilesafe.R.id.ll_content;

/**
 * Created by AnglusWang on 2016/8/8.
 * 病毒查杀 活动界面
 */
@ContentView(R.layout.activity_antivirus)
public class AntivirusActivity extends Activity {

    @ViewInject(R.id.iv_scanning)
    private ImageView ivScanning;
    @ViewInject(R.id.tv_init_virus)
    private TextView tvInitVirus;
    @ViewInject(R.id.progressBar1)
    private ProgressBar pb;
    @ViewInject(ll_content)
    private LinearLayout llContent;
    @ViewInject(R.id.scrollView)
    private ScrollView scrollView;

    protected static final int BEGING = 1;  // 扫描开始
    protected static final int SCANING = 2; // 扫描中
    protected static final int FINISH = 3;  // 扫描结束

    private Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        initView();
        initData();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case BEGING:
                    tvInitVirus.setText("初始化8核杀毒引擎");
                    break;
                case SCANING: // 病毒扫描中
                    TextView child = new TextView(AntivirusActivity.this);
                    ScanInfo scanInfo = (ScanInfo) msg.obj;

                    if (scanInfo.desc) { // 如果为true表示有病毒
                        child.setTextColor(Color.RED);
                        child.setText(scanInfo.appName + "有病毒");
                    } else { // 为false表示没有病毒
                        child.setTextColor(Color.BLACK);

                        child.setText(scanInfo.appName + "扫描安全");
                    }
                    llContent.addView(child, 0); // 添加TextView 到LinearLayout 列表

                    //自动滚动
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            //一直往下面进行滚动
                            scrollView.fullScroll(scrollView.FOCUS_DOWN);
                        }
                    });
                    break;
                case FINISH:
                    ivScanning.clearAnimation(); //扫描完停止动画
                    break;
            }
        }
    };

    private void initData() {

        new Thread() {
            @Override
            public void run() {
                message = Message.obtain();
                message.what = BEGING;

                PackageManager pm = getPackageManager();
                // 获取到所有安装的应用程序
                List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
                // 返回手机上面安装了多少个应用程序
                int size = packageInfos.size();
                pb.setMax(size); // 设置进度条的最大值

                int progress = 0;
                for (PackageInfo info : packageInfos) {
                    // 扫描信息
                    ScanInfo scanInfo = new ScanInfo();

                    // 获取到当前手机上面的app的名字
                    scanInfo.appName = info.applicationInfo.loadLabel(pm).toString();
                    scanInfo.packageName = info.applicationInfo.packageName;

                    // 首先需要获取到每个应用程序的目录
                    String sourceDir = info.applicationInfo.sourceDir;
                    // 获取到文件的 md5 码
                    String md5 = MD5Utils.getFileMd5(sourceDir);
                    // 判断当前的文件是否是病毒数据库里面
                    String desc = AntivirusDao.checkFileVirus(md5);
                    if (desc == null) {
                        scanInfo.desc = false;
                    } else {
                        scanInfo.desc = true;
                    }
                    progress++;
                    pb.setProgress(progress); // 设置进度条进度
                    SystemClock.sleep(1000); //睡眠一秒,降低扫描速度

                    message = Message.obtain();
                    message.what = SCANING;
                    message.obj = scanInfo;
                    handler.sendMessage(message);

                }

                message = Message.obtain();
                message.what = FINISH;
                handler.sendMessage(message);
            }
        }.start();
    }

    private void initView() {
        /**
         * 第一个参数表示开始的角度 第二个参数表示结束的角度 第三个参数表示参照自己 初始化旋转动画
         */
        RotateAnimation rotateAnimation =
                new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
        // 设置动画的时间
        rotateAnimation.setDuration(3000);
        // 设置动画无限循环
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        // 开始动画
        ivScanning.startAnimation(rotateAnimation);
    }

    static class ScanInfo {
        boolean desc;
        String appName;
        String packageName;
    }
}
