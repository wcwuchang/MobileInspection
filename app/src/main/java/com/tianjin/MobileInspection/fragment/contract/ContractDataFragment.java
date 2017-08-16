package com.tianjin.MobileInspection.fragment.contract;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.activity.contract.ContractDetailActivity;
import com.tianjin.MobileInspection.adapter.ContractDataAdapter;
import com.tianjin.MobileInspection.customview.ScrollListView;
import com.tianjin.MobileInspection.entity.ContractData;
import com.tianjin.MobileInspection.entity.ContractManager;
import com.tianjin.MobileInspection.entity.Road;
import com.tianjin.MobileInspection.fragment.BaseFragment;
import com.tianjin.MobileInspection.module.ContractModule;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by wuchang on 2017-4-5.
 *
 * 合同完成量的表格数据展示
 */
public class ContractDataFragment extends BaseFragment {

    private View view;

    private Spinner mspMonth;
    private ScrollListView mlvContractData;

    private TextView mtvContractName;
    private TextView mtvBegainTime;
    private TextView mtvEndTime;
    private TextView mtvUnitName;

    private TextView mtvMonth;
    private TextView mtvContractNum;
    private TextView mtvContractHasdo;
    private TextView mtvContractRest;
    private TextView mtvContractUnit;

    private TextView mtvSpendPrice;
    private TextView mtvSpendBudget;
    private TextView mtvSpendHasdo;
    private TextView mtvSpendUnit;

    private LinearLayout linear6;
    private LinearLayout linear7;
    private LinearLayout linear8;
    private LinearLayout linear9;

    private RelativeLayout mrelatMonth;

    private ContractDataAdapter contractadapter;
    private ContractModule module;
    private ContractDetailActivity contractDetail;
    private ContractManager cm;
    private String mstrContractId;
    private int mintContractType;
    private boolean isInspection;
    private int month=1;

    private ArrayList<ArrayList<ContractData>> yearData;

    public void setInit(Context c, ContractDetailActivity contractDetail){
        this.context=c;
        this.contractDetail=contractDetail;
    }

    public void setContractInfo(String mstrContractId,int type){
        this.mstrContractId=mstrContractId;
        this.mintContractType=type;
        if(mintContractType==1){
            isInspection=true;
        }else {
            isInspection=false;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null) {
            view = inflater.inflate(R.layout.fragment_contract_data, container, false);
            initView();
            initData();
        }
        ViewGroup parent=(ViewGroup)view.getParent();
        if(parent!=null){
            parent.removeView(view);
        }
        return view;
    }

    private void initView() {
        mspMonth=(Spinner)view.findViewById(R.id.sp_month);
        mlvContractData=(ScrollListView) view.findViewById(R.id.lv_contract_data);
        mtvMonth=(TextView)view.findViewById(R.id.tv_contract_1);
        mtvContractNum=(TextView)view.findViewById(R.id.tv_contract_2);
        mtvContractHasdo=(TextView)view.findViewById(R.id.tv_contract_3);
        mtvContractRest=(TextView)view.findViewById(R.id.tv_contract_4);
        mtvContractUnit=(TextView)view.findViewById(R.id.tv_contract_5);

        mtvSpendPrice=(TextView)view.findViewById(R.id.tv_contract_6);
        mtvSpendBudget=(TextView)view.findViewById(R.id.tv_contract_7);
        mtvSpendHasdo=(TextView)view.findViewById(R.id.tv_contract_8);
        mtvSpendUnit=(TextView)view.findViewById(R.id.tv_contract_9);

        mtvContractName=(TextView)view.findViewById(R.id.tv_contract_detail_name);
        mtvBegainTime=(TextView)view.findViewById(R.id.tv_contract_begain_time);
        mtvEndTime=(TextView)view.findViewById(R.id.tv_contract_end_time);
        mtvUnitName=(TextView)view.findViewById(R.id.tv_contract_unit_name);

        mrelatMonth=(RelativeLayout)view.findViewById(R.id.relat_chose_month);

        linear6=(LinearLayout)view.findViewById(R.id.linear_6);
        linear7=(LinearLayout)view.findViewById(R.id.linear_7);
        linear8=(LinearLayout)view.findViewById(R.id.linear_8);
        linear9=(LinearLayout)view.findViewById(R.id.linear_9);

    }

    private void initData() {
        mtvMonth.setText("名称");
        mtvContractNum.setText("合同数量");
        mtvContractHasdo.setText("已完成数量");
        mtvContractRest.setText("剩余数量");
        mtvContractUnit.setText("单位");

        mtvSpendPrice.setText("单价");
        mtvSpendBudget.setText("预算");
        mtvSpendHasdo.setText("已花费");
        mtvSpendUnit.setText("单位");

        yearData=new ArrayList<ArrayList<ContractData>>();
        for(int i=0;i<12;i++){
            ArrayList<ContractData> datas=new ArrayList<ContractData>();
            yearData.add(datas);
        }

        Calendar calendar=Calendar.getInstance();
        month=calendar.get(Calendar.MONTH);

        String[] mItems=getActivity().getResources().getStringArray(R.array.month);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mspMonth.setAdapter(adapter);

        contractadapter=new ContractDataAdapter(context);
        mspMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                contractadapter.updata(yearData.get(position),!isInspection);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        module=new ContractModule(handler,context);
        module.getContractDetail(mstrContractId);

    }

    @Override
    public void resultData(Message msg) {
        super.resultData(msg);
        switch (msg.what){
            case ContractModule.INT_CONTRACT_DETAIL_SUCCESS:
                cm=(ContractManager)msg.obj;
                showData();
                break;
            case ContractModule.INT_CONTRACT_DETAIL_FILED:
                toLoginActivity(msg);
                break;
        }
    }

    private ArrayList<ContractData> prossPrice(ArrayList<ContractData> data){
        ContractData o = new ContractData();
        data.add(o);
        int size=data.size()-1;
        data.get(size).setContractItem("总计");
        double price=0;
        double spend=0;
        for(int i=0;i<data.size()-1;i++){
            price=price+data.get(i).getBudget();
            spend=spend+data.get(i).getSpend();
        }
        data.get(size).setBudget(price);
        data.get(size).setSpend(spend);
        data.get(size).setPriceUnit("元");
        return data;
    }


    private void showData(){
        if(cm==null) return;
        mtvContractName.setText(cm.getName());
        mtvBegainTime.setText(cm.getBeginDate());
        mtvEndTime.setText(cm.getEndDate());
        mtvUnitName.setText(cm.getUnitName());

        ArrayList<ContractData> contractDatas=cm.getDatalist();

        for(int i=0;i<contractDatas.size();i++){
            ContractData data=contractDatas.get(i);
            yearData.get(data.getMonth()-1).add(data);
        }

        ArrayList<Road> roads=cm.getRoad();
        final double[] cunm={20,16,18,25,16,21,22,22,26};
        final double[] hnum={5,2,3,3,4,2,1,5,4};
        if(!isInspection) {
            linear6.setVisibility(View.VISIBLE);
            linear7.setVisibility(View.VISIBLE);
            linear8.setVisibility(View.VISIBLE);
            linear9.setVisibility(View.VISIBLE);
            for(int i=0;i<yearData.size();i++) {
                for(int j=0;j<yearData.get(i).size();j++) {
                    yearData.get(i).get(j).setPriceUnit("元");
                    prossPrice(yearData.get(i));
                }
            }
        }else {
            for(int i=0;i<yearData.size();i++) {
                for(int j=0;j<yearData.get(i).size();j++) {
                    yearData.get(i).get(j).setUnit("次");
                }
            }
        }
        mlvContractData.setAdapter(contractadapter);
        mspMonth.setSelection(month);
//        contractadapter.updata(yearData.get(),!isInspection);
    }

}
