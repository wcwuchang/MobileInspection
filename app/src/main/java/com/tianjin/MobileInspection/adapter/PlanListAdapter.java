package com.tianjin.MobileInspection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.entity.PlanManage;

import java.util.List;

/**
 * Created by zhuangaoran on 2016-12-30.
 */
public class PlanListAdapter extends BaseAdapter {

    private Context context;
    private List<PlanManage> data;

    public PlanListAdapter(Context context){
        this.context=context;
    }

    public void updata(List<PlanManage> data){
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
            convertView= LayoutInflater.from(context).inflate(R.layout.listview_plan_manage_item,null);
            holder.name=(TextView)convertView.findViewById(R.id.tv_evaluate_manage_name);
            holder.create=(TextView)convertView.findViewById(R.id.tv_evaluate_manage_unit);
            holder.date=(TextView)convertView.findViewById(R.id.tv_evaluate_manage_date);
            convertView.setTag(holder);
        }else{
            holder=(Holder)convertView.getTag();
        }
        PlanManage em=data.get(position);
        holder.name.setText(em.getPlanName());
        holder.create.setText(em.getPlanStartDate());
        holder.date.setText(em.getPlanEndDate());
        return convertView;
    }

    private class Holder{
        TextView name;
        TextView create;
        TextView date;

    }




}
