package com.tianjin.MobileInspection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.entity.Approve;

import java.util.List;

/**
 * Created by wuchang on 2016-12-12.
 */
public class ApproveListAdapter extends BaseAdapter{

    private Context context;
    private List<Approve> data;

    public ApproveListAdapter(Context context){
        this.context=context;
    }

    public void updata(List<Approve> data){
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
            convertView= LayoutInflater.from(context).inflate(R.layout.listview_approve_item,null);
            holder.date=(TextView)convertView.findViewById(R.id.tv_approve_date);
            holder.content=(TextView)convertView.findViewById(R.id.tv_approve_content);
            holder.timeline=(TextView)convertView.findViewById(R.id.tv_approve_time_line);
            convertView.setTag(holder);
        }else {
            holder=(Holder)convertView.getTag();
        }
        Approve app=data.get(position);
        holder.date.setText(app.getApproveDate());
        holder.content.setText(app.getApproveContent());
        if(getItemId(position)==data.size()-1){
            holder.timeline.setVisibility(View.GONE);
        }
        return convertView;
    }

    private class Holder{
        TextView date;
        TextView content;
        TextView timeline;
    }
}
