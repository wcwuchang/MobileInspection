package com.tianjin.MobileInspection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.entity.HiddenType;

import java.util.List;

/**
 * Created by wuchang on 2016-12-16.
 */
public class MaintenanceTroublesAdapter extends BaseAdapter{

    private Context context;
    private List<HiddenType> data;

    public MaintenanceTroublesAdapter(Context context){
        this.context=context;
    }

    public void updata(List<HiddenType> data){
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
            convertView= LayoutInflater.from(context).inflate(R.layout.listview_maintenance_trouble_list_item,null);
            holder.name=(TextView)convertView.findViewById(R.id.tv_yh_detail_name);
            holder.content=(EditText) convertView.findViewById(R.id.edt_yh_detail_size);
            holder.unit=(TextView)convertView.findViewById(R.id.tv_yh_detail_unit);
            convertView.setTag(holder);
        }else{
            holder=(Holder) convertView.getTag();
        }
        HiddenType ht=data.get(position);
        holder.name.setText(ht.getTypeName());
        holder.content.setText(ht.getTroubleContent());
        holder.unit.setVisibility(View.GONE);
        return convertView;
    }

    private class Holder{
        TextView name;
        EditText content;
        TextView unit;
    }
}
