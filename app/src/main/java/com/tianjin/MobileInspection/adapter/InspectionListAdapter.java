package com.tianjin.MobileInspection.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.socks.library.KLog;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.entity.InspectionChoose;

import java.util.ArrayList;

/**
 * 巡检列表适配器
 * Created by wuchang on 2016/10/13.
 */
public class InspectionListAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<InspectionChoose> data;
    private int select=-1;

    public InspectionListAdapter (Context context){
        mContext=context;
    }
    public void updata(ArrayList<InspectionChoose> data){
        this.data=data;
        notifyDataSetChanged();
    }

    public void setSelect(int select){
        this.select=select;
        notifyDataSetChanged();
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
        Holder holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.listview_inspection_execution,null);
            holder=new Holder();
            holder.name=(TextView)convertView.findViewById(R.id.tv_inspection_name);
            holder.content=(TextView)convertView.findViewById(R.id.tv_inspection_content);
            holder.date=(TextView)convertView.findViewById(R.id.tv_inspection_time);
            convertView.setTag(holder);
        }else{
            holder=(Holder)convertView.getTag();
        }
        if(select==position){
            holder.name.setTextColor(Color.RED);
        }else {
            holder.name.setTextColor(Color.rgb(66,172,251));
        }
        holder.name.setText(data.get(position).getInspectionName());
        holder.content.setText(data.get(position).getContent());
        holder.date.setText(data.get(position).getDate());
        KLog.d(data.get(position).getDate());
        return convertView;
    }

    private class Holder{
        TextView name;
        TextView content;
        TextView date;
    }
}
