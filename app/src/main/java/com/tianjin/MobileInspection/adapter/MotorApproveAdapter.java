package com.tianjin.MobileInspection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.entity.Approve;

import java.util.ArrayList;

/**
 * Created by wuchang on 2017-7-13.
 */
public class MotorApproveAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<Approve> data;

    public MotorApproveAdapter (Context context){
        this.context=context;
    }

    public void updata(ArrayList<Approve> data){
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
            convertView= LayoutInflater.from(context).inflate(R.layout.listview_maintenance_flow_item,null);
            holder.flowcode=(TextView)convertView.findViewById(R.id.tv_main_flow_code);
            holder.man=(TextView)convertView.findViewById(R.id.tv_main_flow_man);
            holder.start=(TextView)convertView.findViewById(R.id.tv_main_flow_begain);
            holder.finish=(TextView)convertView.findViewById(R.id.tv_main_flow_end);
            holder.decision=(TextView)convertView.findViewById(R.id.tv_main_flow_decision);
            convertView.setTag(holder);
        }else {
            holder=(Holder)convertView.getTag();
        }
        Approve approve=data.get(position);
        holder.flowcode.setText(approve.getApproveCode());
        holder.man.setText(approve.getApproveMan());
        holder.start.setText(approve.getApproveStartTime());
        holder.finish.setText(approve.getApproveFinishTime());
        holder.decision.setText(approve.getApproveDecision());
        return convertView;
    }

    class Holder{
        TextView flowcode;
        TextView man;
        TextView start;
        TextView finish;
        TextView decision;
    }
}
