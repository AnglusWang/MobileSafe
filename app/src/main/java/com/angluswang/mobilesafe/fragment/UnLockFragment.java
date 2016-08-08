package com.angluswang.mobilesafe.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.angluswang.mobilesafe.R;
import com.angluswang.mobilesafe.engine.AppInfos;
import com.angluswang.mobilesafe.entity.AppInfo;

import java.util.List;

/**
 * Created by AnglusWang on 2016/8/8.
 * 为加锁的活动界面
 */

public class UnLockFragment extends Fragment {

    private View view;
    private ListView lvApps;
    private TextView tvUnlock;
    private List<AppInfo> appInfos;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.item_unlock_fragment, null);
        lvApps = (ListView) view.findViewById(R.id.lv_apps);
        tvUnlock = (TextView) view.findViewById(R.id.tv_unlock);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        appInfos = AppInfos.getAppInfos(getActivity());
        UnClockAdapter adapter = new UnClockAdapter();
        lvApps.setAdapter(adapter);
    }

    private class UnClockAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return appInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return appInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position,
                            View convertView, ViewGroup parent) {
            ViewHolder holder;
            View view;
            if (convertView == null) {
                holder = new ViewHolder();
                view = View.inflate(getActivity(), R.layout.item_unlock, null);

                holder.imgIcon = (ImageView) view.findViewById(R.id.img_icon);
                holder.tvName = (TextView) view.findViewById(R.id.tv_name);
                holder.imgUnlock = (ImageView) view.findViewById(R.id.img_unlock);

                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }
            holder.imgIcon.setImageDrawable(appInfos.get(position).getIcon());
            holder.tvName.setText(appInfos.get(position).getApkName());

            return view;
        }
    }

    static class ViewHolder {
        ImageView imgIcon;
        TextView tvName;
        ImageView imgUnlock;
    }
}
