package com.example.angluswang.mobilesafe.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.angluswang.mobilesafe.R;
import com.example.angluswang.mobilesafe.adapter.MyBaseAdapter;
import com.example.angluswang.mobilesafe.db.dao.BlackNumberDao;
import com.example.angluswang.mobilesafe.entity.BlackNumberInfo;

import java.util.List;

/**
 * Created by Jeson on 2016/7/31.
 * 通讯卫士
 */
public class CallSafeActivity extends Activity {
    private ListView lvBlackPhone;
    private List<BlackNumberInfo> blackNumInfos;
    private BlackNumberDao dao;
    private CallSafeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_safe);

        initView();
        initData();
    }

    private void initData() {
        dao = new BlackNumberDao(CallSafeActivity.this);
//        // 初始化黑名单数据
//        Random random = new Random();
//        for (int i = 0; i < 100; i++) {
//            Long num = 13173849000L + i;
//            dao.add(num + "", String.valueOf(random.nextInt(3) + 1));
//        }

        blackNumInfos = dao.findAllNumber();
        adapter = new CallSafeAdapter(blackNumInfos, this);
        lvBlackPhone.setAdapter(adapter);
    }

    private void initView() {
        lvBlackPhone = (ListView) findViewById(R.id.lv_black_number);
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
