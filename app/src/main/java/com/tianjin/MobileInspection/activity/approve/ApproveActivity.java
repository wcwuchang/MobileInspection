package com.tianjin.MobileInspection.activity.approve;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.adapter.ApproveListAdapter;
import com.tianjin.MobileInspection.entity.Approve;
import com.tianjin.MobileInspection.main.BaseActivity;
import com.tianjin.MobileInspection.module.ApproveModule;

import java.util.ArrayList;
import java.util.List;

/**
 * 审批
 * Created by wuchang on 2016-12-12.
 */
public class ApproveActivity extends BaseActivity{

    private LinearLayout mlinearBack;
    private TextView mtvTitle;

    private ListView mlvApproveList;
    private TextView mtvUrgent;
    private TextView mtvDaily;

    private List<Approve> testdata;
    private ApproveListAdapter adapter;
    private ApproveModule module;
    private Intent intent;
    private String procInsId;
    private ArrayList<String> options;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve);
        initView();
        initDate();
    }

    private void initDate() {
        module=new ApproveModule(handler,this);
        testdata=new ArrayList<Approve>();
        testdata();
        adapter=new ApproveListAdapter(this);
        mlvApproveList.setAdapter(adapter);
        adapter.updata(testdata);
        intent=getIntent();
        procInsId=intent.getStringExtra("procInsId");
        options=intent.getStringArrayListExtra("options");
        module.getApproveList(procInsId);

    }

    private void initView() {
        mlinearBack=(LinearLayout)findViewById(R.id.linear_return_back);
        mtvTitle=(TextView)findViewById(R.id.tv_activity_title);

        mlvApproveList=(ListView)findViewById(R.id.lv_approve_down);

        mtvTitle.setText("审批");
        mlinearBack.setOnClickListener(this);
    }

    private void testdata() {
        for(int i=0;i<5;i++){
            Approve app=new Approve();
            app.setApproveDate("2016.12."+(i+1));
            app.setApproveContent("基本上搞定");
            testdata.add(app);
        }
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
            case ApproveModule.INT_GET_APPROVE_SUCCESS:
                testdata=(List<Approve>)msg.obj;
                adapter.updata(testdata);
                break;
        }
    }
}
