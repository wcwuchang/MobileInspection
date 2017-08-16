package com.tianjin.MobileInspection.activity.todo_task;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.socks.library.KLog;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.entity.Approve;
import com.tianjin.MobileInspection.entity.InspectionDetail;
import com.tianjin.MobileInspection.entity.Todo;
import com.tianjin.MobileInspection.main.BaseActivity;
import com.tianjin.MobileInspection.module.ApproveModule;
import com.tianjin.MobileInspection.module.ContractModule;
import com.tianjin.MobileInspection.module.TodoModule;
import com.tianjin.MobileInspection.until.ConnectionURL;
import com.tianjin.MobileInspection.until.DateUtils;
import com.tianjin.MobileInspection.until.FlowUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wuchang on 2016-12-9.
 *
 * 待办巡检详情
 */
public class TodoInspectionDetailActivity extends BaseActivity {

    private final int INT_CONTRACT_NAME_REQUEST = 0x89;
    private final int INT_UNIT_NAME_REQUEST = 0x88;
    private final int INT_ROAD_NAME_REQUEST = 0x87;
    private final int INT_TRAFFIC_NAME_REQUEST = 0x86;
    private final int INT_WORKER_NAME_REQUEST = 0x85;

    private LinearLayout mlinearBack;
    private TextView mtvTitle;
    private TextView mtvApprove;

    private TextView mtvContractName;
    private TextView mtvUntileName;
    private TextView mtvInspectionSize;
    private TextView mtvTraffic;
    private TextView mtvWorker;
    private TextView mtvInspectionDate;

    private EditText medtContent;
    private EditText medtInspectionName;

    private TodoModule module;
    private Intent intent;
    private String taskId;
    private Todo todo;
    private String options;
    private String approveId;
    private ContractModule contractmodule;
    private com.tianjin.MobileInspection.entity.InspectionDetail detail;

    private LinearLayout mlinearApprove;
    private Button mbtnTrack;
    private Button mbtnApprove;

    //审批
    private Dialog dialog;
    private View dialogview;
    private Button dialogCancel;
    private Button dialogQualified;
    private Button dialogNotQualified;
    private String dialogMsg;

    private ApproveModule approveModule;
    private String taskDefKey="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_inspection_detail);
        initView();
        initData();
    }

    private void initData() {
        intent=getIntent();
        taskId=intent.getStringExtra("id");
        module=new TodoModule(handler,this);
        todo=(Todo)intent.getSerializableExtra("todo");

        contractmodule=new ContractModule(handler,this);
        module.getGormUrl(todo.getTaskId(),todo.getTaskName(),todo.getTaskDefKey(),todo.getProcInsId(),todo.getProcDefId());
    }

    private void initView() {
        mlinearBack = (LinearLayout) findViewById(R.id.linear_return_back);
        mtvTitle = (TextView) findViewById(R.id.tv_activity_title);

        mtvContractName = (TextView) findViewById(R.id.tv_new_inspection_contract_name);
        mtvUntileName = (TextView) findViewById(R.id.tv_new_inspection_unitname);
        mtvInspectionSize = (TextView) findViewById(R.id.tv_new_inspection_size);
        mtvTraffic = (TextView) findViewById(R.id.tv_new_inspection_traffic);
        mtvWorker = (TextView) findViewById(R.id.tv_new_inspection_worker);
        mtvInspectionDate = (TextView) findViewById(R.id.tv_new_inspection_date);

        medtContent = (EditText) findViewById(R.id.edt_new_inspection_content);
        medtInspectionName=(EditText)findViewById(R.id.edt_new_inspection_name);
        mlinearApprove=(LinearLayout)findViewById(R.id.linear_approve);
        mbtnTrack=(Button)findViewById(R.id.btn_show_track);
        mbtnApprove=(Button)findViewById(R.id.btn_approve);

        mlinearApprove.setVisibility(View.GONE);

        mtvTitle.setText("巡检详情");
        mlinearBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.linear_return_back:
                finish();
                break;
            case R.id.btn_show_track:
                Intent rnt=new Intent("android.intent.action.TRACKPLAYBACKACTIVITY");
                rnt.putExtra("id",detail.getInspectionId());
                rnt.putExtra("starttime",Integer.parseInt(DateUtils.getTimeToStamp(detail.getStartTime())));
                rnt.putExtra("endtime",Integer.parseInt(DateUtils.getTimeToStamp(detail.getEndTime())));
                if(detail!=null){
                    rnt.putExtra("traffic",detail.getTraffic());
                }
                startActivity(rnt);
                break;
            case R.id.btn_approve:
                if(taskDefKey.equals("")) {
                    approveModule = new ApproveModule(handler, this);
                    approveModule.getApproveList(todo.getProcInsId());
                }else {
                    showCommitDialog();
                }
                break;
        }
    }

    private void showCommitDialog(){
        initDialogView();
        dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogview);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void initDialogView() {
        dialogview= LayoutInflater.from(this).inflate(R.layout.dialog_inspection_approve,null);
        dialogCancel=(Button)dialogview.findViewById(R.id.btn_cancel);
        dialogQualified=(Button)dialogview.findViewById(R.id.btn_qualified);
        dialogNotQualified=(Button)dialogview.findViewById(R.id.btn_not_qualified);

        dialogCancel.setOnClickListener(dialogListener);
        dialogQualified.setOnClickListener(dialogListener);
        dialogNotQualified.setOnClickListener(dialogListener);
    }


    @Override
    public void resultData(Message msg) {
        super.resultData(msg);
        switch (msg.what){
            case TodoModule.INT_GET_FORM_URL_SUCCESS:
                String jsessionid = MyApplication.getStringSharedPreferences("JSESSIONID", "");
                String url=msg.obj.toString();
                String[] str=url.split("\\?");
                StringBuffer sb=new StringBuffer();
                sb.append(str[0]).append(";JSESSIONID=").append(jsessionid).append("?_ajax&").append(str[1]);
                KLog.d(url);
                module.getTodoInspectionDetail(sb.toString());
                break;
            case TodoModule.INT_GET_TODO_DETAIL_SUCCESS:
                detail=(InspectionDetail)msg.obj;
                showData();
                break;
            case ApproveModule.INT_COMMIT_APPROVE_FILED:
            case ApproveModule.INT_GET_APPROVE_FILED:
            case TodoModule.INT_GET_FORM_URL_FILED:
            case TodoModule.INT_GET_TODO_DETAIL_FILED:
                toLoginActivity(msg);
                break;
            case ApproveModule.INT_GET_APPROVE_SUCCESS:
                ArrayList<Approve> testdata=(ArrayList<Approve>)msg.obj;
                KLog.d(testdata.get(0).toString());
                taskDefKey=testdata.get(testdata.size()-1).getApproveId();
                showCommitDialog();
                break;
            case ApproveModule.INT_COMMIT_APPROVE_SUCCESS:
                if(dialog.isShowing()){
                    dialog.cancel();
                }
                Intent intent1=new Intent("android.intent.action.TODOACTIVITY");
                startActivity(intent1);
                finish();
                break;
//            case ContractModule.INT_CONTRACT_DETAIL_SUCCESS:
//                ContractManager cm=(ContractManager)msg.obj;
//                mtvUntileName.setText(cm.getUnitName());
//                unitId=cm.getUnitId();
//                roadlist=cm.getRoad();
//                break;
        }
    }

    private String contactId = "";

    private void showData(){
        medtInspectionName.setText(detail.getTitle());
        mtvContractName.setText(detail.getContractName());
        mtvTraffic.setText(FlowUtil.traffic(detail.getTraffic()));
        if(detail.getRoads().size()==0){
            mtvInspectionSize.setText("此任务未指明巡检的范围");
        }else {
            StringBuffer sb1 = new StringBuffer();
            sb1.append(detail.getRoads().get(0).getRoadName());
            for (int i = 1; i < detail.getRoads().size(); i++) {
                sb1.append(",").append(detail.getRoads().get(i).getRoadName());
            }
            mtvInspectionSize.setText(sb1.toString());
        }
        mtvUntileName.setText(detail.getUnitName());
        medtContent.setText(detail.getContent());
        mtvWorker.setText(detail.getContactName());
        mtvInspectionDate.setText(detail.getDate());
        approveId=detail.getInspectionId();
        options=detail.getOption();
        if(detail.getState().equals("3")){
            mlinearApprove.setVisibility(View.VISIBLE);
            mbtnTrack.setOnClickListener(this);
            mbtnApprove.setOnClickListener(this);
        }else {
            mlinearApprove.setVisibility(View.GONE);
        }
        KLog.d(detail.getOption());
        medtContent.setFocusable(false);
        medtInspectionName.setFocusable(false);
    }

    View.OnClickListener dialogListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_cancel:
                    if(dialog.isShowing())
                        dialog.cancel();
                    break;
                case R.id.btn_qualified:
                    approve("满意","yes");
                    break;
                case R.id.btn_not_qualified:
                    approve("不满意","no");
                    break;

            }
        }
    };

    private void approve(String msg,String yes){
        Map<String,String> mapr=new HashMap<String,String>();
        mapr.put("id",detail.getInspectionId());
        mapr.put("act.taskId",todo.getTaskId());
        mapr.put("act.procInsId",todo.getProcInsId());
        mapr.put("act.taskDefKey",taskDefKey);
        mapr.put("act.comment",msg);
        mapr.put("act.flag",yes);
        approveModule.approveCommit(ConnectionURL.STR_INSPECTION_APPROVE_COMMIT,mapr);
    }
}
