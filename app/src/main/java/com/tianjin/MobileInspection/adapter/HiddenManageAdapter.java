package com.tianjin.MobileInspection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.entity.HiddenTroubleDetail;

import java.util.ArrayList;

/**
 * Created by wuchang on 2017-7-21.
 */
public class HiddenManageAdapter extends BaseAdapter{

    private ArrayList<HiddenTroubleDetail> data;
    private Context context;

    public HiddenManageAdapter(Context context){
        this.context=context;
    }

    public void updata(ArrayList<HiddenTroubleDetail> data){
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
            convertView= LayoutInflater.from(context).inflate(R.layout.listview_hidden_managelist_item,null);
            holder.mtvName=(TextView)convertView.findViewById(R.id.tv_hidden_manage_name);
            holder.mtvNum=(TextView)convertView.findViewById(R.id.tv_hidden_manage_num);
            holder.mtvUnit=(TextView)convertView.findViewById(R.id.tv_hidden_manage_unit);
            holder.mtvLocal=(TextView)convertView.findViewById(R.id.tv_hidden_manage_local);
            holder.mtvTime=(TextView)convertView.findViewById(R.id.tv_hidden_manage_up_time);
            holder.mivState=(ImageView)convertView.findViewById(R.id.iv_hidden_manage_state);
            convertView.setTag(holder);
        }else {
            holder=(Holder) convertView.getTag();
        }
        HiddenTroubleDetail ht=data.get(position);
        holder.mtvName.setText(ht.getStockName());
        holder.mtvNum.setText(ht.getQuantity());
        holder.mtvUnit.setText(ht.getUnit());
        holder.mtvLocal.setText(ht.getRoadName()+ht.getLightId()+"号路灯处");
        holder.mtvTime.setText(ht.getDate());
        if(ht.getState().equals("1")){
            holder.mivState.setImageResource(R.mipmap.h_youxiao);
        }else if(ht.getState().equals("2")){
            holder.mivState.setImageResource(R.mipmap.h_wait);
        }else if(ht.getState().equals("3")){
            holder.mivState.setImageResource(R.mipmap.h_yanhuan);
        }else if(ht.getState().equals("4")){
            holder.mivState.setImageResource(R.mipmap.h_not);
        }else if(ht.getState().equals("5")){
            holder.mivState.setImageResource(R.mipmap.h_finish);
        }
        return convertView;
    }

    private class Holder{
        TextView mtvName;
        TextView mtvNum;
        TextView mtvUnit;
        TextView mtvLocal;
        TextView mtvTime;
        ImageView mivState;
    }
}
