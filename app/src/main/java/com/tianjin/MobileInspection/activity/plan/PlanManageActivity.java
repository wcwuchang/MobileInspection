package com.tianjin.MobileInspection.activity.plan;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.adapter.PlanTypeListAdapter;
import com.tianjin.MobileInspection.entity.PlanManage;
import com.tianjin.MobileInspection.main.BaseActivity;
import com.tianjin.MobileInspection.module.PlanModule;

import java.util.ArrayList;

/**
 * Created by wuchang on 2016-11-21.
 *
 * 计划管理
 */
public class PlanManageActivity extends BaseActivity implements View.OnClickListener{

    private ListView mlvPlanList;
    private LinearLayout mlinearBack;
    private TextView mtvTitle;

    private ArrayList<PlanManage> planlist;
    private PlanTypeListAdapter adater;

    private PlanModule module;

    private Dialog dialog;
    private View view;
    private TextView mtvPlanName;
    private Button mbtnDetail;
    private Button mbtnIssued;

    private PlanManage pmc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_manage);
        initView();
        initData();
    }

    private void initData() {
        mtvTitle.setText(getString(R.string.str_plan_manage_title));
        planlist=new ArrayList<PlanManage>();
        module=new PlanModule(handler,this);
        adater=new PlanTypeListAdapter(this,this);
        mlvPlanList.setAdapter(adater);
        module.getPlanTypeList();

        mlvPlanList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(PlanManageActivity.this,adater.getItem(position).toString(),Toast.LENGTH_SHORT).show();
                pmc=(PlanManage)adater.getItem(position);
//                Intent intent=new Intent("android.intent.action.PLANINSPECTIONACTIVITY");
//                intent.putExtra("type",position+1);
//                intent.putExtra("title",pmc.getPlanName());
//                intent.putExtra("id",pmc.getPlanId());
//                startActivity(intent);
                Intent intent=new Intent(PlanManageActivity.this,PlanDetailActivity.class);
                intent.putExtra("planTitle",pmc.getPlanName());
                intent.putExtra("planId",pmc.getPlanId());
                intent.putExtra("planType",pmc.getPlanType());
                startActivity(intent);
//                module.getPlanDetail(pmc.getPlanId());
//                initDialog();
            }
        });

    }

    private void initDialog(){
        view= LayoutInflater.from(this).inflate(R.layout.dialog_plan_issued,null);
        mtvPlanName=(TextView)view.findViewById(R.id.tv_plan_name);
        mbtnDetail=(Button)view.findViewById(R.id.btn_dialog_detail);
        mbtnIssued=(Button)view.findViewById(R.id.btn_dialog_issued);

        mbtnIssued.setOnClickListener(listener);
        mbtnDetail.setOnClickListener(listener);

        mtvPlanName.setText(pmc.getPlanName());

        dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.show();
    }

    private void initView() {
        mlinearBack=(LinearLayout)findViewById(R.id.linear_return_back);
        mtvTitle=(TextView)findViewById(R.id.tv_activity_title);

        mlvPlanList=(ListView)findViewById(R.id.lv_plan_list);

        mlinearBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linear_return_back:
                this.finish();
                break;
        }
    }

    @Override
    public void resultData(Message msg) {
        super.resultData(msg);
        switch (msg.what){
            case PlanModule.PLAN_LIST_SUCCESS:
                planlist=(ArrayList<PlanManage>)msg.obj;
                adater.updata(planlist);
                break;
            case PlanModule.ISSUED_PLANTASK_SUCCESS:
                dialog.cancel();
                module.getPlanTypeList();
                break;
            case PlanModule.PLAN_LIST_FILED:
            case PlanModule.ISSUED_PLANTASK_FILED:
                 toLoginActivity(msg);
                break;
       }
    }


    private View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_dialog_detail:
                    Intent intent=new Intent(PlanManageActivity.this,PlanDetailActivity.class);
                    intent.putExtra("planTitle",pmc.getPlanName());
                    intent.putExtra("planId",pmc.getPlanId());
                    startActivity(intent);
                    dialog.cancel();
                    break;
                case R.id.btn_dialog_issued:
                    module.issuedPlanTask(pmc.getPlanId());
                    break;
            }
        }
    };
}
