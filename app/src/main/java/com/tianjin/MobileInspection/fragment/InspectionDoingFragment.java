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
import com.tianjin.MobileInspection.adapter.InspectionListAdapter;
import com.tianjin.MobileInspection.customview.pullReflashView.PullToRefreshLayout;
import com.tianjin.MobileInspection.customview.pullReflashView.PullableListView;
import com.tianjin.MobileInspection.entity.InspectionChoose;
import com.tianjin.MobileInspection.module.InspectionModule;

import java.util.ArrayList;

/**
 * xunjian
 * Created by wuchang on 2016-11-21.
 */
public class InspectionDoingFragment extends BaseFragment {

    private final static int INT_HAS_LOAD_ALL_DATA=0x567;
    private String TAG="InspectionDoingFragment";
    private View view;
    private PullToRefreshLayout pullToRefreshLayout;
    private PullableListView mPLVContractList;

    private ArrayList<InspectionChoose> inspData;
    private ArrayList<InspectionChoose> getData;
    private InspectionListAdapter adapter;
    private InspectionModule module;

    private int pageNo=1;
    private int pageSize=20;
    private String state="2";
    private boolean isreflash=false;
    private boolean isloadmore=false;


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
        inspData=new ArrayList<InspectionChoose>();
        getData=new ArrayList<InspectionChoose>();
        adapter = new InspectionListAdapter(context);
        mPLVContractList.setAdapter(adapter);
        adapter.updata(inspData);
        module=new InspectionModule(handler,context);
        module.getInspectionList(String.valueOf(pageNo),String.valueOf(pageSize),state);

        mPLVContractList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelect(position);
                InspectionChoose insp=(InspectionChoose)adapter.getItem(position);
                Intent intent=new Intent("android.intent.action.INSPECTIONDETAIL");
                intent.putExtra("name",insp.getInspectionName());
                intent.putExtra("inspectionId",insp.getInspectionId());
                intent.putExtra("entryName",insp.getPerson()+insp.getPersonId());
                intent.putExtra("isFinish",false);
                startActivity(intent);

            }
        });
    }

    private void initView() {
        pullToRefreshLayout=(PullToRefreshLayout)view.findViewById(R.id.prelat_layout);
        mPLVContractList=(PullableListView)view.findViewById(R.id.lv_pullreflash);
        pullToRefreshLayout.setOnRefreshListener(new MyListener());
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

    private void showData(){
        if(pageNo==1){
            inspData.clear();
        }
        inspData.addAll(getData);
        adapter.updata(inspData);
        stopPullToRefreshLayoutSuccess();
    }

    @Override
    public void resultData(Message msg) {
        super.resultData(msg);
        switch (msg.what){
            case INT_HAS_LOAD_ALL_DATA:
                stopPullToRefreshLayoutSuccess();
                break;
            case InspectionModule.INT_GET_INSPECTION_CHOSE_SUCCESS:
                getData=(ArrayList<InspectionChoose>)msg.obj;
                showData();
                break;
            case InspectionModule.INT_GET_INSPECTION_CHOSE_FILED:
                toLoginActivity(msg);
                stopPullToRefreshLayoutFiled();
                break;
        }
    }

    //PullableListView下拉上拉监听
    public class MyListener implements PullToRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
            // 下拉刷新操作
            isreflash=true;
            pageNo=1;
            module.getInspectionList(String.valueOf(pageNo),String.valueOf(pageSize),state);
        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            //上拉加载操作
            isloadmore=true;
            if(getData.size()<pageSize){
                Toast.makeText(context,"已加载所有数据",Toast.LENGTH_SHORT).show();
                handler.sendEmptyMessage(INT_HAS_LOAD_ALL_DATA);
            }else {
                pageNo++;
                module.getInspectionList(String.valueOf(pageNo),String.valueOf(pageSize),state);
            }
        }

    }
}
