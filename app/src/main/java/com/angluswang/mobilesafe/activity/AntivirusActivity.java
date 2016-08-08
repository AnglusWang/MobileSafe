package com.angluswang.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.angluswang.mobilesafe.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

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
    @ViewInject(R.id.ll_content)
    private LinearLayout llContent;
    @ViewInject(R.id.scrollView)
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        /**
         * 第一个参数表示开始的角度 第二个参数表示结束的角度 第三个参数表示参照自己 初始化旋转动画
         */
        RotateAnimation rotateAnimation =
                new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
        // 设置动画的时间
        rotateAnimation.setDuration(5000);
        // 设置动画无限循环
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        // 开始动画
        ivScanning.startAnimation(rotateAnimation);
    }
}
