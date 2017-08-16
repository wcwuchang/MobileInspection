package com.tianjin.MobileInspection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.entity.DiseaseAndExpert;

import java.util.List;

/**
 * Created by zhuangaoran on 2017-1-4.
 */
public class DiseaseExpertAdapter extends BaseAdapter {

    private Context context;
    private List<DiseaseAndExpert> data;

    public DiseaseExpertAdapter(Context context){
        this.context=context;
    }

    public void updata(List<DiseaseAndExpert> data){
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
            convertView= LayoutInflater.from(context).inflate(R.layout.listview_new_inspection_chose_item,null);
            holder.chose=(ImageView)convertView.findViewById(R.id.iv_list_chose);
            holder.name=(TextView)convertView.findViewById(R.id.tv_list_chose_name);
            convertView.setTag(holder);
        }else {
            holder=(Holder)convertView.getTag();
        }
        DiseaseAndExpert nic=data.get(position);
        if(nic.isChose()){
            holder.chose.setImageResource(R.drawable.list_add_choice);
        }else {
            holder.chose.setImageResource(R.drawable.list_add_normal);
        }
        holder.name.setText(nic.getDeName());
        return convertView;
    }

    private class Holder{
        TextView name;
        ImageView chose;
    }


}
