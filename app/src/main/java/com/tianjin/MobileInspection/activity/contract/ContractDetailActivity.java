package com.tianjin.MobileInspection.activity.contract;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.entity.ContractManager;
import com.tianjin.MobileInspection.fragment.contract.ContractContentFragemt;
import com.tianjin.MobileInspection.fragment.contract.ContractDataFragment;
import com.tianjin.MobileInspection.fragment.contract.ContractHasdoFragment;
import com.tianjin.MobileInspection.main.BaseActivity;
import com.tianjin.MobileInspection.module.ContractModule;

/**
 * Created by 吴昶 on 2017/2/27.
 *
 * 合同详情
 */
public class ContractDetailActivity extends BaseActivity {

    private LinearLayout mlinearBack;
    private LinearLayout mlinearOption;
    private TextView mtvTitle;
    private TextView mtvOption;

    private RadioGroup mrgContract;

    private Intent intent;
    private String mstrContractId;
    private int contractType;

    private ContractModule module;
    private ContractManager cm;
    private FragmentManager fragmentManager;
    private ContractContentFragemt contractContentFragemt;
    private ContractHasdoFragment contractHasdoFragment;

    private ContractDataFragment contractDataFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_detail);
        initView();
        initData();
    }

    private void initData() {
        intent=getIntent();
        mstrContractId=intent.getExtras().getString("id");
        contractType=intent.getExtras().getInt("type");
//        module=new ContractModule(handler,this);
//        module.getContractDetail(mstrContractId);
        fragmentManager=getSupportFragmentManager();
        if(contractDataFragment==null){
            contractDataFragment=new ContractDataFragment();
            contractDataFragment.setInit(ContractDetailActivity.this,ContractDetailActivity.this);
            contractDataFragment.setContractInfo(mstrContractId,contractType);
        }
        changFragment(contractDataFragment);
    }

    private void initView() {
        mlinearBack=(LinearLayout)findViewById(R.id.linear_return_back);
        mlinearOption=(LinearLayout)findViewById(R.id.linear_save);
        mtvTitle=(TextView)findViewById(R.id.tv_activity_title);
        mtvOption=(TextView)findViewById(R.id.tv_activity_mun);

        mrgContract=(RadioGroup)findViewById(R.id.rg_contract_group);
        mtvTitle.setText("合同详情");
        mlinearBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.linear_return_back:
                finish();
                break;
        }
    }

    @Override
    public void resultData(Message msg) {
        super.resultData(msg);
        switch (msg.what){
            case ContractModule.INT_CONTRACT_DETAIL_SUCCESS:
                cm=(ContractManager)msg.obj;
//                initEvent();
                break;
            case ContractModule.INT_CONTRACT_DETAIL_FILED:
                toLoginActivity(msg);
                break;
        }
    }

    public ContractManager getCm(){
        return cm;
    }

//    private void initEvent() {
//        mrgContract.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch (checkedId){
//                    case R.id.rb_contract_content:
//                        if(contractContentFragemt==null){
//                            contractContentFragemt=new ContractContentFragemt();
//                            contractContentFragemt.setInit(ContractDetailActivity.this,ContractDetailActivity.this);
//                        }
//                        changFragment(contractContentFragemt);
//                        break;
//                    case R.id.rb_conyract_has_do:
////                        if(contractHasdoFragment==null){
////                            contractHasdoFragment=new ContractHasdoFragment();
////                            contractHasdoFragment.init(ContractDetailActivity.this,ContractDetailActivity.this);
////                        }
////                        changFragment(contractHasdoFragment);
//                        if(contractDataFragment==null){
//                            contractDataFragment=new ContractDataFragment();
//                            contractDataFragment.setInit(ContractDetailActivity.this,ContractDetailActivity.this);
//                        }
//                        changFragment(contractDataFragment);
//                        break;
//                }
//            }
//        });
//
//        mrgContract.check(R.id.rb_contract_content);
//    }

    private void changFragment(Fragment fragment){
        FragmentTransaction tran=fragmentManager.beginTransaction();
        tran.replace(R.id.frame_contract_body,fragment);
        tran.commit();
    }
}
