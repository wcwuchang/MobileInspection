package com.tianjin.MobileInspection.activity.todo_task;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.socks.library.KLog;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.adapter.MotorApproveAdapter;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.customview.dialog.ApproveCommitDialog;
import com.tianjin.MobileInspection.entity.Approve;
import com.tianjin.MobileInspection.entity.HiddenTroubleDetail;
import com.tianjin.MobileInspection.entity.Task;
import com.tianjin.MobileInspection.entity.Todo;
import com.tianjin.MobileInspection.main.BaseActivity;
import com.tianjin.MobileInspection.module.ApproveModule;
import com.tianjin.MobileInspection.module.TaskModlue;
import com.tianjin.MobileInspection.module.TodoModule;
import com.tianjin.MobileInspection.until.ConnectionURL;
import com.tianjin.MobileInspection.until.FlowUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wuchang on 2017-7-13.
 *
 * 待办维修
 */
public class TodoMotorDetailActivity extends BaseActivity implements ApproveCommitDialog.ApproveCommitListener{

    //控件
    private LinearLayout mlinearBack;
    private TextView mtvTitle;
    private LinearLayout mlinearSave;
    private TextView mtvSave;

    private TextView mtvContent;
    private TextView mtvNum;
    private TextView mtvUnit;
    private TextView mtvMan;
    private TextView mtvFinishTimel;

    private Button mbtnReported;
    private Button mbtnMaintenance;
    private Button mbtnNo;
    private Button mbtnYes;
    private Button mbtnReportSchme;
    private Button mbtnReportMaintenance;

    private ListView mlvFlowList;
    //初始化数据
    private TodoModule module;
    private ApproveModule approveModule;
    private TaskModlue modlue;
    private ArrayList<String> contact;
    private Intent intent;
    private Todo todo;
    private Task task;

    private MotorApproveAdapter motorAdapter;
    private ArrayList<Approve> approvedata;
    private String taskDefKey;

    private ApproveCommitDialog approveCommitDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);
        initView();
        initData();
    }

    private void initView() {
        mlinearBack=(LinearLayout)findViewById(R.id.linear_return_back);
        mlinearSave=(LinearLayout)findViewById(R.id.linear_save);
        mtvTitle=(TextView)findViewById(R.id.tv_activity_title);
        mtvSave=(TextView)findViewById(R.id.tv_activity_mun);

        mtvContent=(TextView)findViewById(R.id.tv_motor_content);
        mtvNum=(TextView)findViewById(R.id.tv_motor_num);
        mtvUnit=(TextView)findViewById(R.id.tv_motor_unit);
        mtvMan=(TextView)findViewById(R.id.tv_motor_man);
        mbtnMaintenance=(Button)findViewById(R.id.btn_motor_maintenance);
        mbtnReported=(Button)findViewById(R.id.btn_motor_scheme);

        mlvFlowList=(ListView)findViewById(R.id.lv_flow_list);

        mbtnNo=(Button)findViewById(R.id.btn_no);
        mbtnYes=(Button)findViewById(R.id.btn_yes);
        mbtnReportSchme=(Button)findViewById(R.id.btn_report_scheme);
        mbtnReportMaintenance=(Button)findViewById(R.id.btn_report_maintenance);

        mbtnReported.setOnClickListener(this);
        mbtnMaintenance.setOnClickListener(this);
        mbtnNo.setOnClickListener(this);
        mbtnYes.setOnClickListener(this);
        mlinearBack.setOnClickListener(this);
        mbtnReportMaintenance.setOnClickListener(this);
        mbtnReportSchme.setOnClickListener(this);

        motorAdapter=new MotorApproveAdapter(this);
        mlvFlowList.setAdapter(motorAdapter);

        mtvContent.setOnClickListener(this);
    }

    private void initData(){
        approveModule=new ApproveModule(handler,this);
        modlue=new TaskModlue(handler,this);
        intent=getIntent();
        todo=(Todo)intent.getExtras().getSerializable("todo");
        KLog.d((todo==null)+"");
        module=new TodoModule(handler,this);
        module.getGormUrl(todo.getTaskId(),todo.getTaskName(),todo.getTaskDefKey(),todo.getProcInsId(),todo.getProcDefId());
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.linear_return_back:
                finish();
                break;
            case R.id.btn_motor_maintenance:
                Intent inte=new Intent("android.intent.action.MAINTENANCEUPLOADACTIVITY");
                inte.putExtra("taskId",task.getTaskId());
                inte.putExtra("stockId",task.getHiddenTroubleDetail().getStockId());
                inte.putExtra("stockName",task.getHiddenTroubleDetail().getStockName());
                inte.putExtra("quantity",task.getHiddenTroubleDetail().getQuantity());
                inte.putExtra("unit",task.getHiddenTroubleDetail().getUnit());
                inte.putExtra("procInsId", "");
                inte.putExtra("options", "");
                inte.putExtra("todo",false);
                inte.putExtra("stockName",task.getHiddenTroubleDetail().getStockName());
                inte.putExtra("unit",task.getHiddenTroubleDetail().getUnit());
                if(task.getDate()==null||task.getDate().equals("")){
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("hidden",(Serializable)task.getHidden());
                    inte.putExtras(bundle);
                }
                startActivity(inte);
                break;
            case R.id.btn_motor_scheme:
                HiddenTroubleDetail hidden=task.getHiddenTroubleDetail();
                Intent intent1=new Intent(this, TodoReportApproveActivity.class);
                intent1.putExtra("stock",hidden.getStockName());
                intent1.putExtra("quantity",hidden.getQuantity());
                intent1.putExtra("unit",hidden.getUnit());
                intent1.putExtra("report",task.getReportContent());
                intent1.putStringArrayListExtra("fujian",task.getFujian());
                startActivity(intent1);
                break;
            case R.id.tv_motor_content:
                if(task!=null) {
                    Intent trouble = new Intent("android.intent.action.MAINTANENCEHIDDENDETAILACTIVITY");
                    trouble.putExtra("id", task.getHiddenTroubleDetail().getTroubleId());
                    startActivity(trouble);
                }
                break;
            case R.id.btn_no:
                if(approveCommitDialog==null){
                    approveCommitDialog=new ApproveCommitDialog(this);
                    approveCommitDialog.setListener(this);
                }
                approveCommitDialog.show();
                break;
            case R.id.btn_yes:
                setHiddenApprove("通过","yes");
                break;
            case R.id.btn_report_scheme:
                Intent intent2=new Intent("android.intent.action.REPORTINGSCHEMEACTIVITY");
                intent2.putExtra("taskId",task.getTaskId());
                startActivity(intent2);
                break;
            case R.id.btn_report_maintenance:
                Intent inte1=new Intent("android.intent.action.MAINTENANCEUPLOADACTIVITY");
                inte1.putExtra("taskId",task.getTaskId());
                inte1.putExtra("stockId",task.getHiddenTroubleDetail().getStockId());
                inte1.putExtra("stockName",task.getHiddenTroubleDetail().getStockName());
                inte1.putExtra("quantity",task.getHiddenTroubleDetail().getQuantity());
                inte1.putExtra("unit",task.getHiddenTroubleDetail().getUnit());
                inte1.putExtra("procInsId", "");
                inte1.putExtra("options", "");
                inte1.putExtra("todo",true);
                inte1.putExtra("stockName",task.getHiddenTroubleDetail().getStockName());
                inte1.putExtra("unit",task.getHiddenTroubleDetail().getUnit());
                startActivity(inte1);
                break;
        }
    }

    @Override
    public void resultData(Message msg) {
        super.resultData(msg);
        switch (msg.what){
            case TodoModule.INT_GET_FORM_URL_SUCCESS:
                String jsessionid = MyApplication.getStringSharedPreferences("JSESSIONID", "");
                KLog.d(jsessionid);
                String url=msg.obj.toString();
                String[] str=url.split("\\?");
                StringBuffer sb=new StringBuffer();
                sb.append(str[0]).append(";JSESSIONID=").append(jsessionid).append("?_ajax&").append(str[1]);
                KLog.d(url);
                module.getReportDetail(sb.toString());
                break;
            case TodoModule.INT_GET_SPECIAL_DETAIL_SUCCESS:
                task=(Task)msg.obj;
                showData();
                break;
            case ApproveModule.INT_GET_APPROVE_SUCCESS:
                ArrayList<Approve> approvedata=(ArrayList<Approve>)msg.obj;
                if(approvedata!=null&&approvedata.size()>0) {
                    taskDefKey = approvedata.get(approvedata.size() - 1).getApproveId();
                    motorAdapter.updata(approvedata);
                    int code= FlowUtil.todoFlowCode(approvedata.get(approvedata.size()-1).getApproveId());
                    if(code==4){
                        mbtnReportSchme.setVisibility(View.VISIBLE);
                    }else if(code==7){
                        mbtnReportMaintenance.setVisibility(View.VISIBLE);
                    }else if(code==1||code==2||code==3){

                    }else {
                        mbtnNo.setVisibility(View.VISIBLE);
                        mbtnYes.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case ApproveModule.INT_COMMIT_APPROVE_SUCCESS:
                Intent intent1 = new Intent("android.intent.action.TODOACTIVITY");
                startActivity(intent1);
                finish();
                break;
            case TodoModule.INT_GET_FORM_URL_FILED:
            case TodoModule.INT_GET_SPECIAL_DETAIL_FILED:
            case ApproveModule.INT_GET_APPROVE_FILED:
            case ApproveModule.INT_COMMIT_APPROVE_FILED:
                toLoginActivity(msg);
                break;
        }
    }

    private void showData() {
        if(task==null) return;
        HiddenTroubleDetail hidden=task.getHiddenTroubleDetail();
        mtvContent.setText(hidden.getTypeName()+"-"+hidden.getName()+"-"+hidden.getStockName());
        mtvNum.setText(hidden.getQuantity());
        mtvUnit.setText(hidden.getUnit());
        mtvMan.setText(task.getContactName());

        if(task.getReportContent()!=null&& !task.getReportContent().equals("")){
            mbtnReported.setVisibility(View.VISIBLE);
        }
        if(task.getMaintenanceState()==1||task.getMaintenanceState()==3){
            mbtnMaintenance.setVisibility(View.VISIBLE);
        }
        approveModule.getApproveList(task.getActprocInsId());
    }

    private void setHiddenApprove(String msg,String flag){
        Map<String,String> mapr=new HashMap<String,String>();
        mapr.put("id",task.getTaskId());
        mapr.put("act.taskId",task.getActtaskId());
        mapr.put("act.procInsId",task.getActprocInsId());
        mapr.put("act.taskDefKey",taskDefKey);
        mapr.put("act.comment",msg);
        mapr.put("act.flag",flag);
        if(task.getMaintenanceState()==2) {
            approveModule.approveCommit(ConnectionURL.STR_MAINTENANCE_APPROVE, mapr);
        }else {
            approveModule.approveCommit(ConnectionURL.STR_MAINTENANCE_MAIN_APPROVE, mapr);
        }
    }

    @Override
    public void ApproveCancel() {
        approveCommitDialog.cancel();
    }

    @Override
    public void ApproveCommit(String msg) {
        if(msg==null||msg.equals("")){
            Toast.makeText(this,"请输入不同意的理由",Toast.LENGTH_SHORT).show();
        }else {
            setHiddenApprove(msg,"no");
        }
    }
}
