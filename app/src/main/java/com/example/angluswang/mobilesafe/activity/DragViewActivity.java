package com.example.angluswang.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.angluswang.mobilesafe.R;

/**
 * Created by Jeson on 2016/7/30.
 * 修改归属地显示位置
 */

public class DragViewActivity extends Activity {

    private TextView tvTop;
    private TextView tvBottom;
    private ImageView imgDrag;

    private int startX; //起始点坐标
    private int startY;
    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_view);

        mPref = getSharedPreferences("config", MODE_PRIVATE);

        initView();
    }

    private void initView() {

        tvTop = (TextView) findViewById(R.id.tv_top);
        tvBottom = (TextView) findViewById(R.id.tv_bottom);
        imgDrag = (ImageView) findViewById(R.id.img_drag);

        // 获取保存的 view 的坐标
        int lastX = mPref.getInt("lastX", 0);
        int lastY = mPref.getInt("lastY", 100);

        // onMeasure, onLayout, onDraw
        // 以下方法不能使用，因为没有测量完不能布局
//        imgDrag.layout(lastX, lastY, lastX + imgDrag.getWidth(), lastY + imgDrag.getHeight());

        // 获取屏幕宽高
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        final int winWidth = point.x;
        final int winHight = point.y;
        
        if (lastY > winHight / 2) { //上边文本框显示, 下边文本框隐藏
            tvTop.setVisibility(View.VISIBLE);
            tvBottom.setVisibility(View.INVISIBLE);
        } else {
            tvTop.setVisibility(View.INVISIBLE);
            tvBottom.setVisibility(View.VISIBLE);
        }

        RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams) imgDrag.getLayoutParams();//获取布局对象
        params.leftMargin = lastX;  // 设置左边距
        params.topMargin = lastY;   // 设置上边距
        imgDrag.setLayoutParams(params);

        // 设置触摸监听
        imgDrag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 1. 初始化起点坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 2. 计算偏移量
                        int endX = (int) event.getRawX();
                        int endY = (int) event.getRawY();

                        int dx = endX - startX;
                        int dy = endY - startY;

                        // 3. 更新界面 View , left, top, right, bottom(左上右下)
                        int l = imgDrag.getLeft() + dx;
                        int t = imgDrag.getTop() + dy;
                        int r = imgDrag.getRight() + dx;
                        int b = imgDrag.getBottom() + dy;

                        // 判断是否超出屏幕外，注意状态栏
                        if (l < 0 | r > winWidth | t < 0 | b > winHight - 60) {
                            break;
                        }

                        if (t > winHight / 2) { //上边文本框显示, 下边文本框隐藏
                            tvTop.setVisibility(View.VISIBLE);
                            tvBottom.setVisibility(View.INVISIBLE);
                        } else {
                            tvTop.setVisibility(View.INVISIBLE);
                            tvBottom.setVisibility(View.VISIBLE);
                        }
                        imgDrag.layout(l, t, r, b);

                        // 4. 重新初始化起点坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        // 5. 手指抬起后，记录view 的坐标
                        mPref.edit().putInt("lastX", imgDrag.getLeft())
                                .putInt("lastY", imgDrag.getTop()).apply();
                        break;
                }
                return true;
            }
        });
    }
}
