package com.tianjin.MobileInspection.activity.contract;

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

import com.socks.library.KLog;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.adapter.ContractManagerAdapter;
import com.tianjin.MobileInspection.customview.pullReflashView.PullToRefreshLayout;
import com.tianjin.MobileInspection.customview.pullReflashView.PullableListView;
import com.tianjin.MobileInspection.entity.ContractManager;
import com.tianjin.MobileInspection.main.BaseActivity;
import com.tianjin.MobileInspection.module.ContractModule;

import java.util.ArrayList;
import java.util.List;

/**
 * 合同管理
 * Created by wuchang on 2016-11-21.
 */
public class ContractManageActivity extends BaseActivity {

    private final static int INT_REFRESH_ALL=0x4563;

    private PullToRefreshLayout pullToRefreshLayout;
    private PullableListView mPLVContractList;

    private LinearLayout mlinearBack;
    private TextView mtvTitle;
    private LinearLayout mlinearAdd;

    private List<ContractManager> contractList;
    private List<ContractManager> getForService;
    private ContractManagerAdapter adapter;

    private ContractModule module;
    private int pageNo=1;
    private int pageSize=7;
    private boolean refresh=false;
    private boolean loadmore=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_manage);
        initView();
        initData();
    }

    private void initData() {
        mtvTitle.setText(getString(R.string.str_contract_manage_title));
        getForService=new ArrayList<ContractManager>();
        contractList=new ArrayList<ContractManager>();
        adapter=new ContractManagerAdapter(this,this);
        mPLVContractList.setAdapter(adapter);
        adapter.updata(contractList);
        module=new ContractModule(handler,this);

        mPLVContractList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                KLog.d(position+"");
                ContractManager cm=(ContractManager)adapter.getItem(position);
                if(cm.getType()==1) {
                    Intent intent = new Intent("android.intent.action.CONTRACTDETAIL");
                    intent.putExtra("id", cm.getContractId());
                    intent.putExtra("type", cm.getType());
                    startActivity(intent);
                }else {
                    AlertDialog.Builder tuichuDialog = new AlertDialog.Builder(ContractManageActivity.this).setMessage("抱歉，维修数据尚在整理中，暂未开放！");
                    tuichuDialog.setPositiveButton("知道了",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    tuichuDialog.show();
                }
            }
        });
    }



    private void initView() {
        mlinearBack=(LinearLayout)findViewById(R.id.linear_return_back);
        mlinearAdd=(LinearLayout)findViewById(R.id.linear_add);
        mtvTitle=(TextView)findViewById(R.id.tv_activity_title);

        pullToRefreshLayout=(PullToRefreshLayout)findViewById(R.id.prelat_layout);
        mPLVContractList=(PullableListView)findViewById(R.id.lv_pullreflash);
        pullToRefreshLayout.setOnRefreshListener(new MyListener());
        mlinearBack.setOnClickListener(this);
//        mlinearAdd.setVisibility(View.VISIBLE);
        mlinearAdd.setOnClickListener(this);
    }

    public void showDetail(int position){

    }

    @Override
    protected void onResume() {
        super.onResume();
        module.getContractList(pageNo,pageSize);
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
            case ContractModule.INT_CONTRACT_LIST_SUCCESS:
                getForService=(ArrayList<ContractManager>)msg.obj;
                showData(getForService);
                break;
            case ContractModule.INT_CONTRACT_LIST_FILED:
                if(msg.arg1==0){
                    Toast.makeText(this,"登陆超时请重新登陆",Toast.LENGTH_SHORT).show();
                    retuenTologin();
                }else {
                    Toast.makeText(this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    pullToRefreshLayoutCancel();
                }
                break;
            case INT_REFRESH_ALL:
                if(loadmore){
                    Toast.makeText(ContractManageActivity.this,"已加载全部数据",Toast.LENGTH_SHORT).show();
                }
                pullToRefreshLayoutCancel();
                break;

        }
    }

    private void showData(List<ContractManager> getForService) {
        if(pageNo==1){
            contractList.clear();
        }
        contractList.addAll(getForService);
        adapter.updata(contractList);
        pullToRefreshLayoutCancel();
    }

    private void pullToRefreshLayoutCancel(){
        if(loadmore){
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            loadmore=false;
        }
        if(refresh){
            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            refresh=false;
        }
    }

    //PullableListView下拉上拉监听
    public class MyListener implements PullToRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
            refresh=true;
            pageNo=1;
            getForService.clear();
            module.getContractList(pageNo,pageSize);
        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            loadmore=true;
            if (getForService.size()<pageSize){
                handler.sendEmptyMessageDelayed(INT_REFRESH_ALL,500);
            }else{
                getForService.clear();
                pageNo++;
                module.getContractList(pageNo,pageSize);
            }
        }

    }

}
