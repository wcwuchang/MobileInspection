package com.tianjin.MobileInspection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.entity.HiddenType;

import java.util.List;

/**
 * Created by wuchang on 2016-12-14.
 */
public class ChoseHiddenTypeAdapter extends BaseAdapter{

    private Context context;
    private List<HiddenType> data;

    public ChoseHiddenTypeAdapter(Context context){
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
            convertView= LayoutInflater.from(context).inflate(R.layout.listview_chose_hidden_type_item,null);
            holder.chose=(ImageView)convertView.findViewById(R.id.iv_hidden_type_chose);
            holder.name=(TextView)convertView.findViewById(R.id.tv_hidden_type_name);
            convertView.setTag(holder);
        }else{
            holder=(Holder)convertView.getTag();
        }
        HiddenType type=data.get(position);
        if(type.isChosed()){
            holder.chose.setImageResource(R.drawable.list_add_choice);
        }else{
            holder.chose.setImageResource(R.drawable.list_add_normal);
        }
        holder.name.setText(type.getTypeName());
        return convertView;
    }

   private class Holder{
        ImageView chose;
        TextView name;
    }
}
