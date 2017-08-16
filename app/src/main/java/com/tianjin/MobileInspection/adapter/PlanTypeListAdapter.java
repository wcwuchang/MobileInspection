package com.tianjin.MobileInspection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.activity.plan.PlanManageActivity;
import com.tianjin.MobileInspection.entity.PlanManage;

import java.util.ArrayList;

/**
 * 计划管理
 * Created by wuchang on 2016-11-21.
 */
public class PlanTypeListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<PlanManage> data;
    private PlanManageActivity activity;

    public PlanTypeListAdapter(Context context, PlanManageActivity activity){
        this.context=context;
        this.activity=activity;
    }

    public void updata(ArrayList<PlanManage> data){
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
            convertView= LayoutInflater.from(context).inflate(R.layout.listview_plan_manage,null);
            holder.plan=(LinearLayout)convertView.findViewById(R.id.linear_plan);
            holder.type=(ImageView)convertView.findViewById(R.id.iv_plan_img);
            holder.name=(TextView)convertView.findViewById(R.id.tv_plan_name);
            convertView.setTag(holder);
        }else {
            holder=(Holder)convertView.getTag();
        }
        long pe=getItemId(position);
        if(pe==0){
            holder.type.setImageResource(R.drawable.plan_list_xunjian);
        }else if(pe==1){
            holder.type.setImageResource(R.drawable.plan_list_zhuanxiang);
        }else if(pe==2){
            holder.type.setImageResource(R.drawable.plan_list_richang);
        }else if(pe==3){
            holder.type.setImageResource(R.drawable.plan_list_caiwu);
        }
        holder.name.setText(data.get(position).getPlanName());
        return convertView;
    }

    private class Holder{
        LinearLayout plan;
        ImageView type;
        TextView name;
    }

}
