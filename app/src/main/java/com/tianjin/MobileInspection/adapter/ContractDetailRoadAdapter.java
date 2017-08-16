package com.tianjin.MobileInspection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.entity.Road;

import java.util.List;

/**
 * Created by wuchang on 2016-12-8.
 */
public class ContractDetailRoadAdapter extends BaseAdapter{

    private Context context;
    private List<Road> data;

    public ContractDetailRoadAdapter(Context context){
        this.context=context;
    }

    public void updata(List<Road> data){
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
            convertView= LayoutInflater.from(context).inflate(R.layout.listview_contract_manage_detail_item,null);
            holder.roadname=(TextView)convertView.findViewById(R.id.tv_contract_detail_list_name);
            holder.roadlength=(TextView)convertView.findViewById(R.id.tv_contract_detail_list_length);
            holder.roadsize=(TextView)convertView.findViewById(R.id.tv_contract_detail_list_size);
            convertView.setTag(holder);
        }else{
            holder=(Holder)convertView.getTag();
        }
        Road road=data.get(position);
        holder.roadname.setText(road.getRoadName());
        return convertView;
    }

    private class Holder{
        TextView roadname;
        TextView roadlength;
        TextView roadsize;
    }
}
