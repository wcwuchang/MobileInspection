package com.tianjin.MobileInspection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.entity.ContractData;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by wuchang on 2017-4-5.
 */
public class ContractDataAdapter extends BaseAdapter{


    private ArrayList<ContractData> data;
    private Context context;
    private DecimalFormat df=new DecimalFormat("0");
    private boolean showPrice;

    public ContractDataAdapter(Context context){
        this.context=context;
    }

    public void updata(ArrayList<ContractData> data,boolean showPrice){
        this.data=data;
        this.showPrice=showPrice;
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
    public View getView(int position, View view, ViewGroup parent) {
        Holder holder=null;
        if(view==null){
            holder=new Holder();
            view= LayoutInflater.from(context).inflate(R.layout.listview_contract_item,null);
            holder.name=(TextView)view.findViewById(R.id.tv_contract_1);
            holder.contractNum=(TextView)view.findViewById(R.id.tv_contract_2);
            holder.hasdoneNum=(TextView)view.findViewById(R.id.tv_contract_3);
            holder.rest=(TextView)view.findViewById(R.id.tv_contract_4);
            holder.unit=(TextView)view.findViewById(R.id.tv_contract_5);
            holder.price=(TextView)view.findViewById(R.id.tv_contract_6);
            holder.budget=(TextView)view.findViewById(R.id.tv_contract_7);
            holder.spend=(TextView)view.findViewById(R.id.tv_contract_8);
            holder.spendUnit=(TextView)view.findViewById(R.id.tv_contract_9);

            holder.linear6=(LinearLayout)view.findViewById(R.id.linear_6);
            holder.linear7=(LinearLayout)view.findViewById(R.id.linear_7);
            holder.linear8=(LinearLayout)view.findViewById(R.id.linear_8);
            holder.linear9=(LinearLayout)view.findViewById(R.id.linear_9);

            view.setTag(holder);
        }else {
            holder=(Holder)view.getTag();
        }
        ContractData contractData=data.get(position);
        if(showPrice && getItemId(position)==data.size()-1){
            holder.contractNum.setText("/");
            holder.hasdoneNum.setText("/");
            holder.rest.setText("/");
            holder.unit.setText("/");
            holder.price.setText("/");
        }else {
            holder.contractNum.setText(df.format(contractData.getContractNum()));
            holder.hasdoneNum.setText(df.format(contractData.getHasdoneNum()));
            holder.rest.setText(df.format(contractData.getContractNum()-contractData.getHasdoneNum()));
            holder.unit.setText(contractData.getUnit());
        }
        holder.name.setText(contractData.getContractItem());
        if(showPrice) {
            holder.budget.setText(df.format(contractData.getBudget()));
            holder.spend.setText(df.format(contractData.getSpend()));
            holder.spendUnit.setText(contractData.getPriceUnit());
            if(getItemId(position)!=data.size()-1) {
                holder.price.setText(df.format(contractData.getPrice()));
            }
            holder.linear6.setVisibility(View.VISIBLE);
            holder.linear7.setVisibility(View.VISIBLE);
            holder.linear8.setVisibility(View.VISIBLE);
            holder.linear9.setVisibility(View.VISIBLE);
        }
        return view;
    }

    private class Holder{
        TextView name;
        TextView contractNum;
        TextView hasdoneNum;
        TextView rest;
        TextView unit;

        TextView price;
        TextView budget;
        TextView spend;
        TextView spendUnit;

        LinearLayout linear6;
        LinearLayout linear7;
        LinearLayout linear8;
        LinearLayout linear9;

    }
}
