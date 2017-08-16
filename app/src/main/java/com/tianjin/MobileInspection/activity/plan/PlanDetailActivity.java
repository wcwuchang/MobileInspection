package com.tianjin.MobileInspection.activity.plan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.entity.PlanManage;
import com.tianjin.MobileInspection.fragment.PlanContentFragment;
import com.tianjin.MobileInspection.fragment.PlanHasdoFragment;
import com.tianjin.MobileInspection.main.BaseActivity;
import com.tianjin.MobileInspection.module.PlanModule;

/**
 * Created by wuchang on 2017-2-28.
 */
public class PlanDetailActivity extends BaseActivity {

    private RadioGroup mrgPlan;
    private LinearLayout mlinearBack;
    private LinearLayout mlinearSave;
    private TextView mtvTitle;
    private ImageView mivSave;

    private PlanManage planManage;
    private PlanModule planModule;

    private String planId;
    private String planType;
    private Intent intent;

    private FragmentManager fragmentManager;

    private PlanContentFragment contentFragment;
    private PlanHasdoFragment hasdoFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_detail);
        initView();
        initData();
    }

    private void initData() {
        fragmentManager=getSupportFragmentManager();
        intent=getIntent();
        planType=intent.getStringExtra("planType");
        planId=intent.getStringExtra("planId");

        String title=intent.getStringExtra("planTitle");
        mtvTitle.setText(title);

        planModule=new PlanModule(handler,this);
        planModule.getPlanDetail(planId);
    }

    private void initView() {
        mrgPlan=(RadioGroup)findViewById(R.id.rg_plan_group);
        mlinearBack=(LinearLayout)findViewById(R.id.linear_return_back);
        mtvTitle=(TextView)findViewById(R.id.tv_activity_title);
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
            case PlanModule.GET_PLAN_DETAIL_SUCCESS:
                planManage=(PlanManage)msg.obj;
                initEvent();
                break;
            case PlanModule.GET_PLAN_DETAIL_FAIL:
                toLoginActivity(msg);
                break;
        }
    }

    public PlanManage getPlanManage(){
        return planManage;
    }

    private void initEvent() {
        mrgPlan.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_plan_content:
                        if(contentFragment==null){
                            contentFragment=new PlanContentFragment();
                            contentFragment.initFragment(PlanDetailActivity.this,PlanDetailActivity.this);
                        }
                        changFragment(contentFragment);
                        break;
                    case R.id.rb_plan_has_do:
                        if(hasdoFragment==null){
                            hasdoFragment=new PlanHasdoFragment();
                            hasdoFragment.init(PlanDetailActivity.this,PlanDetailActivity.this);
                        }
                        changFragment(hasdoFragment);
                        break;
                }
            }
        });
        mrgPlan.check(R.id.rb_plan_content);
    }


    private void changFragment(Fragment fragment){
        FragmentTransaction tran=fragmentManager.beginTransaction();
        tran.replace(R.id.frame_plan_body,fragment);
        tran.commit();
    }

}
