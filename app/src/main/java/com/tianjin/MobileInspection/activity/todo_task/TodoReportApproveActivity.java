package com.tianjin.MobileInspection.activity.todo_task;

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

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.adapter.FujianAdapter;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.customview.ScrollListView;
import com.tianjin.MobileInspection.entity.Todo;
import com.tianjin.MobileInspection.main.BaseActivity;
import com.tianjin.MobileInspection.module.TodoModule;
import com.tianjin.MobileInspection.until.ConnectionURL;
import com.tianjin.MobileInspection.until.DownLoadImageTask;
import com.tianjin.MobileInspection.until.FileUtil;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by wuchang on 2016-12-16.
 *
 * 待办上报方案详情
 */
public class TodoReportApproveActivity extends BaseActivity{

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

    private LinearLayout mlinearOpp;
    private Button mbtnNo;
    private Button mbtnYes;
    private LinearLayout mlinearStockNum;
    private TextView mtvStockName;
    private TextView medtStockNum;
    private TextView mtvUnit;

//    private Task task;
    private FujianAdapter fujianadapter;

//    private String taskDefKey="";
//    private String commit;
//    private String flag="";
//    private ApproveModule approveModule;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_approve);
        initView();
        initData();
    }

    private void initData() {
//        approveModule=new ApproveModule(handler,this);
//        module=new TodoModule(handler,this);
        fujianadapter=new FujianAdapter(this);
        mslvFuj.setAdapter(fujianadapter);
        intent=getIntent();
        String stock=intent.getStringExtra("stock");
        String quantity=intent.getStringExtra("quantity");
        String unit=intent.getStringExtra("unit");
        String report=intent.getStringExtra("report");
        ArrayList<String> fujian=intent.getStringArrayListExtra("fujian");

        mtvStockName.setText(stock);
        medtStockNum.setText(quantity);
        mtvUnit.setText(unit);

        medtContent.setText(report);

        fujianadapter.updata(fujian);

        mslvFuj.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                download(position);
            }
        });
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

        mlinearOpp=(LinearLayout)findViewById(R.id.linear_opp);
        mlinearOpp.setVisibility(View.GONE);
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
        }
    }

//    private void getgetApproveList(){
//        if(!taskDefKey.equals("")){
//            setHiddenApprove(commit,flag);
//        }else {
//            approveModule.getApproveList(task.getActprocInsId());
//        }
//    }

//    private void setHiddenApprove(String msg,String flag){
//        Map<String,String> mapr=new HashMap<String,String>();
//        mapr.put("id",task.getTaskId());
//        mapr.put("act.taskId",task.getActtaskId());
//        mapr.put("act.procInsId",task.getActprocInsId());
//        mapr.put("act.taskDefKey",taskDefKey);
//        mapr.put("act.comment",msg);
//        mapr.put("act.flag",flag);
//        approveModule.approveCommit(ConnectionURL.STR_MAINTENANCE_APPROVE,mapr);
//    }

    @Override
    public void resultData(Message msg) {
        super.resultData(msg);
        switch (msg.what){
            case DownLoadImageTask.DOWN_LOAD_SUCCESS:
                Toast.makeText(this,"下载成功",Toast.LENGTH_SHORT).show();
                FileUtil.openFile(getContext(),msg.obj.toString());
                break;
            case DownLoadImageTask.DOWN_LOAD_FILED:
                Toast.makeText(this,"下载失败",Toast.LENGTH_SHORT).show();
                break;
        }
    }

//    private void showData() {
//        if(task==null)return;
//
//    }

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
                            DownLoadImageTask task = new DownLoadImageTask(TodoReportApproveActivity.this,
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
