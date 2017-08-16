package com.tianjin.MobileInspection.activity.maintenance_task;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.adapter.FujianAdapter;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.main.BaseActivity;
import com.tianjin.MobileInspection.module.FileUploadModule;
import com.tianjin.MobileInspection.module.MaintanenceModule;
import com.tianjin.MobileInspection.until.CheckUntil;
import com.tianjin.MobileInspection.until.ConnectionURL;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by wuchang on 2017-6-12.
 *
 * 上报方案
 */
public class ReportingSchemeActivity extends BaseActivity{
    private static final int INT_IMAGE_CHOSE=0x5238;
    private static final int INT_FILE_CHOSE=0x5239;
    private static final int INT_GET_EXPERT=0x5240;
    private LinearLayout mlinearBack;
    private TextView mtvTitle;

    private EditText medtReport;
    private ListView mlvFile;

    private Button mbtnCommit;
    private Button mbtnFile;
    private Intent intent;
    private String taskId;

    private ArrayList<String> filename;
    private ArrayList<String> filepath;
    private FujianAdapter fujianAdapter;

    private Dialog choseDialog;
    private View dialogView;
    private TextView mtvImage;
    private TextView mtvFile;
    private TextView mtvCancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporting_scheme);
        initView();
        initData();
    }

    private void initData() {
        intent=getIntent();
        taskId=intent.getStringExtra("taskId");

        filename=new ArrayList<String>();
        filepath=new ArrayList<String>();
        fujianAdapter=new FujianAdapter(this);
        mlvFile.setAdapter(fujianAdapter);

        mlvFile.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,final int position, long id) {
                AlertDialog.Builder tuichuDialog = new AlertDialog.Builder(ReportingSchemeActivity.this)
                        .setMessage("删除此项？");
                tuichuDialog.setPositiveButton("删除",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                filepath.remove(position);
                                filename.remove(position);
                                fujianAdapter.updata(filename);
                            }});
                tuichuDialog.setNegativeButton("取消",null);
                tuichuDialog.show();
                return false;
            }
        });
    }

    private void initView() {
        mlinearBack=(LinearLayout)findViewById(R.id.linear_return_back);
        mtvTitle=(TextView)findViewById(R.id.tv_activity_title);

        medtReport=(EditText)findViewById(R.id.edt_report);
        mlvFile=(ListView)findViewById(R.id.lv_file);

        mbtnCommit=(Button)findViewById(R.id.btn_commit);
        mbtnFile=(Button)findViewById(R.id.btn_file);

        mtvTitle.setText("上报方案");

        mbtnFile.setOnClickListener(this);
        mbtnCommit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btn_commit:
                String repot=CheckUntil.checkEditext(medtReport.getText());
                if(repot.equals("")){
                    Toast.makeText(this,"请填写方案说明",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(filepath.size()<=0){
                    Toast.makeText(this,"请选择附件",Toast.LENGTH_SHORT).show();
                    return;
                }
//
// MaintanenceModule.reportShceme(handler,taskId,repot);
                    try {
                        FileUploadModule.uploadReport(this,handler,ConnectionURL.STR_REPORT_SHCEME+ MyApplication.getStringSharedPreferences("JSESSIONID", "")+ ConnectionURL.HTTP_SERVER_END,
                                filepath,taskId,repot);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                break;
            case R.id.btn_file:
                choseDialoginit();
                break;
        }
    }
    private void choseDialoginit() {
        initDialogView();
        choseDialog=new Dialog(this);
        choseDialog.setContentView(dialogView);
        choseDialog.setCanceledOnTouchOutside(false);
        choseDialog.show();
    }

    private void initDialogView() {
        dialogView= LayoutInflater.from(this).inflate(R.layout.dialog_fujian_add,null);
        mtvImage=(TextView)dialogView.findViewById(R.id.tv_fujian_add_img);
        mtvFile=(TextView)dialogView.findViewById(R.id.tv_fujian_add_file);
        mtvCancel=(TextView)dialogView.findViewById(R.id.tv_fujian_add_cancel);
        mtvImage.setOnClickListener(dialogListener);
        mtvFile.setOnClickListener(dialogListener);
        mtvCancel.setOnClickListener(dialogListener);
    }

    View.OnClickListener dialogListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_fujian_add_img:
                    Intent intent=new Intent("android.intent.action.MULTIIMAGESELECTORACTIVITY");
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT,9);
                    startActivityForResult(intent,INT_IMAGE_CHOSE);
                    break;
                case R.id.tv_fujian_add_file:
                    Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
                    intent1.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                    intent1.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(intent1,INT_FILE_CHOSE);
                    break;
                case R.id.tv_fujian_add_cancel:
                    break;
            }
            choseDialog.cancel();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(resultCode==RESULT_OK&&requestCode==INT_IMAGE_CHOSE){
                List<String> list = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                if(list!=null||list.size()==0) {
                    for(int i=0;i<list.size();i++){
                        for(int j=0;j<filepath.size();j++){
                            if(filepath.get(j).equals(list.get(i))){
                                break;
                            }
                        }
                        filepath.add(list.get(i));
                        File file = new File(list.get(i));
                        filename.add(file.getName());
                        fujianAdapter.updata(filename);
                    }
                }
            }else if(resultCode==RESULT_OK&&requestCode==INT_FILE_CHOSE){
                Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
                int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                actualimagecursor.moveToFirst();
                String path = actualimagecursor.getString(actual_image_column_index);
                for(int i=0;i<filepath.size();i++){
                    if(filepath.equals(path)){
                        return;
                    }
                }
                filepath.add(path);
                File file = new File(path);
                filename.add(file.getName());
                fujianAdapter.updata(filename);
            }
    }


    @Override
    public void resultData(Message msg) {
        super.resultData(msg);
        switch (msg.what){
            case MaintanenceModule.NEW_MAINTANENCE_SUCCESS:
                if(filepath!=null&&filepath.size()>0) {
                    String id = msg.obj.toString();
                    try {
                        FileUploadModule.uploadFiles(this,handler,
                                ConnectionURL.STR_REPORT_SHCEME+ MyApplication.getStringSharedPreferences("JSESSIONID", "")+ ConnectionURL.HTTP_SERVER_END
                                , filepath, id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case MaintanenceModule.NEW_MAINTANENCE_FILED:
                toLoginActivity(msg);
                break;
            case FileUploadModule.INT_UPLOAD_FILES_SUCCESS:
                Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
                Intent deialy=new Intent(this,DeailyListActivity.class);
                startActivity(deialy);
                finish();
                break;
            case FileUploadModule.INT_UPLOAD_FILES_FILED:
                Toast.makeText(this,"保存失败",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
