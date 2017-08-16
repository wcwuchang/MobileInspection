package com.tianjin.MobileInspection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.entity.Schedule;

import java.util.List;

/**
 * 待办事项列表
 * Created by wuchang on 2016/10/14.
 */
public class ScheduleAdapter extends BaseAdapter{
    private Context mContext;
    private List<Schedule> data;

    public ScheduleAdapter (Context context){
        mContext=context;
    }
    public void updata(List<Schedule> data){
        this.data=data;
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
            holder=new Holder();
            convertView= LayoutInflater.from(mContext).inflate(R.layout.listview_schedule_item,null);
            holder.type=(TextView)convertView.findViewById(R.id.tv_type);
            holder.content=(TextView)convertView.findViewById(R.id.tv_inspection_name);
            convertView.setTag(holder);
        }else{
            holder=(Holder)convertView.getTag();
        }
        holder.type.setText(data.get(position).getType());
        holder.content.setText(data.get(position).getContent());
        return convertView;
    }

    private class Holder{
        TextView type;
        TextView content;
    }
}
