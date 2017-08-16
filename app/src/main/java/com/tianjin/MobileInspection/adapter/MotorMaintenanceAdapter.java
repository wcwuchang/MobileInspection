package com.tianjin.MobileInspection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.entity.HiddenTroubleDetail;
import com.tianjin.MobileInspection.entity.Task;

import java.util.ArrayList;

/**
 * Created by wuchang on 2017-7-24.
 */
public class MotorMaintenanceAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<Task> data;

    public MotorMaintenanceAdapter (Context context){
        this.context=context;
    }

    public void updata(ArrayList<Task> data){
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
            convertView= LayoutInflater.from(context).inflate(R.layout.listview_motor_maintenance_item,null);
            holder.mtvName=(TextView)convertView.findViewById(R.id.tv_motor_maintenance_name);
            holder.mtvTime=(TextView)convertView.findViewById(R.id.tv_motor_maintenance_time);
            holder.mtvHidden=(TextView)convertView.findViewById(R.id.tv_motor_maintenance_hidden);
            convertView.setTag(holder);
        }else {
            holder=(Holder)convertView.getTag();
        }
        Task task=data.get(position);
        HiddenTroubleDetail hd=task.getHiddenTroubleDetail();
        holder.mtvName.setText(task.getTaskContent());
        holder.mtvTime.setText(task.getDate());
        holder.mtvHidden.setText(hd.getStockName());
        return convertView;
    }

    private class Holder{
        TextView mtvName;
        TextView mtvTime;
        TextView mtvHidden;
    }
}
