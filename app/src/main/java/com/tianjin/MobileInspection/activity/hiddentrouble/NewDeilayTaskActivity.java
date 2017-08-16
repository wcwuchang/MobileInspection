package com.tianjin.MobileInspection.activity.hiddentrouble;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.socks.library.KLog;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.adapter.FujianAdapter;
import com.tianjin.MobileInspection.adapter.HiddenTroublesAddAdapter;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.customview.dialog.DateTimeDialog;
import com.tianjin.MobileInspection.customview.ScrollListView;
import com.tianjin.MobileInspection.entity.HiddenType;
import com.tianjin.MobileInspection.main.BaseActivity;
import com.tianjin.MobileInspection.module.FileUploadModule;
import com.tianjin.MobileInspection.module.TaskModlue;
import com.tianjin.MobileInspection.until.CheckUntil;
import com.tianjin.MobileInspection.until.ConnectionURL;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by wuchang on 2016-12-23.
 * 新建日常任务
 */
public class NewDeilayTaskActivity extends BaseActivity implements DateTimeDialog.MyOnDateSetListener{

    private final int INT_WORKER_NAME_REQUEST = 0x85;
    private final int INT_CONTRACT_NAME_REQUEST = 0x89;
    private final int INT_TROUBLE_NAME_REQUEST = 0x86;
    private final int INT_CHOSE_HIDDEN_TYPE_REQUEST_CODE=0x90;
    private static final int INT_IMAGE_CHOSE=0x5238;
    private static final int INT_FILE_CHOSE=0x5239;
    private static final int INT_GET_EXPERT=0x5240;

    private LinearLayout mlinearBack;
    private LinearLayout mlinearSave;
    private TextView mtvTitle;
    private ImageView mivSave;

    private EditText medtTaskName;
    private EditText medtContent;
    private TextView mtvBKU;
    private TextView mtvContractName;
    private TextView mtvTroubleName;
    private TextView mtvContactName;
    private TextView mtvDate;

    private RelativeLayout mrelatContractName;
    private RelativeLayout mrelatTroubleName;
    private RelativeLayout mrelatContactName;
    private RelativeLayout mrelatDate;

    private Button mbtnAddTrouble;
    private Button mbtnTroubleDetail;
    private Button mbtnFujianUpload;

    private AlertDialog.Builder tuichuDialog;
    private DateTimeDialog dateTimeDialog;
    private String contactId = "";//联系人id
    private String contractId = "";//合同id
    private String troubleId = "";//隐患id
    private String taskName="";
    private String taskContent="";
    private String teakdate="";
    private TaskModlue modlue;
    private ScrollListView mlvOpion;
    private ScrollListView mlvFujian;
    private ArrayList<HiddenType> hiddenList;
    private HiddenTroublesAddAdapter addAdapter;
    private ArrayList<String> hiddenId=new ArrayList<String>();
    private ArrayList<String> hiddenNum=new ArrayList<String>();
    private String troubleData;

    private Dialog choseDialog;
    private View dialogView;
    private TextView mtvImage;
    private TextView mtvFile;
    private TextView mtvCancel;

    private ArrayList<String> filename;
    private ArrayList<String> filepath;
    private FujianAdapter fujianAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_deilay_task);
        initView();
        initData();
    }

    private void initData() {
        dateTimeDialog = new DateTimeDialog(this, null, this);
        modlue=new TaskModlue(handler,this);
        addAdapter=new HiddenTroublesAddAdapter(this);
        mlvOpion.setAdapter(addAdapter);
        hiddenList=new ArrayList<HiddenType>();
        filename=new ArrayList<String>();
        filepath=new ArrayList<String>();
        fujianAdapter=new FujianAdapter(this);
        mlvFujian.setAdapter(fujianAdapter);

        mlvOpion.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,final int position, long id) {
                AlertDialog.Builder tuichuDialog = new AlertDialog.Builder(NewDeilayTaskActivity.this)
                        .setMessage("删除此项？");
                tuichuDialog.setPositiveButton("删除",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                hiddenList.remove(position);
                                addAdapter.updata(hiddenList);
//                                ScreenUtils.setListViewHeightBasedOnChildren(getApplicationContext(),mlvOpion);
                            }});
                tuichuDialog.setNegativeButton("取消",null);
                tuichuDialog.show();
                return false;
            }
        });

        mlvFujian.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,final int position, long id) {
                AlertDialog.Builder tuichuDialog = new AlertDialog.Builder(NewDeilayTaskActivity.this)
                        .setMessage("删除此项？");
                tuichuDialog.setPositiveButton("删除",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                filepath.remove(position);
                                filename.remove(position);
                                fujianAdapter.updata(filename);
//                                ScreenUtils.setListViewHeightBasedOnChildren(getApplicationContext(),mlvFujian);
                            }});
                tuichuDialog.setNegativeButton("取消",null);
                tuichuDialog.show();
                return false;
            }
        });
    }

    private void initView() {
        mlinearBack = (LinearLayout) findViewById(R.id.linear_return_back);
        mlinearSave = (LinearLayout) findViewById(R.id.linear_add);
        mivSave = (ImageView) findViewById(R.id.iv_add);
        mtvTitle = (TextView) findViewById(R.id.tv_activity_title);

        medtTaskName=(EditText)findViewById(R.id.edt_special_title_name);
        medtContent=(EditText)findViewById(R.id.edt_special_content_name);
        mtvBKU=(TextView)findViewById(R.id.tv_special_op_type);
        mtvContractName=(TextView)findViewById(R.id.tv_special_contract_name);
        mtvTroubleName=(TextView)findViewById(R.id.tv_special_trouble_name);
        mtvContactName=(TextView)findViewById(R.id.tv_special_cantact_name);
        mtvDate=(TextView)findViewById(R.id.tv_special_date_name);

        mrelatContractName=(RelativeLayout)findViewById(R.id.relat_special_contract);
        mrelatTroubleName=(RelativeLayout)findViewById(R.id.relat_special_trouble);
        mrelatContactName=(RelativeLayout)findViewById(R.id.relat_special_contact);
        mrelatDate=(RelativeLayout)findViewById(R.id.relat_special_date);

        mbtnAddTrouble=(Button)findViewById(R.id.btn_deilay_trouble_list);
        mbtnFujianUpload=(Button)findViewById(R.id.btn_deilay_fujian_upload);
        mbtnTroubleDetail=(Button)findViewById(R.id.btn_deilay_trouble_detail);

        mlvOpion=(ScrollListView)findViewById(R.id.lv_op_list);
        mlvFujian=(ScrollListView)findViewById(R.id.lv_fujian_list);

        mlinearSave.setVisibility(View.VISIBLE);

        mtvTitle.setText("新建日常任务");
        mivSave.setImageResource(R.drawable.list_ico_save);
        mlinearSave.setOnClickListener(this);
        mlinearBack.setOnClickListener(this);
        mrelatContractName.setOnClickListener(this);
        mrelatTroubleName.setOnClickListener(this);
        mrelatContactName.setOnClickListener(this);
        mrelatDate.setOnClickListener(this);

        mbtnAddTrouble.setOnClickListener(this);
        mbtnFujianUpload.setOnClickListener(this);
        mbtnTroubleDetail.setOnClickListener(this);
        mtvBKU.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.linear_return_back:
                finish();
                break;
            case R.id.relat_special_contract:
                toActivity("android.intent.action.NEWINSPECTIONCHOSEACTIVITY", INT_CONTRACT_NAME_REQUEST, 1);
                break;
            case R.id.relat_special_trouble:
                toActivity("android.intent.action.NEWINSPECTIONCHOSEACTIVITY", INT_TROUBLE_NAME_REQUEST, 6);
                break;
            case R.id.relat_special_contact:
                toActivity("android.intent.action.CONTACTCHOICEACTIVITY", INT_WORKER_NAME_REQUEST, 5);
                break;
            case R.id.relat_special_date:
                dateTimeDialog.hideOrShow();
                break;
            case R.id.linear_add:
                if(checkdata()){
                    //                     标题       内容      联系人    合同       日期    隐患      隐患id   name
                    modlue.saveDeilayTask(taskName,taskContent,contactId,contractId,teakdate,troubleId,hiddenId,hiddenNum);
                }
                break;
            case R.id.btn_deilay_trouble_list:
                Intent intent2=new Intent("android.intent.action.CHOSEHIDDENTYPEACTIVITY");
                Bundle bundle=new Bundle();
                bundle.putSerializable("hasChose",hiddenList);
                intent2.putExtras(bundle);
                startActivityForResult(intent2,INT_CHOSE_HIDDEN_TYPE_REQUEST_CODE);
                break;
            case R.id.btn_deilay_fujian_upload:
                choseDialoginit();
                break;
            case R.id.btn_deilay_trouble_detail:
                Intent it=new Intent("android.intent.action.HIDDENTROUBLEDETAILACTIVITY");
                it.putExtra("id",troubleId);
                startActivity(it);
                break;
            case R.id.tv_special_op_type:
                Intent iten=new Intent("android.intent.action.DISEASEDATEBASEACTIVITY");
                iten.putExtra("dis",false);
                startActivityForResult(iten,INT_GET_EXPERT);
                break;
        }
    }

    private void toActivity(String action, int requestcode, int type) {
        Intent in = new Intent(action);
        if(type==1){
            in.putExtra("id",contractId);
        }else if(type==5){
            in.putExtra("id",contactId);
        }else if(type==6){
            in.putExtra("id",troubleId);
        }
        in.putExtra("type", type);
        startActivityForResult(in, requestcode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK&&data!=null){
            if(requestCode==INT_CONTRACT_NAME_REQUEST){
                if (!data.getExtras().getString("id").equals("")) {
                    contractId = data.getExtras().getString("id");
                }
                KLog.d(contractId);
                mtvContractName.setText(data.getExtras().getString("name"));
            }else if(requestCode==INT_TROUBLE_NAME_REQUEST){
                if (!data.getExtras().getString("id").equals("")) {
                    troubleId = data.getExtras().getString("id");
                    troubleData=data.getExtras().getString("data");
                    mbtnTroubleDetail.setVisibility(View.VISIBLE);
                }else {
                    troubleId="";
                    mbtnTroubleDetail.setVisibility(View.GONE);
                }
                KLog.d(troubleId);
                mtvTroubleName.setText(data.getExtras().getString("name"));
            }else if(requestCode==INT_WORKER_NAME_REQUEST){
                List<String> id = data.getStringArrayListExtra("id");
                if (id != null && id.size() > 0) {
                    List<String> name = data.getStringArrayListExtra("name");
                    contactId = id.get(0);
                    mtvContactName.setText(name.get(0));
                }
            }else if(requestCode==INT_CHOSE_HIDDEN_TYPE_REQUEST_CODE){
                Bundle bundle=data.getExtras();
                ArrayList<HiddenType> getList=(ArrayList<HiddenType>)bundle.getSerializable("hiddentype");
                for(int i=0;i<getList.size();i++){
                    for(int j=0;j<hiddenList.size();j++){
                        if(hiddenList.get(j).getTypeId().equals(getList.get(i).getTypeId())){
                            getList.remove(i);
                            i--;
                            break;
                        }
                    }
                }
                hiddenList.addAll(getList);
                addAdapter.updata(hiddenList);
//                ScreenUtils.setListViewHeightBasedOnChildren(getApplicationContext(),mlvOpion);
            }else if(requestCode==INT_IMAGE_CHOSE){
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
//                        ScreenUtils.setListViewHeightBasedOnChildren(this,mlvFujian);
                    }
                }
            }else if(requestCode==INT_FILE_CHOSE){
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
//                ScreenUtils.setListViewHeightBasedOnChildren(this,mlvFujian);
            }else if(requestCode==INT_GET_EXPERT){
                String exp=data.getStringExtra("dis");
                medtContent.setText(exp);
            }
        }
    }

    @Override
    public void onDateSet(Date date) {
        java.util.Date currentdate = new java.util.Date();
        if(date.before(currentdate)){
            datebijiao("新建日常任务时间不能早于当前时间");
        }else {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
            teakdate=sdf.format(date);
            mtvDate.setText(teakdate);
        }
    }

    private void datebijiao(String msg) {
        tuichuDialog = new AlertDialog.Builder(this).setMessage(msg);
        tuichuDialog.setPositiveButton("知道了",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        tuichuDialog.show();
    }

    private boolean checkdata(){
        if(CheckUntil.checkEditext(medtTaskName.getText()).equals("")){
            Toast.makeText(this,"请填写任务名称",Toast.LENGTH_SHORT).show();
            return false;
        }
        taskName=medtTaskName.getText().toString();
        if(CheckUntil.checkEditext(medtContent.getText()).equals("")){
            Toast.makeText(this,"请填写维修方式",Toast.LENGTH_SHORT).show();
            return false;
        }
        taskContent=medtContent.getText().toString();
        if(contractId.equals("")){
            Toast.makeText(this,"请选择合同",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (troubleId.equals("")){
            Toast.makeText(this,"请选择隐患",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(contactId.equals("")){
            Toast.makeText(this,"请选择负责人",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(teakdate.equals("")){
            Toast.makeText(this,"请选择日期",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(hiddenList.size()>0) {
            hiddenId.clear();
            hiddenNum.clear();
            for (int i = 0; i < hiddenList.size(); i++) {
                HiddenTroublesAddAdapter.Holder holder=(HiddenTroublesAddAdapter.Holder)mlvOpion.getChildAt(i).getTag();
                if(CheckUntil.checkEditext(holder.content.getText()).equals("")){
                    Toast.makeText(this,"请填写"+holder.name.getText()+"的具体数量",Toast.LENGTH_SHORT).show();
                    return false;
                }
                String num=holder.content.getText().toString();
                hiddenNum.add(num);
                HiddenType ht=(HiddenType)addAdapter.getItem(i);
                hiddenId.add(ht.getTypeId());
            }
        }
        return true;
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
    public void resultData(Message msg) {
        super.resultData(msg);
        switch (msg.what){
            case TaskModlue.INT_SAVE_DEILAY_TASK_SUCCESS:
                if(filepath!=null&&filepath.size()>0) {
                    String id = msg.obj.toString();
                    try {
                        FileUploadModule.uploadFiles(this,handler,
                                ConnectionURL.STR_SAVE_DEILAY_TASK_FUJIAN+ MyApplication.getStringSharedPreferences("JSESSIONID", "")+ ConnectionURL.HTTP_SERVER_END
                                , filepath, id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case TaskModlue.INT_SAVE_DEILAY_TASK_FILED:
                toLoginActivity(msg);
                break;
            case FileUploadModule.INT_UPLOAD_FILES_SUCCESS:
                Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
                finish();
                break;
            case FileUploadModule.INT_UPLOAD_FILES_FILED:
                Toast.makeText(this,"保存失败",Toast.LENGTH_SHORT).show();
            break;
}
}
}
