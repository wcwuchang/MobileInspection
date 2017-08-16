package com.tianjin.MobileInspection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.entity.StandingBook;

import java.util.List;

/**
 * Created by wuchang on 2016-12-15.
 */
public class StandingBookGridviewAdapter extends BaseAdapter{

    private Context context;
    private List<StandingBook> data;

    public StandingBookGridviewAdapter (Context context){
        this.context=context;
    }

    public void updata(List<StandingBook> data){
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
            convertView= LayoutInflater.from(context).inflate(R.layout.gridview_standing_book_item,null);
            holder.image=(ImageView)convertView.findViewById(R.id.iv_book_image);
            holder.name=(TextView) convertView.findViewById(R.id.tv_book_name);
            convertView.setTag(holder);
        }else {
            holder=(Holder)convertView.getTag();
        }
        StandingBook sb=data.get(position);
        holder.image.setImageBitmap(sb.getBookBmp());
        holder.name.setText(sb.getType());
        return convertView;
    }

    private class Holder{
        ImageView image;
        TextView name;
    }

}
