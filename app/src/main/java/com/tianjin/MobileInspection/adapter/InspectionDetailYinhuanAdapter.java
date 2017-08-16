package com.tianjin.MobileInspection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.socks.library.KLog;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.entity.Yinhuan;

import java.util.List;

/**
 * Created by wuchang on 2016-12-12.
 */
public class InspectionDetailYinhuanAdapter extends BaseAdapter{

    private Context context;
    private List<Yinhuan> data;

    public InspectionDetailYinhuanAdapter(Context context){
        this.context=context;
    }

    public void updata(List<Yinhuan> data){
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
            convertView= LayoutInflater.from(context).inflate(R.layout.listview_inspection_list_item,null);
            holder=new Holder();
            holder.name=(TextView)convertView.findViewById(R.id.tv_inspection_name);
            holder.content=(TextView)convertView.findViewById(R.id.tv_inspection_content);
            holder.person=(TextView)convertView.findViewById(R.id.tv_inspection_worker_name);
            holder.date=(TextView)convertView.findViewById(R.id.tv_inspection_time);
            holder.head=(ImageView)convertView.findViewById(R.id.iv_inspection_head);
            convertView.setTag(holder);
        }else{
            holder=(Holder)convertView.getTag();
        }
        Yinhuan yh=data.get(position);
        KLog.d(yh.toString());
        holder.name.setText(yh.getYhContent());
        holder.content.setVisibility(View.GONE);
        holder.person.setText(yh.getPersonName());
        holder.date.setText(yh.getYhdate());
        holder.head.setVisibility(View.GONE);
        return convertView;
    }

    private class Holder{
        ImageView head;
        TextView name;
        TextView content;
        TextView person;
        TextView date;
    }
}
