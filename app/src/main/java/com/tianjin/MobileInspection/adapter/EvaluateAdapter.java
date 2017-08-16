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
 * Created by 吴昶 on 2016/12/29.
 */
public class EvaluateAdapter extends BaseAdapter {

    private Context context;
    private List<EvaluateManager> data;

    public EvaluateAdapter(Context context){
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
            convertView= LayoutInflater.from(context).inflate(R.layout.listview_evaluate_list_item,null);
            holder.name=(TextView)convertView.findViewById(R.id.tv_evaluate_itemname);
            holder.grade=(TextView)convertView.findViewById(R.id.tv_evaluate_itemgrade);
            convertView.setTag(holder);
        }else {
            holder=(Holder)convertView.getTag();
        }
        holder.name.setText(data.get(position).getItemName());
        holder.grade.setText(data.get(position).getItemGrade());
        return convertView;
    }

    private class Holder{
        TextView name;
        TextView grade;
    }
}
