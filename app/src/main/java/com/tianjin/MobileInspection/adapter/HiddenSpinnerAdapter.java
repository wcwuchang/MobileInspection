package com.tianjin.MobileInspection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.entity.HiddenSpinner;

import java.util.List;

/**
 * Created by wuchang on 2017-6-5.
 */
public class HiddenSpinnerAdapter extends BaseAdapter{

    private Context context;
    private List<HiddenSpinner> data;

    public HiddenSpinnerAdapter(Context context){
        this.context=context;
    }

    public void updata(List<HiddenSpinner> data){
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
            convertView= LayoutInflater.from(context).inflate(R.layout.layout_hidden_spinner_item,null);
            holder.textView=(TextView)convertView.findViewById(R.id.tv_hidden_spinner_name);
            convertView.setTag(holder);
        }else {
            holder=(Holder)convertView.getTag();
        }
        holder.textView.setText(data.get(position).getName());
        return convertView;
    }

    private class Holder{
        TextView textView;
    }
}
