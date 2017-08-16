package com.tianjin.MobileInspection.activity.plan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.socks.library.KLog;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.adapter.PlanListAdapter;
import com.tianjin.MobileInspection.customview.pullReflashView.PullToRefreshLayout;
import com.tianjin.MobileInspection.customview.pullReflashView.PullableListView;
import com.tianjin.MobileInspection.entity.PlanManage;
import com.tianjin.MobileInspection.main.BaseActivity;
import com.tianjin.MobileInspection.module.PlanModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuangaoran on 2016-12-30.
 *
 * 计划巡检列表
 */
public class PlanInspectionActivity extends BaseActivity{

    private final static int INT_HAS_LOAD_ALL_DATA=0x98;

    private LinearLayout mlinearBack;
    private TextView mtvTitle;
    private LinearLayout mlinearAdd;

    private List<PlanManage> pmList;
    private List<PlanManage> planManages;
    private PlanListAdapter adapter;
    private Intent intent;
    private int type=0;
    private String title;
    private String planType;
    private PullToRefreshLayout pullToRefreshLayout;
    private PullableListView mPLVContractList;

    private int pageNo=1;
    private int pageSize=20;
    private boolean  isreflash=false;
    private boolean  isloadmore=false;

//    private TPlanManage tPlanMange;//本地数据库
    private PlanModule module;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_type_list);
        initView();
        initData();
    }

    private void initData() {
        pmList=new ArrayList<PlanManage>();
        adapter=new PlanListAdapter(this);
        mPLVContractList.setAdapter(adapter);

        intent=getIntent();
        type=intent.getIntExtra("type",0);
        KLog.d(type+"");
        title=intent.getStringExtra("title");
        mtvTitle.setText(title);
//        tPlanMange= MyApplication.DBM.gettPlanManage();
        setTestData();

        module=new PlanModule(handler,this);
        module.getPlanTypeList(String.valueOf(type));

        mPLVContractList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent1=new Intent("android.intent.action.PLANDETAIL");
//                intent1.putExtra("planType",String.valueOf(type));
//                intent1.putExtra("planId",((PlanManage)adapter.getItem(position)).getPlanId());
//                intent1.putExtra("planTitle",((PlanManage)adapter.getItem(position)).getPlanName());
//                startActivity(intent1);
            }
        });

    }

    private void initView() {
        mlinearBack=(LinearLayout)findViewById(R.id.linear_return_back);
        mtvTitle=(TextView)findViewById(R.id.tv_activity_title);
        mlinearAdd=(LinearLayout)findViewById(R.id.linear_add);

        pullToRefreshLayout=(PullToRefreshLayout)findViewById(R.id.prelat_layout);
        mPLVContractList=(PullableListView)findViewById(R.id.lv_pullreflash);
        pullToRefreshLayout.setOnRefreshListener(new MyListener());

//        mlinearAdd.setVisibility(View.VISIBLE);
        mlinearBack.setOnClickListener(this);
//        mlinearAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.linear_return_back:
                finish();
                break;
            case R.id.linear_add:
                Intent intent1=new Intent("android.intent.action.NEWPLANACTIVITY");
                intent1.putExtra("planType",planType);
                startActivity(intent1);
                break;
        }
    }

    private void setTestData(){
        switch (type){
            case 1:
                planType="巡检";
                break;
            case 2:
                planType="日常";
                break;
            case 3:
                planType="专项";
                break;
            case 4:
                planType="财务";
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        planManages();
    }

//    public void planManages(){
//        planManages=tPlanMange.query(pageNo,pageSize,planType);
//        if((planManages==null||planManages.size()==0)&&pmList.size()==0){
//            Toast.makeText(this,"当前无数据",Toast.LENGTH_SHORT).show();
//        }else {
//            if(pageNo==1) pmList.clear();
//            pmList.addAll(planManages);
//            adapter.updata(pmList);
//        }
//        stopPullToRefreshLayoutSuccess();
//    }



    @Override
    public void resultData(Message msg) {
        super.resultData(msg);
        switch (msg.what){
            case PlanModule.PLAN_LIST_SUCCESS:
                planManages =(ArrayList<PlanManage>)msg.obj;
                showData();
                break;
            case PlanModule.PLAN_LIST_FILED:
                if(msg.arg1==-1){
                    Toast.makeText(this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
                }else {
                    toLoginActivity(msg);
                }
                stopPullToRefreshLayoutFail();
                break;
        }
    }

    private void showData() {
        if(pageNo==1) pmList.clear();
        pmList.addAll(planManages);
        adapter.updata(pmList);
        stopPullToRefreshLayoutSuccess();
    }

    private void stopPullToRefreshLayoutSuccess(){
        if(isreflash){
            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        }
        if(isloadmore){
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        }
    }

    private void stopPullToRefreshLayoutFail(){
        if(isreflash){
            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
        }
        if(isloadmore){
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
        }
    }

    //PullableListView下拉上拉监听
    public class MyListener implements PullToRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
            // 下拉刷新操作
            isreflash=true;
            pageNo=1;
            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
//            module.getPlanTypeList(String.valueOf(type));
//            planManages();
        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            //上拉加载操作
            isloadmore=true;
            Toast.makeText(PlanInspectionActivity.this,"已加载所有数据",Toast.LENGTH_SHORT).show();
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
//            if(planManages.size()<pageSize){
//                isloadmore=false;
//            }else {
//                pageNo++;
//
////                planManages();
//            }
        }

    }
}
