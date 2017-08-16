package com.tianjin.MobileInspection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.socks.library.KLog;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.entity.Task;

import java.util.ArrayList;

/**
 * Created by wuchang on 2017-7-24.
 */
public class PlanMaintenanceAdapter extends BaseAdapter{

    private ArrayList<Task> data;
    private Context context;

    public PlanMaintenanceAdapter (Context context){
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
            convertView= LayoutInflater.from(context).inflate(R.layout.listview_plan_maintenance_item,null);
            holder.mtvName=(TextView)convertView.findViewById(R.id.tv_plan_maintenance_name);
            holder.mtvContract=(TextView)convertView.findViewById(R.id.tv_plan_maintenance_contract);
            holder.mtvMan=(TextView)convertView.findViewById(R.id.tv_plan_maintenance_man);
            convertView.setTag(holder);
        }else {
            holder=(Holder)convertView.getTag();
        }
        Task task=data.get(position);
        KLog.d(task.toString());
        holder.mtvName.setText(task.getTaskContent());
        holder.mtvContract.setText(task.getContractName());
        holder.mtvMan.setText(task.getContactName());
        return convertView;
    }


    private class Holder{
        TextView mtvName;
        TextView mtvContract;
        TextView mtvMan;
    }
}
