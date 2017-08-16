package com.tianjin.MobileInspection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.socks.library.KLog;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.until.FileUtil;

import java.util.ArrayList;

/**
 * Created by 吴昶 on 2016/12/26.
 */
public class FujianAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> data;

    public FujianAdapter(Context context){
        this.context=context;
    }

    public void updata(ArrayList<String> data){
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
            convertView= LayoutInflater.from(context).inflate(R.layout.listview_fujian_list,null);
            holder.name=(TextView)convertView.findViewById(R.id.tv_fujian_name);
            convertView.setTag(holder);
        }else {
            holder=(Holder)convertView.getTag();
        }
        KLog.d(data.get(position));
        holder.name.setText(FileUtil.getFileNameForUrl(data.get(position)));
        return convertView;
    }


    class Holder{
        TextView name;
    }
}
