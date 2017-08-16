package com.tianjin.MobileInspection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.activity.contract.ContractManageActivity;
import com.tianjin.MobileInspection.entity.ContractManager;

import java.util.List;

/**
 * 合同管理适配器
 * Created by wuchang on 2016/10/14.
 */
public class ContractManagerAdapter extends BaseAdapter{
    private Context mContext;
    private List<ContractManager> data;
    private ContractManageActivity activity;
    public ContractManagerAdapter(Context context,ContractManageActivity activity){
        mContext=context;
        this.activity=activity;
    }

    public void updata(List<ContractManager> data){
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
            convertView= LayoutInflater.from(mContext).inflate(R.layout.listview_contract_manage_item,null);
            holder.name=(TextView)convertView.findViewById(R.id.tv_contract_name);
            holder.unit=(TextView)convertView.findViewById(R.id.tv_contract_unit_name);
            holder.date=(TextView)convertView.findViewById(R.id.tv_contract_begain_end_time);
            holder.linearType=(LinearLayout) convertView.findViewById(R.id.linear_contract);
            convertView.setTag(holder);
        }else{
            holder=(Holder)convertView.getTag();
        }
        ContractManager cm=data.get(position);
        holder.name.setText(cm.getName());
        holder.unit.setText(cm.getUnitName());
        holder.date.setText(cm.getBeginDate()+"至"+cm.getEndDate());
//        int flowtype=cm.getFlowtype();
//        if(flowtype==0){
//            holder.linearType.setBackgroundResource(R.drawable.select_contract_manage_finished);
//        }else if(flowtype==1){
            holder.linearType.setBackgroundResource(R.drawable.select_contract_manage_doing);
//        }else if(flowtype==2){
//            holder.linearType.setBackgroundResource(R.drawable.select_contract_manage_maintanence);
//        }else if(flowtype==3){
//            holder.linearType.setBackgroundResource(R.drawable.select_contract_manage_draft);
//        }
//        holder.linearType.setOnClickListener(new ContractListener(position));
        return convertView;
    }

    private class Holder{
        LinearLayout linearType;
        TextView name;
        TextView unit;
        TextView date;
    }

     class ContractListener implements View.OnClickListener{

         private int id;

         public ContractListener(int id){
             this.id=id;
         }

         @Override
         public void onClick(View v) {
            activity.showDetail(id);
         }
     }
}
