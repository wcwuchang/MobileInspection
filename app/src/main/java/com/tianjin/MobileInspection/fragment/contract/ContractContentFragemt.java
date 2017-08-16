package com.tianjin.MobileInspection.fragment.contract;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.activity.contract.ContractDetailActivity;
import com.tianjin.MobileInspection.adapter.ContractDetailRoadAdapter;
import com.tianjin.MobileInspection.entity.ContractManager;
import com.tianjin.MobileInspection.fragment.BaseFragment;

/**
 * Created by 吴昶 on 2017/2/27.
 */
public class ContractContentFragemt extends BaseFragment {

    private View view;

    private TextView mtvContractName;
    private TextView mtvContractStart;
    private TextView mtvContractEnd;
    private TextView mtvContractOffice;
    private EditText mtvContractContent;
    private ListView mlvContractDetail;

    private ContractDetailRoadAdapter adapter;
    private ContractManager contractManager;
    private ContractDetailActivity contractDetail;

    public void setInit(Context c,ContractDetailActivity contractDetail){
        this.context=c;
        this.contractDetail=contractDetail;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null) {
            view = inflater.inflate(R.layout.fragment_contract_content, container, false);
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
        mtvContractName=(TextView)view.findViewById(R.id.tv_contract_detail_name);
        mtvContractStart=(TextView)view.findViewById(R.id.tv_contract_detail_begintime);
        mtvContractEnd=(TextView)view.findViewById(R.id.tv_contract_detail_endtime);
        mtvContractOffice=(TextView)view.findViewById(R.id.tv_contract_detail_untilname);
        mtvContractContent=(EditText)view.findViewById(R.id.tv_contract_detail);
        mlvContractDetail=(ListView)view.findViewById(R.id.lv_contract_detail);
    }

    private void initData() {
        adapter=new ContractDetailRoadAdapter(context);
        mlvContractDetail.setAdapter(adapter);
        contractManager=contractDetail.getCm();
        if(contractManager==null){
            return;
        }
        showContractData();
    }

    public void updata(ContractManager contractManager){
        this.contractManager=contractManager;
        showContractData();
    }

    private void showContractData() {
        mtvContractName.setText(contractManager.getName());
        mtvContractStart.setText(contractManager.getBeginDate());
        mtvContractEnd.setText(contractManager.getEndDate());
        mtvContractOffice.setText(contractManager.getUnitName());
        mtvContractContent.setText(contractManager.getContent());
        if(contractManager.getRoad()!=null&&contractManager.getRoad().size()>0){
            adapter.updata(contractManager.getRoad());
        }
    }
}
