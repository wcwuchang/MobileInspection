package com.tianjin.MobileInspection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.activity.actor.MainActivity;
import com.tianjin.MobileInspection.entity.IndexOptionEntity;

import java.util.List;

/**
 * Created by wuchang on 2016-11-16.
 */
public class IndexOptionAdapter extends BaseAdapter {
    private Context context;
    private List<IndexOptionEntity> data;
    private MainActivity activity;

    public IndexOptionAdapter(Context context,List<IndexOptionEntity> data,MainActivity activity){
        this.data=data;
        this.context=context;
        this.activity=activity;
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
            convertView= LayoutInflater.from(context).inflate(R.layout.gridview_index_option_item,null);
            holder.img=(ImageView)convertView.findViewById(R.id.iv_optionImg);
            holder.name=(TextView)convertView.findViewById(R.id.tv_optionName);
            convertView.setTag(holder);
        }else{
            holder=(Holder)convertView.getTag();
        }
        IndexOptionEntity entity=data.get(position);
        holder.img.setImageBitmap(entity.getOptionBmp());
        holder.name.setText(entity.getOptionName());
        convertView.setOnClickListener(new OptionListener(entity.getOptionId()));
        return convertView;
    }

    private class Holder{
        ImageView img;
        TextView name;
    }

    class OptionListener implements View.OnClickListener{

        private int id;
        public OptionListener(int id){
            this.id=id;
        }
        @Override
        public void onClick(View v) {
            activity.optionDo(id);
        }
    }
}
