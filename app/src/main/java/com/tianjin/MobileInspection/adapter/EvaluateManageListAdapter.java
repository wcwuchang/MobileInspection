package com.tianjin.MobileInspection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.entity.EvaluateManager;

import java.util.List;

/**
 * Created by wuchang on 2016-12-15.
 */
public class EvaluateManageListAdapter extends BaseAdapter{

    private Context context;
    private List<EvaluateManager> data;

    public EvaluateManageListAdapter (Context context){
        this.context=context;
    }

    public void updata(List<EvaluateManager> data){
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
            convertView= LayoutInflater.from(context).inflate(R.layout.listview_evaluate_manage_item,null);
            holder.name=(TextView)convertView.findViewById(R.id.tv_evaluate_manage_name);
            holder.unit=(TextView)convertView.findViewById(R.id.tv_evaluate_manage_unit);
            holder.date=(TextView)convertView.findViewById(R.id.tv_evaluate_manage_date);
            convertView.setTag(holder);
        }else{
            holder=(Holder)convertView.getTag();
        }
        EvaluateManager em=data.get(position);
        holder.name.setText(em.getEvaluateName());
        holder.unit.setText(em.getEvaluateUnit());
        holder.date.setText(em.getEvaluateDate());
        return convertView;
    }

    private class Holder{
        TextView name;
        TextView unit;
        TextView date;

    }
}
