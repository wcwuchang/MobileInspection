package com.tianjin.MobileInspection.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.adapter.PlanMaintenanceAdapter;
import com.tianjin.MobileInspection.customview.pullReflashView.PullToRefreshLayout;
import com.tianjin.MobileInspection.customview.pullReflashView.PullableListView;
import com.tianjin.MobileInspection.entity.Task;
import com.tianjin.MobileInspection.module.TaskModlue;
import com.tianjin.MobileInspection.until.ArrayListSortByDate;

import java.util.ArrayList;
import java.util.Collections;

/**
 * xunjian
 * Created by wuchang on 2016-11-21.
 */
public class SpecialHasFinishedFragment extends BaseFragment {

    private String TAG="InspectionDoingFragment";
    private View view;
    private PullToRefreshLayout pullToRefreshLayout;
    private PullableListView mPLVContractList;

//    private DeailyListAdapter adapter;
    private PlanMaintenanceAdapter adapter;
    private ArrayList<Task> inspData;
    private ArrayList<Task> getData;
    private int month=12;
    private int day=15;
    private TaskModlue modlue;
    private int pageNo=1;
    private int pageSize=20;
    private boolean isreflash=false;
    private boolean isloadmore=false;
    private String state="4";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view==null) {
            view = inflater.inflate(R.layout.fragment_inspection, container, false);
            initView();
            initData();
        }
        ViewGroup parent=(ViewGroup)view.getParent();
        if(parent!=null){
            parent.removeView(view);
        }
        return view;
    }

    private void initData() {
        inspData=new ArrayList<Task>();
        getData=new ArrayList<Task>();
//        adapter = new DeailyListAdapter(context);
        adapter=new PlanMaintenanceAdapter(context);
        mPLVContractList.setAdapter(adapter);
        modlue=new TaskModlue(handler,context);

        mPLVContractList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task insp=(Task)adapter.getItem(position);
                Intent intent=new Intent("android.intent.action.PLANTASKDETAILACTIVITY");
                intent.putExtra("name",insp.getTaskName());
                intent.putExtra("taskId",insp.getTaskId());
                intent.putExtra("finish",true);
                intent.putExtra("contractId",insp.getContractId());
                intent.putExtra("contractName",insp.getContractName());
                startActivity(intent);

            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        modlue.getPlanMainFinishList(pageNo,pageSize,state);
    }

    private void listDataPaixunByDate() {
        Collections.sort(inspData,new ArrayListSortByDate());
    }

    private void initView() {
        pullToRefreshLayout=(PullToRefreshLayout)view.findViewById(R.id.prelat_layout);
        mPLVContractList=(PullableListView)view.findViewById(R.id.lv_pullreflash);
        pullToRefreshLayout.setOnRefreshListener(new MyListener());
    }

    private void showData(){
        if(pageNo==1){
            inspData.clear();
        }
        inspData.addAll(getData);
//        listDataPaixunByDate();
        adapter.updata(inspData);
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

    private void stopPullToRefreshLayoutFiled(){
        if(isreflash){
            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
        }
        if(isloadmore){
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
        }
    }

    @Override
    public void resultData(Message msg) {
        super.resultData(msg);
        switch (msg.what){
            case TaskModlue.INT_GET_SPECIAL_LIST_SUCCESS:
                getData=(ArrayList<Task>)msg.obj;
                showData();
                break;
            case TaskModlue.INT_GET_SPECIAL_LIST_FILED:
                toLoginActivity(msg);
                stopPullToRefreshLayoutFiled();
                break;
        }
    }

    //PullableListView下拉上拉监听
    public class MyListener implements PullToRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
            isreflash=true;
            pageNo=1;
            modlue.getPlanMainFinishList(pageNo,pageSize,state);
        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            isloadmore=true;
            if(getData.size()<pageSize){
                Toast.makeText(context,"已加载所有数据",Toast.LENGTH_SHORT).show();
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }else {
                pageNo++;
                modlue.getPlanMainFinishList(pageNo,pageSize,state);
            }
        }

    }
}
