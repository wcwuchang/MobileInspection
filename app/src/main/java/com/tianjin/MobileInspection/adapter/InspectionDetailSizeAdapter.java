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
import com.tianjin.MobileInspection.entity.Road;

import java.util.List;

/**
 * Created by wuchang on 2016-12-7.
 */
public class InspectionDetailSizeAdapter extends BaseAdapter {

    private Context context;
    private List<Road> data;

    public InspectionDetailSizeAdapter(Context context){
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
            convertView= LayoutInflater.from(context).inflate(R.layout.listview_inspection_choose_itemlist_item,null);
            holder.line=(ImageView)convertView.findViewById(R.id.iv_nodes_link);
            holder.road=(TextView)convertView.findViewById(R.id.tv_node_name);
            convertView.setTag(holder);
        }else{
            holder=(Holder)convertView.getTag();
        }
        KLog.d(data.get(position).toString());
        if(getItemId(position)==0){
            holder.line.setVisibility(View.GONE);
        }else {
            holder.line.setVisibility(View.VISIBLE);
        }
        holder.road.setText(data.get(position).getRoadName());
        return convertView;
    }

    private class Holder{
        ImageView line;
        TextView road;
    }
}
