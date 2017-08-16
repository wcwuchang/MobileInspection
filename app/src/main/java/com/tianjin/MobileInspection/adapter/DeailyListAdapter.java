package com.tianjin.MobileInspection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.entity.Task;

import java.util.List;

/**
 * 巡检列表适配器
 * Created by wuchang on 2016/10/13.
 */
public class DeailyListAdapter extends BaseAdapter{

    private Context mContext;
    private List<Task> data;

    public DeailyListAdapter(Context context){
        mContext=context;
    }
    public void updata(List<Task> data){
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
            convertView= LayoutInflater.from(mContext).inflate(R.layout.listview_deaily_naintanence_item,null);
            holder=new Holder();
            holder.name=(TextView)convertView.findViewById(R.id.tv_inspection_name);
            holder.date=(TextView)convertView.findViewById(R.id.tv_deaily_date);
            convertView.setTag(holder);
        }else{
            holder=(Holder)convertView.getTag();
        }
        holder.date.setText(data.get(position).getDate());
        if(getItemId(position)>0&&data.get(position).getDate().equals(data.get((int)getItemId(position)-1).getDate())){
            holder.date.setVisibility(View.GONE);
        }else {
            holder.date.setVisibility(View.VISIBLE);
        }
        holder.name.setText(data.get(position).getTaskContent());
        return convertView;
    }

    private class Holder{
        TextView name;
        TextView date;
    }
}
