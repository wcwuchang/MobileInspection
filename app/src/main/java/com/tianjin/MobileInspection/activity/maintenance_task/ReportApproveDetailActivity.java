package com.tianjin.MobileInspection.activity.maintenance_task;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.socks.library.KLog;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.adapter.FujianAdapter;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.customview.ScrollListView;
import com.tianjin.MobileInspection.entity.Approve;
import com.tianjin.MobileInspection.entity.Task;
import com.tianjin.MobileInspection.entity.Todo;
import com.tianjin.MobileInspection.main.BaseActivity;
import com.tianjin.MobileInspection.module.ApproveModule;
import com.tianjin.MobileInspection.module.MaintanenceModule;
import com.tianjin.MobileInspection.module.TaskModlue;
import com.tianjin.MobileInspection.module.TodoModule;
import com.tianjin.MobileInspection.until.ConnectionURL;
import com.tianjin.MobileInspection.until.DownLoadImageTask;
import com.tianjin.MobileInspection.until.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wuchang on 2016-12-16.
 *
 * 待办上报方案详情
 */
public class ReportApproveDetailActivity extends BaseActivity{

    private final int INT_WORKER_NAME_REQUEST = 0x85;
    private LinearLayout mlinearBack;
    private TextView mtvTitle;
    private LinearLayout mlinearSave;
    private TextView mtvSave;

    private Intent intent;
    private Todo todo;

    private TodoModule module;

    private EditText medtContent;
    private ScrollListView mslvFuj;

    private Button mbtnNo;
    private Button mbtnYes;
    private LinearLayout mlinearStockNum;
    private TextView mtvStockName;
    private TextView medtStockNum;
    private TextView mtvUnit;

    private Task task;
    private FujianAdapter fujianadapter;

    private String taskDefKey="";
    private String commit;
    private String flag="";
    private ApproveModule approveModule;
    String taskId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_approve);
        initView();
        initData();
    }

    private void initData() {
        approveModule=new ApproveModule(handler,this);
        module=new TodoModule(handler,this);
        fujianadapter=new FujianAdapter(this);
        mslvFuj.setAdapter(fujianadapter);
        intent=getIntent();
        todo=(Todo)intent.getExtras().getSerializable("todo");
        taskId=intent.getStringExtra("taskId");
//        module.getGormUrl(todo.getTaskId(),todo.getTaskName(),todo.getTaskDefKey(),todo.getProcInsId(),todo.getProcDefId());
        MaintanenceModule.getReportDetail(handler,this,taskId);
    }

    private void initView() {
        mlinearBack=(LinearLayout)findViewById(R.id.linear_return_back);
        mlinearSave=(LinearLayout)findViewById(R.id.linear_save);
        mtvTitle=(TextView)findViewById(R.id.tv_activity_title);
        mtvSave=(TextView)findViewById(R.id.tv_activity_mun);

        medtContent=(EditText)findViewById(R.id.edt_report);
        mslvFuj=(ScrollListView)findViewById(R.id.lv_file);

        mlinearStockNum=(LinearLayout)findViewById(R.id.linear_hidden_trouble_num);
        mtvStockName=(TextView)findViewById(R.id.tv_yh_detail_name);
        medtStockNum=(TextView) findViewById(R.id.edt_yh_detail_size);
        mtvUnit=(TextView)findViewById(R.id.tv_yh_detail_unit);

        mbtnNo=(Button)findViewById(R.id.btn_no);
        mbtnYes=(Button)findViewById(R.id.btn_yes);

        medtContent.setFocusable(false);

        mtvTitle.setText("方案详情");
        mtvSave.setText("");
        mlinearBack.setOnClickListener(this);
        mbtnNo.setOnClickListener(this);
        mbtnYes.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.linear_return_back:
                finish();
                break;
            case R.id.btn_no:
                commit="不通过";
                flag="no";
                getgetApproveList();
                break;
            case R.id.btn_yes:
                commit="通过";
                flag="yes";
                getgetApproveList();
                break;

        }
    }

    private void getgetApproveList(){
        if(!taskDefKey.equals("")){
            setHiddenApprove(commit,flag);
        }else {
            approveModule.getApproveList(task.getActprocInsId());
        }
    }

    private void setHiddenApprove(String msg,String flag){
        Map<String,String> mapr=new HashMap<String,String>();
        mapr.put("id",task.getTaskId());
        mapr.put("act.taskId",task.getActtaskId());
        mapr.put("act.procInsId",task.getActprocInsId());
        mapr.put("act.taskDefKey",taskDefKey);
        mapr.put("act.comment",msg);
        mapr.put("act.flag",flag);
        approveModule.approveCommit(ConnectionURL.STR_MAINTENANCE_APPROVE,mapr);
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
            case MaintanenceModule.INT_GET_SPECIAL_DETAIL_SUCCESS:
                task=(Task)msg.obj;
                showData();
                break;
            case TaskModlue.INT_GET_DEILAY_LIST_FILED:
            case TodoModule.INT_GET_SPECIAL_DETAIL_FILED:
            case TodoModule.INT_GET_FORM_URL_FILED:
            case TaskModlue.INT_SPECIAL_HAS_REPORTED_FALSE:
                toLoginActivity(msg);
                break;
            case DownLoadImageTask.DOWN_LOAD_SUCCESS:
                Toast.makeText(this,"下载成功",Toast.LENGTH_SHORT).show();
                FileUtil.openFile(getContext(),msg.obj.toString());
                break;
            case DownLoadImageTask.DOWN_LOAD_FILED:
                Toast.makeText(this,"下载失败",Toast.LENGTH_SHORT).show();
                break;
            case ApproveModule.INT_COMMIT_APPROVE_FILED:
            case TodoModule.COMMIT_METERAGE_FILED:
                toLoginActivity(msg);
                break;
            case ApproveModule.INT_GET_APPROVE_SUCCESS:
                ArrayList<Approve> testdata=(ArrayList<Approve>)msg.obj;
                taskDefKey=testdata.get(testdata.size()-1).getApproveId();
                KLog.d(taskDefKey);
                setHiddenApprove(commit,flag);
                break;
            case ApproveModule.INT_COMMIT_APPROVE_SUCCESS:
                Intent intent1 = new Intent("android.intent.action.TODOACTIVITY");
                startActivity(intent1);
                finish();
                break;
        }
    }

    private void showData() {
        if(task==null)return;
        mtvStockName.setText(task.getHiddenTroubleDetail().getStockName());
        medtStockNum.setText(task.getHiddenTroubleDetail().getQuantity());
        mtvUnit.setText(task.getHiddenTroubleDetail().getUnit());

        medtContent.setText(task.getTaskContent());

        fujianadapter.updata(task.getFujian());

        mslvFuj.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                download(position);
            }
        });


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
                            DownLoadImageTask task = new DownLoadImageTask(ReportApproveDetailActivity.this,
                                    handler,
                                    filename);
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
