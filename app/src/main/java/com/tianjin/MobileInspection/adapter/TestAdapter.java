package com.tianjin.MobileInspection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;

import java.util.List;

/**
 * Created by zhuangaoran on 2016/9/27.
 */
public class TestAdapter extends BaseAdapter {
    private Context context;
    private List<String> data;

    public TestAdapter(Context context,List<String> data){
        this.context=context;
        this.data=data;
    }
    @Override
    public int getCount() {
        return data==null?0:data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(context).inflate(R.layout.listview_pull_item,null);
        TextView tv=(TextView)convertView.findViewById(R.id.tv_test);
        tv.setText(data.get(position));
        return convertView;
    }
}
