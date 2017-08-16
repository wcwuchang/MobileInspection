package com.tianjin.MobileInspection.activity.maintenance_task;

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

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.adapter.FujianAdapter;
import com.tianjin.MobileInspection.adapter.HiddenTroublesAddAdapter;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.customview.dialog.CustomDialog;
import com.tianjin.MobileInspection.customview.NobarGridView;
import com.tianjin.MobileInspection.customview.ScrollListView;
import com.tianjin.MobileInspection.entity.Task;
import com.tianjin.MobileInspection.main.BaseActivity;
import com.tianjin.MobileInspection.module.TaskModlue;
import com.tianjin.MobileInspection.until.ConnectionURL;
import com.tianjin.MobileInspection.until.DownLoadImageTask;
import com.tianjin.MobileInspection.until.FileUtil;

import java.io.File;

/**
 * Created by wuchang on 2016-12-16.
 *
 */
public class SpecialDetailActivity extends BaseActivity{

    private LinearLayout mlinearBack;
    private TextView mtvTitle;
    private LinearLayout mlinearSave;
    private TextView mtvSave;

    private TextView mtvDetailName;
    private TextView mtvDetailContent;
    private TextView mtvContractName;
    private TextView mtvTroubleName;
    private TextView mtvContact;
    private TextView mtvDate;
    private LinearLayout mlinearTrouble;
    private LinearLayout mlinearContract;

    private NobarGridView mgvDeailyImg;
    private ScrollListView mlvTroubles;
    private ScrollListView mlvFujian;

    private Intent intent;
    private String deilayTaskId;
    private String deilayTaskName;
    private TaskModlue modlue;
    private Task task;

    private HiddenTroublesAddAdapter addadapter;
    private FujianAdapter fujianadapter;
    private CustomDialog downLoadDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deaily_detail);
        initView();
        initData();
    }

    private void initData() {
        modlue=new TaskModlue(handler,this);
        intent=getIntent();
        deilayTaskId=intent.getStringExtra("taskId");
        deilayTaskName=intent.getStringExtra("name");
        mtvDetailName.setText(deilayTaskName);
        boolean finish=intent.getBooleanExtra("finish",false);
        if(finish){
//            mlinearSave.setVisibility(View.VISIBLE);
//            mtvSave.setText("上报");
//            mlinearSave.setOnClickListener(this);
        }else {
            mlinearSave.setVisibility(View.GONE);
//            mtvSave.setText("上报");
//            mlinearSave.setOnClickListener(this);
        }
        addadapter=new HiddenTroublesAddAdapter(this);
        fujianadapter=new FujianAdapter(this);
        mlvTroubles.setAdapter(addadapter);
        mlvFujian.setAdapter(fujianadapter);
        modlue.getSpecialDetail(deilayTaskId);

        mlvTroubles.setFocusable(false);

        mlvFujian.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                download(position);
            }
        });

        downLoadDialog=new CustomDialog(this,"正在下载");
        downLoadDialog.setCancelable(false);
        downLoadDialog.setCanceledOnTouchOutside(false);
    }

    private void initView() {
        mlinearBack=(LinearLayout)findViewById(R.id.linear_return_back);
        mlinearSave=(LinearLayout)findViewById(R.id.linear_save);
        mtvTitle=(TextView)findViewById(R.id.tv_activity_title);
        mtvSave=(TextView)findViewById(R.id.tv_activity_mun);

        mtvDetailName=(TextView)findViewById(R.id.tv_deaily_detail_name);
        mtvDetailContent=(TextView)findViewById(R.id.tv_deaily_detail_content);
        mtvContractName=(TextView)findViewById(R.id.tv_deaily_detail_contract_name);
        mtvTroubleName=(TextView)findViewById(R.id.tv_deaily_detail_trouble_name);
        mtvContact=(TextView)findViewById(R.id.tv_deaily_detail_contact_name);
        mtvDate=(TextView)findViewById(R.id.tv_deaily_detail_location_name);

        mgvDeailyImg=(NobarGridView)findViewById(R.id.gv_deaily_detail_img);
        mlvTroubles=(ScrollListView)findViewById(R.id.lv_trouble_list);
        mlvFujian=(ScrollListView)findViewById(R.id.lv_fujian_list);
        mlinearTrouble=(LinearLayout)findViewById(R.id.linear_deilay_trouble) ;
        mlinearContract=(LinearLayout)findViewById(R.id.linear_special_contract);
        mlinearTrouble.setVisibility(View.GONE);
        mlinearContract.setVisibility(View.GONE);
        mtvTitle.setText("计划任务详情");
        mlinearBack.setOnClickListener(this);
        mlinearTrouble.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.linear_return_back:
                finish();
                break;
            case R.id.linear_save:
                Intent upload=new Intent("android.intent.action.SPECIALMAINTENANCEUPLOADACTIVITY");
                upload.putExtra("id",deilayTaskId);
                upload.putExtra("title",task.getTaskName());
                upload.putExtra("todo",false);
                upload.putExtra("procInsId", "");
                upload.putExtra("options", "");
                upload.putExtra("taskId", "");
                upload.putExtra("todo",false);
                startActivity(upload);
                break;
            case R.id.linear_deilay_trouble:
                if(task!=null) {
                    Intent trouble = new Intent("android.intent.action.TODOHIDDENTROUBLEDETAILACTIVITY");
                    trouble.putExtra("GET", false);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("detail", task.getHiddenTroubleDetail());
                    trouble.putExtras(bundle);
                    startActivity(trouble);
                }
                break;

        }
    }

    @Override
    public void resultData(Message msg) {
        super.resultData(msg);
        switch (msg.what){
            case TaskModlue.INT_SPECIAL_DETAIL_SUCCESS:
                task=(Task)msg.obj;
                showData();
                break;
            case TaskModlue.INT_SPECIAL_DETAIL_FILED:
                toLoginActivity(msg);
                break;
//            case FileUploadModule.DOWNLOAD_SUCCESS:
//                Toast.makeText(this,"下载成功",Toast.LENGTH_SHORT).show();
//                downLoadDialog.cancel();
//                break;
//            case FileUploadModule.DOWNLOAD_FILED:
//                Toast.makeText(this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
//                downLoadDialog.cancel();
//                break;
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
        mtvDetailName.setText(task.getTaskName());
        mtvDetailContent.setText(task.getTaskContent());
        mtvContractName.setText(task.getContractName());
        if(task.getHiddenTroubleDetail()==null){
            mlinearTrouble.setVisibility(View.GONE);
        }else {
            mlinearTrouble.setVisibility(View.VISIBLE);
            mtvTroubleName.setText(task.getHiddenTroubleDetail().getTitle());
        }

        mtvContact.setText(task.getContactName());
        if(task.getHidden()!=null) {
            addadapter.updata(task.getHidden());
//            ScreenUtils.setListViewHeightBasedOnChildren(this,mlvTroubles);
        }
        if(task.getFujian()!=null){
            fujianadapter.updata(task.getFujian());
//            ScreenUtils.setListViewHeightBasedOnChildren(this,mlvFujian);
        }
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
                            DownLoadImageTask task = new DownLoadImageTask(SpecialDetailActivity.this,
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
