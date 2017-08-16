package com.tianjin.MobileInspection.activity.plan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.socks.library.KLog;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.customview.dialog.DateTimeDialog;
import com.tianjin.MobileInspection.databasetable.TPlanManage;
import com.tianjin.MobileInspection.entity.PlanManage;
import com.tianjin.MobileInspection.main.BaseActivity;
import com.tianjin.MobileInspection.module.PlanModule;
import com.tianjin.MobileInspection.until.CheckUntil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wuchang on 2016-12-30.
 *
 * 新建计划
 */
public class NewPlanActivity extends BaseActivity implements DateTimeDialog.MyOnDateSetListener{

    private LinearLayout mlinearBack;
    private LinearLayout mlinearSave;
    private TextView mtvTitle;
    private ImageView mivSave;

    private EditText medtTitle;
    private EditText medtContent;

    private TextView mtvStartTime;
    private TextView mtvEndTime;

    private RelativeLayout mrelatStart;
    private RelativeLayout mrelatEnd;

    private PlanManage planMange;
    private TPlanManage tPlanManage;
    private Intent intent;
    private String planType;
    private boolean isAdd=true;
    private DateTimeDialog dateTimeDialog;
    private boolean start=false;
    private Date startDate;
    private Date endDate;

    private PlanModule module;

    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_new);
        initView();
        initData();
    }

    private void initData() {
        tPlanManage=MyApplication.DBM.gettPlanManage();
        intent=getIntent();
        planType=intent.getStringExtra("planType");
        isAdd=intent.getBooleanExtra("add",true);
        KLog.d(planType);
//            mlinearSave.setVisibility(View.VISIBLE);
            mrelatStart.setOnClickListener(this);
            mrelatEnd.setOnClickListener(this);
//            mlinearSave.setOnClickListener(this);
            medtContent.setHint("请输入计划内容");
            medtTitle.setHint("请输入计划名称");
            mtvTitle.setText("新建"+planType+"计划");
            dateTimeDialog = new DateTimeDialog(this, null, this);
    }

    private void initView() {
        mlinearBack=(LinearLayout)findViewById(R.id.linear_return_back);
        mlinearSave=(LinearLayout)findViewById(R.id.linear_add);
        mtvTitle=(TextView)findViewById(R.id.tv_activity_title);
        mivSave=(ImageView)findViewById(R.id.iv_add);

        medtTitle=(EditText)findViewById(R.id.edt_plan_name);
        medtContent=(EditText)findViewById(R.id.edt_plan_content);
        mtvStartTime=(TextView)findViewById(R.id.tv_plan_start_time);
        mtvEndTime=(TextView)findViewById(R.id.tv_plan_end_time);
        mrelatStart=(RelativeLayout)findViewById(R.id.relat_plan_start_time);
        mrelatEnd=(RelativeLayout)findViewById(R.id.relat_plan_end_time);

        mivSave.setImageResource(R.drawable.list_ico_save);

        mlinearBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.linear_return_back:
                finish();
                break;
            case R.id.linear_add:
                if(checkData()){
                    planMange.setCreateMan(MyApplication.getStringSharedPreferences("name",""));
                    planMange.setCreateManId(MyApplication.getStringSharedPreferences("userId",""));
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    Date date=new Date(System.currentTimeMillis());
                    planMange.setCreateDate(sdf.format(date));
                    planMange.setPlanId(String.valueOf(System.currentTimeMillis()));
                    planMange.setPlanType(planType);
                    int result=tPlanManage.insert(planMange);
                    if(result==1){
                        Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(this,"保存失败",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.relat_plan_start_time:
                start=true;
                dateTimeDialog.hideOrShow();
                break;
            case R.id.relat_plan_end_time:
                start=false;
                dateTimeDialog.hideOrShow();
                break;

        }
    }

    private boolean checkData(){
        planMange=new PlanManage();
        if(CheckUntil.checkEditext(medtTitle.getText()).equals("")){
            Toast.makeText(this,"请填写计划名称",Toast.LENGTH_SHORT).show();
            return false;
        }
        planMange.setPlanName(CheckUntil.checkEditext(medtTitle.getText()));
        if(CheckUntil.checkEditext(medtContent.getText()).equals("")){
            Toast.makeText(this,"请填写计划内容",Toast.LENGTH_SHORT).show();
            return false;
        }
        planMange.setPlanContent(CheckUntil.checkEditext(medtContent.getText()));
        if(CheckUntil.checkEditext(mtvStartTime.getText()).equals("")){
            Toast.makeText(this,"请选择计划开始时间",Toast.LENGTH_SHORT).show();
            return false;
        }
        planMange.setPlanStartDate(mtvStartTime.getText().toString());
        if(CheckUntil.checkEditext(mtvEndTime.getText()).equals("")){
            Toast.makeText(this,"请选择计划结束时间",Toast.LENGTH_SHORT).show();
            return false;
        }
        planMange.setPlanEndDate(mtvEndTime.getText().toString());
        return true;
    }

    @Override
    public void onDateSet(Date date) {
        if(start){
            if(endDate==null){
                startDate=date;
            }else {
                if(endDate.before(date)){
                    Toast.makeText(this,"计划结束时间不得早于计划开始时间",Toast.LENGTH_SHORT).show();
                    mtvEndTime.setText("");
                }
                startDate=date;
            }
            mtvStartTime.setText(sdf.format(date));
        }else {
            if(startDate==null){
                endDate=date;
            }else {
                if(date.before(startDate)){
                    Toast.makeText(this,"计划结束时间不得早于计划开始时间",Toast.LENGTH_SHORT).show();
                    mtvEndTime.setText("");
                    return;
                }
                endDate=date;
            }
            mtvEndTime.setText(sdf.format(date));
        }
    }

}
