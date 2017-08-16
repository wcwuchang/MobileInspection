package com.tianjin.MobileInspection.activity.approve;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.socks.library.KLog;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.adapter.ApproveListAdapter;
import com.tianjin.MobileInspection.entity.Approve;
import com.tianjin.MobileInspection.entity.InspectionDetail;
import com.tianjin.MobileInspection.main.BaseActivity;
import com.tianjin.MobileInspection.module.ApproveModule;
import com.tianjin.MobileInspection.until.CheckUntil;
import com.tianjin.MobileInspection.until.JsonAndMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 审批
 * Created by 吴昶 on 2016/12/19.
 */
public class InspectionApproveActivity extends BaseActivity {

    private LinearLayout mlinearBack;
    private TextView mtvTitle;
    private ListView mlvApprovelist;
    private ApproveListAdapter adapter;
    private List<Approve> testdata;
    private ApproveModule module;
    private Intent intent;
    private String procInsId;
    private String options;
    private LinearLayout mlinearOptions;
    private String taskId;
    private String taskDefKey;
    private String id;
    private String url;
    private Map<String,Object> map;
    private List<String> mapkey;

    private Dialog dialog;
    private View dialogview;
    private TextView dialogTitle;
    private EditText dialogCommont;
    private Button dialogCancel;
    private Button dialogCommit;
    private String dialogMsg;

    private String commitflog;
    private String contactId="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_approve);
        initView();
        initData();

    }

    private void initData() {
        adapter=new ApproveListAdapter(this);
        mlvApprovelist.setAdapter(adapter);
        testdata=new ArrayList<Approve>();
        module=new ApproveModule(handler,this);
        intent=getIntent();

        procInsId=intent.getStringExtra("procInsId");
        options=intent.getStringExtra("options");
        id=intent.getStringExtra("id");
        url=intent.getStringExtra("url");
        taskId=intent.getStringExtra("taskId");
        if(intent.hasExtra("maintain")){
            contactId=intent.getStringExtra("maintain");
        }else {
            contactId=null;
        }

        KLog.d(options);
        map= JsonAndMap.getMapForJson(options);
        mapkey=JsonAndMap.getMapKey(map);

        module.getApproveList(procInsId);
        for(int i=0;i<mapkey.size();i++){
            View view= LayoutInflater.from(this).inflate(R.layout.layout_approve_button,null);
            TextView button=(TextView)view.findViewById(R.id.tv_approve_1) ;
            button.setText(mapkey.get(i));
            button.setOnClickListener(listener);
            if(i%2==0){
                view.setBackgroundResource(R.drawable.select_approve_urgent);
            }else {
                view.setBackgroundResource(R.drawable.select_approve_daily);
            }
            mlinearOptions.addView(view);
        }
    }

    private void initView() {
        mlvApprovelist=(ListView)findViewById(R.id.lv_inspection_approve_list);
        mlinearBack=(LinearLayout)findViewById(R.id.linear_return_back);
        mtvTitle=(TextView)findViewById(R.id.tv_activity_title);
        mlinearOptions=(LinearLayout)findViewById(R.id.linear_inspection_approve_button);
        mtvTitle.setText("审批");
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
            case ApproveModule.INT_GET_APPROVE_SUCCESS:
                testdata=(ArrayList<Approve>)msg.obj;
                KLog.d(testdata.get(0).toString());
                adapter.updata(testdata);
                break;
            case ApproveModule.INT_COMMIT_APPROVE_SUCCESS:
                if(dialog.isShowing()){
                    dialog.cancel();
                }
                Intent intent1=new Intent("android.intent.action.TODOACTIVITY");
                startActivity(intent1);
                break;
            case ApproveModule.INT_COMMIT_APPROVE_FILED:
            case ApproveModule.INT_GET_APPROVE_FILED:
                toLoginActivity(msg);
                break;
        }
    }

    View.OnClickListener listener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            commitflog=((TextView)v).getText().toString();
            showCommitDialog(commitflog);
        }
    };

    private void showCommitDialog(String title){
        initDialogView();
        dialogMsg="请填写"+title+"的理由";
        dialogTitle.setText(dialogMsg);
        dialog=new Dialog(this);
        dialog.setContentView(dialogview);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void initDialogView() {
        dialogview=LayoutInflater.from(this).inflate(R.layout.dialog_approve,null);
        dialogTitle=(TextView)dialogview.findViewById(R.id.tv_dialog_title);
        dialogCommont=(EditText)dialogview.findViewById(R.id.edt_dialog_commont);
        dialogCancel=(Button)dialogview.findViewById(R.id.btn_dialog_cancel);
        dialogCommit=(Button)dialogview.findViewById(R.id.btn_dialog_commit);

        dialogCancel.setOnClickListener(dialogListener);
        dialogCommit.setOnClickListener(dialogListener);
    }

    View.OnClickListener dialogListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_dialog_cancel:
                    if(dialog.isShowing())
                        dialog.cancel();
                    break;
                case R.id.btn_dialog_commit:
                    if(CheckUntil.checkEditext(dialogCommont.getText()).equals("")){
                        Toast.makeText(InspectionApproveActivity.this,dialogMsg,Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Map<String,String> mapr=new HashMap<String,String>();
                    mapr.put("id",id);
                    mapr.put("act.taskId",taskId);
                    mapr.put("act.procInsId",procInsId);
                    mapr.put("act.taskDefKey",testdata.get(testdata.size()-1).getApproveId());
                    mapr.put("act.comment",dialogCommont.getText().toString());
                    mapr.put("act.flag",map.get(commitflog).toString());
                    if(intent.hasExtra("edit")) {
                        Bundle bundle = intent.getExtras();
                        InspectionDetail detail=(InspectionDetail)bundle.getSerializable("edit");
                        mapr.put("id",detail.getInspectionId());
                        mapr.put("title", detail.getInspectionName());
                        mapr.put("contractMain.id", detail.getContractId());
                        mapr.put("office.id", detail.getUnitId());
                        mapr.put("tuser.id", detail.getContactId());
                        mapr.put("inspectingType", detail.getTraffic());
                        mapr.put("inspectingTime", detail.getDate());
                        mapr.put("content", detail.getContent());
                        for (int i = 0; i < detail.getRoads().size(); i++) {
                            mapr.put("inspectingRangeList[" + i + "].road.id", detail.getRoads().get(i).getRoadName());
                        }
                    }
                    if(contactId!=null&&!contactId.equals("")){
                        mapr.put("tuser.id",contactId);
                    }

                    module.approveCommit(url,mapr);

                    break;

            }
        }
    };
}
