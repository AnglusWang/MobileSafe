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

    private EditText etPageNumber;
    private TextView tvPageNumber;

    private int mCurrentNumber = 0;     // 当前页面
    private int mPageSize = 20;     // 每页展示数据个数
    private int mTotalPage;     // 总的页数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_safe);

        initView();
        initData();

//        //初始化黑名单数据
//        BlackNumberDao dao = new BlackNumberDao(this);
//        Random random = new Random();
//        for (int i = 0; i < 200; i++) {
//            Long num = 13173849000L + i;
//            dao.add(num + "", String.valueOf(random.nextInt(3) + 1));
//        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            adapter = new CallSafeAdapter(blackNumInfos, CallSafeActivity.this);
            lvBlackPhone.setAdapter(adapter);
            tvPageNumber.setText(mCurrentNumber + "/" + mTotalPage);
        }
    };

    private void initData() {

        new Thread() {
            public void run() {
                dao = new BlackNumberDao(CallSafeActivity.this);
                // 设置页数View
                mTotalPage = dao.getTotalNumber() / mPageSize;

//                blackNumInfos = dao.findAllNumber();
                blackNumInfos = dao.findPage(mCurrentNumber, mPageSize);
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    private void initView() {
        lvBlackPhone = (ListView) findViewById(R.id.lv_black_number);
        // 展示加载进度条
        LinearLayout ll_pd = (LinearLayout) findViewById(R.id.ll_pd);
        ll_pd.setVisibility(View.INVISIBLE);

        etPageNumber = (EditText) findViewById(R.id.et_page_number);
        tvPageNumber = (TextView) findViewById(R.id.tv_page_number);

    }

    /**
     * 上一页 按钮点击事件
     *
     * @param view
     */
    public void prePage(View view) {
        if (mCurrentNumber <= 0) {
            Toast.makeText(this, "已经是第一页了",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        mCurrentNumber--;
        initData();
    }

    /**
     * 下一页 按钮点击事件
     *
     * @param view
     */
    public void nextPage(View view) {
        //判断当前的页码不能大于总的页数
        if (mCurrentNumber >= (mTotalPage - 1)) {
            Toast.makeText(this, "已经是最后一页了",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        mCurrentNumber++;
        initData();
    }

    /**
     * 跳转 按钮点击事件
     *
     * @param view
     */
    public void jump(View view) {
        String strPageNumber = etPageNumber.getText().toString().trim();
        if (TextUtils.isEmpty(strPageNumber)) {
            Toast.makeText(this, "请输入正确的页码",
                    Toast.LENGTH_SHORT).show();
        } else {
            int number = Integer.parseInt(strPageNumber);
            if (number >= 0 && number <= (mTotalPage - 1)) {
                mCurrentNumber = number;
                initData();
            } else {
                Toast.makeText(this, "请输入正确的页码",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

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
        private CallSafeAdapter(List lists, Context mContext) {
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
