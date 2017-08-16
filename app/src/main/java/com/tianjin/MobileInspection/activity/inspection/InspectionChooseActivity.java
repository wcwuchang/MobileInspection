package com.tianjin.MobileInspection.activity.inspection;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.adapter.InspectionChooseAdapter;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.customview.pullReflashView.PullToRefreshLayout;
import com.tianjin.MobileInspection.customview.pullReflashView.PullableListView;
import com.tianjin.MobileInspection.entity.InspectionChoose;
import com.tianjin.MobileInspection.entity.Road;
import com.tianjin.MobileInspection.main.BaseActivity;
import com.tianjin.MobileInspection.module.ApproveModule;
import com.tianjin.MobileInspection.module.InspectionModule;
import com.tianjin.MobileInspection.until.ArrayListInspectionPaixuByDate;
import com.tianjin.MobileInspection.until.ConnectionURL;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 选择巡检列表
 * Created by wuchang on 2016-11-22.
 */
public class InspectionChooseActivity extends BaseActivity implements View.OnClickListener{

    private static final int INT_HAS_LOAD_ALL_DATA=0x678;
    private TextView mtvTitle;
    private LinearLayout mlinearBack;

    private PullToRefreshLayout pullToRefreshLayout;
    private PullableListView mPLVContractList;

    private List<InspectionChoose> choseList;
    private List<InspectionChoose> getList;
    private InspectionChooseAdapter adapter;
    private InspectionModule module;

    private int pageNo=1;
    private int pageSize=20;
    private boolean refresh=false;
    private boolean loadMore=false;
    private ApproveModule approveModule;

    private AlertDialog.Builder  tuichuDialog;
    private String inspectionId;
    private int trafficId;

    private int state;

    private List<Road> roads;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_choose);
        initView();
        initData();
    }

    private void initData() {
        choseList=new ArrayList<InspectionChoose>();
        getList=new ArrayList<InspectionChoose>();
        roads=new ArrayList<Road>();
        adapter=new InspectionChooseAdapter(this,this);
        mPLVContractList.setAdapter(adapter);
        module=new InspectionModule(handler,this);
        approveModule=new ApproveModule(handler,this);

    }

    private void initView() {
        mtvTitle=(TextView) findViewById(R.id.tv_activity_title);
        mlinearBack=(LinearLayout)findViewById(R.id.linear_return_back);

        pullToRefreshLayout=(PullToRefreshLayout)findViewById(R.id.prelat_layout);
        mPLVContractList=(PullableListView)findViewById(R.id.lv_pullreflash);
        pullToRefreshLayout.setOnRefreshListener(new MyListener());

        mtvTitle.setText(getString(R.string.str_inspection_choose));
        mlinearBack.setOnClickListener(this);
        mPLVContractList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final InspectionChoose ic=(InspectionChoose)adapter.getItem(position);
                inspectionId=ic.getInspectionId();
                trafficId=ic.getTrafficId();
                state=ic.getState();
                roads=ic.getRoads();
                    if (ic.getState() == 1) {
                        tuichuDialog = new AlertDialog.Builder(InspectionChooseActivity.this).setMessage("确定开始  " + ic.getInspectionName() + "  任务?");
                        tuichuDialog.setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Map<String, String> map = new HashMap<String, String>();
                                        map.put("id", inspectionId);
                                        map.put("act.taskId", ic.getTaskId());
                                        map.put("act.taskDefKey", ic.getTaskDefKey());
                                        map.put("act.procInsId", ic.getProcInsId());
                                        map.put("act.comment", "开始巡检");
                                        map.put("act.flag", "yes");
                                        approveModule.approveCommit(ConnectionURL.STR_INSPECTION_APPROVE_COMMIT, map);
                                    }
                                });
                        tuichuDialog.setNegativeButton("取消", null);
                        tuichuDialog.show();
                    } else {
                        Intent inm = new Intent("android.intent.action.LOACTIONTRACKACTIVITY");
                        inm.putExtra("id", inspectionId);
                        inm.putExtra("trafficId", ic.getTrafficId());
                        inm.putExtra("taskId", ic.getTaskId());
                        inm.putExtra("taskDefKey", ic.getTaskDefKey());
                        inm.putExtra("procInsId", ic.getProcInsId());
                        inm.putExtra("procDefKey", ic.getProcDefKey());
                        inm.putExtra("beginDate", ic.getDate());
                        inm.putExtra("state", state);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("roads", (Serializable) roads);
                        inm.putExtras(bundle);
                        startActivity(inm);
                    }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        pageNo=1;
        module.getInspectionChoseLList(String.valueOf(pageNo),String.valueOf(pageSize),
                MyApplication.getStringSharedPreferences("userId",""),"1");
    }

    public void showMore(int position){
        if(choseList.get(position).isMore()) {
            choseList.get(position).setMore(false);
        }else{
            choseList.get(position).setMore(true);
        }
        adapter.update(choseList);
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
            case InspectionModule.INT_GET_INSPECTION_CHOSE_SUCCESS:
                getList=(List<InspectionChoose>)msg.obj;
                showData();
                break;
            case InspectionModule.INT_GET_INSPECTION_CHOSE_FILED:
                toLoginActivity(msg);
                stopPullToRefreshLayoutFiled();
                break;
            case INT_HAS_LOAD_ALL_DATA:
                stopPullToRefreshLayoutSuccess();
                break;
            case ApproveModule.INT_COMMIT_APPROVE_SUCCESS:
                Intent in=new Intent("android.intent.action.LOACTIONTRACKACTIVITY");
                in.putExtra("id",inspectionId);
                in.putExtra("trafficId",trafficId);
                in.putExtra("taskId","false");
                in.putExtra("state",state);
                Bundle bundle=new Bundle();
                bundle.putSerializable("roads",(Serializable)roads);
                in.putExtras(bundle);
                startActivity(in);
                break;
            case ApproveModule.INT_COMMIT_APPROVE_FILED:
                toLoginActivity(msg);
                break;
        }
    }

    private void showData(){
        if(pageNo==1){
            choseList.clear();
        }
        choseList.addAll(getList);
        listDataPaixunByDate();
        adapter.update(choseList);
        stopPullToRefreshLayoutSuccess();
    }

    private void listDataPaixunByDate() {
        Collections.sort(choseList,new ArrayListInspectionPaixuByDate());
    }

    private void stopPullToRefreshLayoutSuccess() {
        if(refresh){
            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            refresh=false;
        }
        if(loadMore){
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            loadMore=false;
        }
    }

    private void stopPullToRefreshLayoutFiled() {
        if(refresh){
            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
            refresh=false;
        }
        if(loadMore){
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
            loadMore=false;
        }
    }

    //PullableListView下拉上拉监听
    public class MyListener implements PullToRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
            // 下拉刷新操作
            refresh=true;
            pageNo=1;
            module.getInspectionChoseLList(String.valueOf(pageNo),String.valueOf(pageSize)
            ,MyApplication.getStringSharedPreferences("userId",""),"1");
        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            //上拉加载操作
            loadMore=true;
            if(getList.size()<pageSize){
                Toast.makeText(InspectionChooseActivity.this,"已加载全部数据",Toast.LENGTH_SHORT).show();
                handler.sendEmptyMessageAtTime(INT_HAS_LOAD_ALL_DATA,1000);
            }else {
                pageNo++;
                module.getInspectionChoseLList(String.valueOf(pageNo),String.valueOf(pageSize)
                ,MyApplication.getStringSharedPreferences("userId",""),"1");
            }
        }
    }
}
