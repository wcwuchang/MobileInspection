package com.tianjin.MobileInspection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.socks.library.KLog;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.entity.HiddenTroubleDetail;
import com.tianjin.MobileInspection.entity.Task;

import java.util.ArrayList;

/**
 * Created by wuchang on 2017-7-12.
 */
public class MaintenanceListAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<Task> data;

    public MaintenanceListAdapter(Context context){
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
            convertView= LayoutInflater.from(context).inflate(R.layout.listview_maintenance_item,null);
            holder.mainName=(TextView)convertView.findViewById(R.id.tv_maintenance_name);
            holder.mainNum=(TextView)convertView.findViewById(R.id.tv_maintenance_num);
            holder.mainUnit=(TextView)convertView.findViewById(R.id.tv_unit);
            holder.finishTime=(TextView)convertView.findViewById(R.id.tv_finish_time);
            holder.taskDetail=(LinearLayout)convertView.findViewById(R.id.linear_main_task_detail);
            holder.planUpload=(LinearLayout)convertView.findViewById(R.id.linear_main_plan_upload);
            holder.planDetail=(LinearLayout)convertView.findViewById(R.id.linear_main_plan_detail);
            holder.tainUpload=(LinearLayout)convertView.findViewById(R.id.linear_main_tain_upload);
            holder.tainDetail=(LinearLayout)convertView.findViewById(R.id.linear_main_tain_detail);
            holder.back=(LinearLayout)convertView.findViewById(R.id.linear_main_tain_back);
            convertView.setTag(holder);
        }else {
            holder=(Holder)convertView.getTag();
        }
        Task task=data.get(position);
        HiddenTroubleDetail htd=task.getHiddenTroubleDetail();
        KLog.d(htd.toString());
        holder.mainName.setText(htd.getStockName());
        holder.finishTime.setText(task.getDate());
        holder.mainNum.setText(htd.getQuantity());
        holder.mainUnit.setText(htd.getUnit());
        return convertView;
    }

    class Holder{
        TextView mainName;
        TextView mainNum;
        TextView mainUnit;
        TextView finishTime;
        LinearLayout taskDetail;
        LinearLayout planUpload;
        LinearLayout planDetail;
        LinearLayout tainUpload;
        LinearLayout tainDetail;
        LinearLayout back;
    }
}
