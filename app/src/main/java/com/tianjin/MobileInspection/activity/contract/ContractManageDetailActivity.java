package com.tianjin.MobileInspection.activity.contract;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.adapter.ContractDetailRoadAdapter;
import com.tianjin.MobileInspection.entity.ContractManager;
import com.tianjin.MobileInspection.main.BaseActivity;
import com.tianjin.MobileInspection.module.ContractModule;

/**
 * Created by 吴昶 on 2016/12/7.
 *
 * 合同详情
 *
 * 废弃
 */
public class ContractManageDetailActivity extends BaseActivity {

    private LinearLayout mlinearBack;
    private LinearLayout mlinearOption;
    private TextView mtvTitle;
    private TextView mtvOption;

    private TextView mtvContractName;
    private TextView mtvContractStart;
    private TextView mtvContractEnd;
    private TextView mtvContractOffice;
    private EditText mtvContractContent;
    private ListView mlvContractDetail;

    private ContractModule module;
    private ContractDetailRoadAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_manage_detail);
        initView();
        initData();
    }

    private void initData() {
        mtvTitle.setText("合同详情");
        mtvOption.setText("操作");
//        mlinearOption.setVisibility(View.VISIBLE);
        module=new ContractModule(handler,this);
        Intent in=getIntent();
        String id=in.getExtras().getString("id");
        module.getContractDetail(id);
        adapter=new ContractDetailRoadAdapter(this);
        mlvContractDetail.setAdapter(adapter);
    }

    private void initView() {
        mlinearBack=(LinearLayout)findViewById(R.id.linear_return_back);
        mlinearOption=(LinearLayout)findViewById(R.id.linear_save);
        mtvTitle=(TextView)findViewById(R.id.tv_activity_title);
        mtvOption=(TextView)findViewById(R.id.tv_activity_mun);

        mtvContractName=(TextView)findViewById(R.id.tv_contract_detail_name);
        mtvContractStart=(TextView)findViewById(R.id.tv_contract_detail_begintime);
        mtvContractEnd=(TextView)findViewById(R.id.tv_contract_detail_endtime);
        mtvContractOffice=(TextView)findViewById(R.id.tv_contract_detail_untilname);
        mtvContractContent=(EditText)findViewById(R.id.tv_contract_detail);
        mlvContractDetail=(ListView)findViewById(R.id.lv_contract_detail);

        mlinearBack.setOnClickListener(this);
        mtvOption.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.linear_return_back:
                finish();
                break;
            case R.id.tv_activity_mun:

                break;
        }

    }

    @Override
    public void resultData(Message msg) {
        super.resultData(msg);
        switch (msg.what){
            case ContractModule.INT_CONTRACT_DETAIL_SUCCESS:
                ContractManager cm=(ContractManager)msg.obj;
                showContractData(cm);
                break;
            case ContractModule.INT_CONTRACT_DETAIL_FILED:
                toLoginActivity(msg);
                break;
        }
    }

    private void showContractData(ContractManager cm) {
        mtvContractName.setText(cm.getName());
        mtvContractStart.setText(cm.getBeginDate());
        mtvContractEnd.setText(cm.getEndDate());
        mtvContractOffice.setText(cm.getUnitName());
        mtvContractContent.setText(cm.getContent());
        if(cm.getRoad()!=null&&cm.getRoad().size()>0){
            adapter.updata(cm.getRoad());
//            ScreenUtils.setListViewHeightBasedOnChildren(this,mlvContractDetail);
        }
    }
}
