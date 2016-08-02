package com.example.angluswang.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.angluswang.mobilesafe.R;
import com.example.angluswang.mobilesafe.adapter.MyBaseAdapter;
import com.example.angluswang.mobilesafe.db.dao.BlackNumberDao;
import com.example.angluswang.mobilesafe.entity.BlackNumberInfo;

import java.util.List;

import static com.example.angluswang.mobilesafe.R.id.et_number;

/**
 * Created by Jeson on 2016/7/31.
 * 通讯卫士
 */
public class CallSafeActivity extends Activity {
    private ListView lvBlackPhone;
    private List<BlackNumberInfo> blackNumInfos;
    private BlackNumberDao dao;
    private CallSafeAdapter adapter;

//    private EditText etPageNumber;
//    private TextView tvPageNumber;

//    private int mCurrentNumber = 0;     // 当前页面

    private int mPageSize = 20;     // 每页展示数据个数
    private int mTotalPage;     // 总的页数

    private int mStartIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_safe);

        initView();
        initData();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            adapter = new CallSafeAdapter(blackNumInfos, CallSafeActivity.this);
            lvBlackPhone.setAdapter(adapter);
        }
    };

    private void initData() {

        new Thread() {
            public void run() {
                dao = new BlackNumberDao(CallSafeActivity.this);
                mTotalPage = dao.getTotalNumber();
                //分批加载数据
                if (blackNumInfos == null) {
                    blackNumInfos = dao.findBatch(mStartIndex, mPageSize);
                } else {
                    //把后面的数据。追加到 blackNumInfos 集合里面。防止黑名单被覆盖
                    blackNumInfos.addAll(dao.findBatch(mStartIndex, mPageSize));
                }

                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    private void initView() {
        lvBlackPhone = (ListView) findViewById(R.id.lv_black_number);
        // 展示加载进度条
        LinearLayout ll_pd = (LinearLayout) findViewById(R.id.ll_pd);
        ll_pd.setVisibility(View.INVISIBLE);

        //设置listView的滚动监听
        lvBlackPhone.setOnScrollListener(new AbsListView.OnScrollListener() {
            /**
             * 状态改变时 回调的方法
             * @param view
             * @param scrollState  表示滚动的状态
             *
             * AbsListView.OnScrollListener.SCROLL_STATE_IDLE 闲置状态
             * AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL 手指触摸的时候的状态
             * AbsListView.OnScrollListener.SCROLL_STATE_FLING 惯性
             */
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        //获取到最后一条显示的数据
                        int lastVisiblePosition = lvBlackPhone.getLastVisiblePosition();
//                        System.out.println("lastVisiblePosition==========" + lastVisiblePosition);
                        if (lastVisiblePosition == blackNumInfos.size() - 1) {
                            // 加载更多的数据。 更改加载数据的开始位置
                            mStartIndex += mPageSize;
                            if (mStartIndex >= mTotalPage) {
                                Toast.makeText(getApplicationContext(), "没有更多的数据了!",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            initData();
                        }
                        break;
                    default:
                        break;
                }

            }

            //listView 滚动的时候调用的方法, 时时调用
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });
    }

    /**
     * 上一页 按钮点击事件
     *
     * @param view
     */
//    public void prePage(View view) {
//        if (mCurrentNumber <= 0) {
//            Toast.makeText(this, "已经是第一页了",
//                    Toast.LENGTH_SHORT).show();
//            return;
//        }
//        mCurrentNumber--;
//        initData();
//    }

    /**
     * 下一页 按钮点击事件
     *
     * @param view
     */
//    public void nextPage(View view) {
//        //判断当前的页码不能大于总的页数
//        if (mCurrentNumber >= (mTotalPage - 1)) {
//            Toast.makeText(this, "已经是最后一页了",
//                    Toast.LENGTH_SHORT).show();
//            return;
//        }
//        mCurrentNumber++;
//        initData();
//    }

    /**
     * 跳转 按钮点击事件
     *
     * @param view
     */
//    public void jump(View view) {
//        String strPageNumber = etPageNumber.getText().toString().trim();
//        if (TextUtils.isEmpty(strPageNumber)) {
//            Toast.makeText(this, "请输入正确的页码",
//                    Toast.LENGTH_SHORT).show();
//        } else {
//            int number = Integer.parseInt(strPageNumber);
//            if (number >= 0 && number <= (mTotalPage - 1)) {
//                mCurrentNumber = number;
//                initData();
//            } else {
//                Toast.makeText(this, "请输入正确的页码",
//                        Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    /**
     * 添加黑名单 按钮点击事件
     *
     * @param view
     */
    public void addBlackNumber(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();

        View dialogView = View.inflate(this, R.layout.dialog_add_black_number, null);

        final EditText etNumber = (EditText) dialogView.findViewById(et_number);
        Button btnOk = (Button) dialogView.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        final CheckBox cbPhone = (CheckBox) dialogView.findViewById(R.id.cb_phone);
        final CheckBox cbSms = (CheckBox) dialogView.findViewById(R.id.cb_sms);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strNumber = etNumber.getText().toString().trim();
                if (TextUtils.isEmpty(strNumber)) {
                    Toast.makeText(CallSafeActivity.this, "请输入黑名单号码",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                String mode = "";

                if (cbPhone.isChecked() && cbSms.isChecked()) {
                    mode = "1";
                } else if (cbPhone.isChecked()) {
                    mode = "2";
                } else if (cbSms.isChecked()) {
                    mode = "3";
                } else {
                    Toast.makeText(CallSafeActivity.this, "请勾选拦截模式",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
                blackNumberInfo.setNumber(strNumber);
                blackNumberInfo.setMode(mode);
                blackNumInfos.add(0, blackNumberInfo);
                //把电话号码和拦截模式添加到数据库。
                dao.add(strNumber, mode);

                if (adapter == null) {
                    adapter = new CallSafeAdapter(blackNumInfos, CallSafeActivity.this);
                    lvBlackPhone.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }

                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setView(dialogView);
        dialog.show();
    }

    /**
     * list_view 适配器
     */
    private class CallSafeAdapter extends MyBaseAdapter<BlackNumberInfo> {
        private CallSafeAdapter(List<BlackNumberInfo> lists, Context mContext) {
            super(lists, mContext);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(CallSafeActivity.this,
                        R.layout.item_call_safe, null);
                holder = new ViewHolder();
                holder.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
                holder.tv_mode = (TextView) convertView.findViewById(R.id.tv_mode);
                holder.iv_delete = (ImageView) convertView.findViewById(R.id.img_delete);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_number.setText(lists.get(position).getNumber());
            String mode = lists.get(position).getMode();
            switch (mode) {
                case "1": // 来电+短信 拦截
                    holder.tv_mode.setText("来电拦截+短信");
                    break;
                case "2": // 来电拦截
                    holder.tv_mode.setText("电话拦截");
                    break;
                case "3": // 短信拦截
                    holder.tv_mode.setText("短信拦截");
                    break;
                default:
                    break;
            }
            final BlackNumberInfo info = lists.get(position);
            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String number = info.getNumber();
                    boolean result = dao.delete(number);
                    if (result) {
                        Toast.makeText(CallSafeActivity.this, "删除成功",
                                Toast.LENGTH_SHORT).show();
                        lists.remove(info);
                        //刷新界面
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(CallSafeActivity.this, "删除失败",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return convertView;
        }

    }

    private static class ViewHolder {
        TextView tv_number;
        TextView tv_mode;
        ImageView iv_delete;
    }
}
