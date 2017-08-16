package com.tianjin.MobileInspection.activity.todo_task;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.tianjin.MobileInspection.adapter.FujianAdapter;
import com.tianjin.MobileInspection.adapter.HiddenTroublesAddAdapter;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.customview.dialog.CustomDialog;
import com.tianjin.MobileInspection.customview.NobarGridView;
import com.tianjin.MobileInspection.customview.ScrollListView;
import com.tianjin.MobileInspection.entity.HiddenTroubleDetail;
import com.tianjin.MobileInspection.entity.Task;
import com.tianjin.MobileInspection.entity.Todo;
import com.tianjin.MobileInspection.main.BaseActivity;
import com.tianjin.MobileInspection.module.TaskModlue;
import com.tianjin.MobileInspection.until.CheckUntil;
import com.tianjin.MobileInspection.until.ConnectionURL;
import com.tianjin.MobileInspection.until.DownLoadImageTask;
import com.tianjin.MobileInspection.until.FileUtil;

import java.io.File;
import java.io.Serializable;

/**
 * Created by wuchang on 2016-12-16.
 *
 * 计划维修详情
 */
public class TodoPlanTaskDetailActivity extends BaseActivity{

    private LinearLayout mlinearBack;
    private TextView mtvTitle;
    private LinearLayout mlinearSave;
    private TextView mtvSave;

    private TextView mtvDetailContent;
    private TextView mtvContractName;
    private TextView mtvTroubleName;
    private TextView mtvContact;
    private TextView mtvDate;
    private LinearLayout mlinearTrouble;

    private LinearLayout mlinearStoack;
    private TextView mtvStockName;
    private TextView mtvStockNum;
    private TextView mtvStockUnit;

    private LinearLayout mlinearJL;
    private TextView mtvJlM;

    private NobarGridView mgvDeailyImg;
    private ListView mlvTroubles;
    private ListView mlvFujian;

    private Intent intent;
    private String deilayTaskId;
    private String deilayTaskName;
    private TaskModlue modlue;
    private Task task;

    private HiddenTroublesAddAdapter addadapter;
    private FujianAdapter fujianadapter;
    private CustomDialog downLoadDialog;

    private Button mbtnReport;
    private Button mbtnCommit;
    private Button mbtnReportDetail;
    private Button mbtnCommitDetail;

    private ScrollListView mslvMaintenance;
    private HiddenTroublesAddAdapter adapter;

    private Todo todo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_main_detail);
        initView();
        initData();
    }

    private void initData() {
        modlue=new TaskModlue(handler,this);
        intent=getIntent();
        todo=(Todo)intent.getExtras().getSerializable("todo");
            deilayTaskId=todo.getTaskId();
            mtvContractName.setText(deilayTaskName);
            boolean finish=intent.getBooleanExtra("finish",false);
            if(!finish){
                mlinearSave.setVisibility(View.GONE);
            }else {
                mlinearSave.setOnClickListener(this);
            }
        modlue.getPlanMainDetail(deilayTaskId);
        downLoadDialog=new CustomDialog(this,"正在下载");
        downLoadDialog.setCancelable(false);
        downLoadDialog.setCanceledOnTouchOutside(false);

        adapter=new HiddenTroublesAddAdapter(this);
        mslvMaintenance.setAdapter(adapter);
    }

    private void initView() {
        mlinearBack=(LinearLayout)findViewById(R.id.linear_return_back);
        mlinearSave=(LinearLayout)findViewById(R.id.linear_save);
        mtvTitle=(TextView)findViewById(R.id.tv_activity_title);
        mtvSave=(TextView)findViewById(R.id.tv_activity_mun);

        mtvDetailContent=(TextView)findViewById(R.id.tv_deaily_detail_content);
        mtvContractName=(TextView)findViewById(R.id.tv_deaily_detail_contract_name);
        mtvTroubleName=(TextView)findViewById(R.id.tv_deaily_detail_trouble_name);
        mtvContact=(TextView)findViewById(R.id.tv_deaily_detail_contact_name);
        mtvDate=(TextView)findViewById(R.id.tv_deaily_detail_location_name);

        mlinearStoack=(LinearLayout)findViewById(R.id.linear_stock);
        mtvStockName=(TextView)findViewById(R.id.tv_stoack);
        mtvStockNum=(TextView)findViewById(R.id.tv_stock_num);
        mtvStockUnit=(TextView)findViewById(R.id.tv_stock_unit);

        mgvDeailyImg=(NobarGridView)findViewById(R.id.gv_deaily_detail_img);
        mlvTroubles=(ListView)findViewById(R.id.lv_trouble_list);
        mlvFujian=(ListView)findViewById(R.id.lv_fujian_list);
        mlinearTrouble=(LinearLayout)findViewById(R.id.linear_deilay_trouble) ;

        mlinearJL=(LinearLayout)findViewById(R.id.linear_jiliang);
        mtvJlM=(TextView)findViewById(R.id.tv_deaily_detail_jl_month);

        mbtnCommit=(Button)findViewById(R.id.btn_commit);
        mbtnReport=(Button)findViewById(R.id.btn_report);
        mbtnCommitDetail=(Button)findViewById(R.id.btn_commit_detail);
        mbtnReportDetail=(Button)findViewById(R.id.btn_report_detail);

        mslvMaintenance=(ScrollListView)findViewById(R.id.lv_maintenance);

        mtvTitle.setText("计划维修任务详情");
        mlinearBack.setOnClickListener(this);
        mlinearTrouble.setOnClickListener(this);
        mlinearTrouble.setVisibility(View.GONE);
        mlinearStoack.setVisibility(View.GONE);
        mlinearJL.setVisibility(View.GONE);
        findViewById(R.id.linear_deaily_detail_location).setVisibility(View.GONE);
        mbtnReport.setOnClickListener(this);
        mbtnCommit.setOnClickListener(this);
        mbtnReportDetail.setOnClickListener(this);
        mbtnCommitDetail.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.linear_return_back:
                finish();
                break;
            case R.id.linear_save:
                Intent upload = new Intent("android.intent.action.MAINTENANCEUPLOADACTIVITY");
                upload.putExtra("id", deilayTaskId);
                upload.putExtra("title",task.getTaskName());
                upload.putExtra("procInsId", "");
                upload.putExtra("options", "");
                upload.putExtra("taskId", "");
                upload.putExtra("todo",true);
                startActivity(upload);
                break;
            case R.id.linear_deilay_trouble:
                if(task!=null) {
                    Intent trouble = new Intent("android.intent.action.MAINTANENCEHIDDENDETAILACTIVITY");
                    trouble.putExtra("id", task.getHiddenTroubleDetail().getTroubleId());
                    startActivity(trouble);
                }
                break;
            case R.id.btn_commit:
                if(!checkPower()) return;
                Intent intent3=new Intent("android.intent.action.PLANMAINTENANCEUPLOADACTIVITY");
                intent3.putExtra("taskId",task.getTaskId());
                intent3.putExtra("stockId",task.getHiddenTroubleDetail().getStockId());
                intent3.putExtra("stockName",task.getHiddenTroubleDetail().getStockName());
                intent3.putExtra("quantity",task.getHiddenTroubleDetail().getQuantity());
                intent3.putExtra("unit",task.getHiddenTroubleDetail().getUnit());
                intent3.putExtra("procInsId", "");
                intent3.putExtra("options", "");
                intent3.putExtra("todo",true);
                Bundle bundle=new Bundle();
                bundle.putSerializable("hidden",(Serializable)task.getHidden());
                intent3.putExtras(bundle);
                KLog.d(true+"");
                startActivity(intent3);
                break;
            case R.id.btn_report:
                if(!checkPower()) return;
                Intent intent2=new Intent("android.intent.action.REPORTINGSCHEMEACTIVITY");
                intent2.putExtra("taskId",task.getTaskId());
                startActivity(intent2);
                break;
            case R.id.btn_commit_detail:
//                if(!checkPower()) return;
                Intent inte=new Intent("android.intent.action.PLANMAINTENANCEUPLOADACTIVITY");
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
                Bundle bundle1=new Bundle();
                bundle1.putSerializable("hidden",(Serializable)task.getHidden());
                inte.putExtras(bundle1);
                KLog.d(false+"");
                startActivity(inte);
                break;
            case R.id.btn_report_detail:
//                if(!checkPower()) return;
                Intent intent1=new Intent("android.intent.action.REPORTAPPROVEDETAILACTIVITY");
                intent1.putExtra("taskId",task.getTaskId());
                startActivity(intent1);
                break;

        }
    }


    private boolean checkPower(){
        String userId=MyApplication.getStringSharedPreferences("userId","");
        String powerId=task.getContactId();
        if(userId.equals(powerId)){
            return true;
        }else {
            Toast.makeText(this,"您无权限操作此任务",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void resultData(Message msg) {
        super.resultData(msg);
        switch (msg.what){
            case TaskModlue.INT_DEILAY_DETAIL_SUCCESS:
                task=(Task)msg.obj;
                showData();
                break;
            case TaskModlue.INT_DEILAY_DETAIL_FILED:
                toLoginActivity(msg);
                break;
            case DownLoadImageTask.DOWN_LOAD_SUCCESS:
                Toast.makeText(this,"下载成功",Toast.LENGTH_SHORT).show();
                String path=msg.obj.toString();
                FileUtil.openFile(getContext(),path);
                break;
            case DownLoadImageTask.DOWN_LOAD_FILED:
                Toast.makeText(this,"下载失败",Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private void showData() {
        if(task==null) return;
        mtvDate.setText(task.getDate());
        mtvDetailContent.setText(task.getTaskContent());
        KLog.d(intent.getStringExtra("contractName"));
        mtvContractName.setText(intent.getStringExtra("contractName"));
        mtvTroubleName.setText(task.getHiddenTroubleDetail().getTitle());
        mtvContact.setText(task.getContactName());

        HiddenTroubleDetail hidden=task.getHiddenTroubleDetail();
        if(CheckUntil.isNull(hidden.getStockId())){
            mlinearStoack.setVisibility(View.GONE);
            mtvTroubleName.setText(hidden.getTypeName()+"-"+hidden.getName());
        }else {
            mlinearStoack.setVisibility(View.VISIBLE);
            mtvStockName.setText(hidden.getStockName());
            mtvStockNum.setText(hidden.getQuantity());
            mtvStockUnit.setText(hidden.getUnit());
            mtvTroubleName.setText(hidden.getTypeName()+"-"+hidden.getName()+"-"+hidden.getStockName());
        }

        if(task.getYearMonth()!=0) {
            mtvJlM.setText(task.getYearMonth() + "");
        }
        KLog.d(task.getMaintenanceState()+"");
        if(task.getMaintenanceState()==0){
            mbtnReport.setVisibility(View.VISIBLE);
        }else if(task.getMaintenanceState()==1||task.getMaintenanceState()==4||task.getMaintenanceState()==2){
            mbtnCommit.setVisibility(View.VISIBLE);
        }else if(task.getMaintenanceState()==3){
            mbtnCommitDetail.setVisibility(View.VISIBLE);
        }

        adapter.updata(task.getHidden());
    }


    private void download(final int position){
        final String filename=FileUtil.getFileNameForUrl(fujianadapter.getItem(position).toString());
        File file=new File(MyApplication.DOWNLOAD_PATH,filename);
        if(file.exists()){
            FileUtil.openFile(getContext(),file.getPath());
        }else {
            AlertDialog.Builder tuichuDialog = new AlertDialog.Builder(this).setMessage("下载附件？");
            tuichuDialog.setPositiveButton("下载",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DownLoadImageTask task = new DownLoadImageTask(TodoPlanTaskDetailActivity.this, handler, filename);
                            StringBuffer sb = new StringBuffer();
                            sb.append(ConnectionURL.HTTP_SERVER_IMAGE).append(fujianadapter.getItem(position).toString());
                            task.execute(sb.toString());
                        }
                    });
            tuichuDialog.setNegativeButton("取消", null);
            tuichuDialog.show();
        }
    }
}
