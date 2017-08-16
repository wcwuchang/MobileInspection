package com.tianjin.MobileInspection.activity.maintenance_task;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
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
import com.tianjin.MobileInspection.activity.todo_task.TodoActivity;
import com.tianjin.MobileInspection.adapter.ChoseImageAdapter;
import com.tianjin.MobileInspection.adapter.HiddenTroublesAddAdapter;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.customview.NobarGridView;
import com.tianjin.MobileInspection.entity.HiddenType;
import com.tianjin.MobileInspection.entity.ImageItem;
import com.tianjin.MobileInspection.entity.Task;
import com.tianjin.MobileInspection.main.BaseActivity;
import com.tianjin.MobileInspection.module.ApproveModule;
import com.tianjin.MobileInspection.module.FileUploadModule;
import com.tianjin.MobileInspection.module.TaskModlue;
import com.tianjin.MobileInspection.until.CheckUntil;
import com.tianjin.MobileInspection.until.ConnectionURL;
import com.tianjin.MobileInspection.until.FileUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by wuchang on 2016-12-12.
 *
 * 专项任务上报
 */
public class SpecialMaintenanceUploadActivity extends BaseActivity{
    private static final int INT_CHOSE_IMAGE_REQUEST_CODE=0x890;
    private static final int INT_SHOW_IMAGE_REQUEST_CODE=0x891;
    private static final int INT_ADD_TROUBLE_REQUESTCODE=0x7860;
    private int INT_MAX_CHOSED_NUMBER=9;
    private LinearLayout mlinearBack;
    private LinearLayout mlineatSave;
    private TextView mtvTitle;
    private TextView mtvCommit;
    private LinearLayout mlinearAddMain;

    private Button mbtnCommit;
    private EditText medtContent;
    private TextView mtvDate;
    private NobarGridView mgvTroubleImage;
    private ArrayList<ImageItem> choseImage;
    private ChoseImageAdapter adapter;
    private ArrayList<HiddenType> hiddenList;
    private ArrayList<String> hiddenId;
    private ArrayList<String> hiddennum;
    private com.tianjin.MobileInspection.customview.ScrollListView mlvTroubleList;

    private Intent intent;
    private String deilayId;
    private HiddenTroublesAddAdapter addAdapter;
    private String date="";
    private String content="";
    private TaskModlue modlue;
    private String imageLoadId;
    private Task task;
    private String title;
    private boolean show=false;
    private String options;
    private String taskId;
    private String id;
    private String procInsid;
    private boolean isTodo=false;
    private ApproveModule approveModule;
    private String taskDefKey;

    private String upLoadId="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deaily_maintenance_upload);
        initView();
        initData();
    }

    private void initData() {
        approveModule=new ApproveModule(handler,this);
        choseImage=new ArrayList<ImageItem>();
        adapter=new ChoseImageAdapter(this);
        mgvTroubleImage.setAdapter(adapter);
        addAdapter=new HiddenTroublesAddAdapter(this);
        mlvTroubleList.setAdapter(addAdapter);
        hiddennum=new ArrayList<String>();
        hiddenId=new ArrayList<String>();
        hiddenList=new ArrayList<HiddenType>();
        long time=System.currentTimeMillis();
        Date date=new Date(time);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        mtvDate.setText(sdf.format(date));
        intent=getIntent();
        deilayId=intent.getStringExtra("id");
        title=intent.getStringExtra("title");
        options=intent.getStringExtra("options");
        taskId=intent.getStringExtra("taskId");
        id=intent.getStringExtra("id");
        procInsid=intent.getStringExtra("procInsId");
        taskDefKey=intent.getStringExtra("taskDefKey");
        isTodo=intent.getBooleanExtra("todo",false);
        modlue=new TaskModlue(handler,this);
        modlue.getSpecialHasReported(deilayId);
        if(isTodo ){
            mbtnCommit.setVisibility(View.VISIBLE);
            mlinearAddMain.setVisibility(View.VISIBLE);
            showAddItem();
            show = false;
            adapter.updata(choseImage);
        }else {
            show=true;
            medtContent.setFocusable(false);
            mlinearAddMain.setVisibility(View.GONE);
            mbtnCommit.setVisibility(View.GONE);
        }

        mgvTroubleImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(((ImageItem)adapter.getItem(position)).isadd()){
                    Intent in=new Intent("android.intent.action.MULTIIMAGESELECTORACTIVITY");
                    in.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT,9);
                    in.putExtra("chosed", choseImage.size() - 1);
                    startActivityForResult(in,INT_CHOSE_IMAGE_REQUEST_CODE);
                }else{
                    ArrayList<String> paths=new ArrayList<String>();
                    for(ImageItem ii:choseImage){
                        if(!ii.isadd()) {
                            paths.add(ii.getImagePath());
                            KLog.d(ii.getImagePath());
                        }
                    }
                    Intent intent=new Intent("android.intent.action.SHOWCHOSEDACTIVITY");
                    intent.putStringArrayListExtra("data",paths);
                    intent.putExtra("id",position);
                    intent.putExtra("show",show);
                    startActivityForResult(intent,INT_SHOW_IMAGE_REQUEST_CODE);
                }
            }
        });

    }

    private void initView() {
        mlinearBack=(LinearLayout)findViewById(R.id.linear_return_back);
        mlineatSave=(LinearLayout)findViewById(R.id.linear_save);
        mtvTitle=(TextView)findViewById(R.id.tv_activity_title);
        mtvCommit=(TextView)findViewById(R.id.tv_activity_mun);

        medtContent=(EditText)findViewById(R.id.edt_hidden_trouble_detail);

        mtvDate=(TextView)findViewById(R.id.tv_maintenance_date);

        mgvTroubleImage=(NobarGridView)findViewById(R.id.gv_chose_image);
        mbtnCommit=(Button)findViewById(R.id.btn_maintenance) ;
        mlinearAddMain=(LinearLayout)findViewById(R.id.linear_deaily_add_maintenance);
        mlvTroubleList=(com.tianjin.MobileInspection.customview.ScrollListView)findViewById(R.id.lv_trouble_list);

//        mlineatSave.setVisibility(View.VISIBLE);
        mtvCommit.setText("审批");
        mtvTitle.setText("计划维修上报");
        mlinearBack.setOnClickListener(this);
        mlineatSave.setOnClickListener(this);
        mbtnCommit.setOnClickListener(this);
        mlinearAddMain.setOnClickListener(this);
    }

    private void showAddItem(){
        ImageItem ii=new ImageItem();
        ii.setBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.list_image_add_down));
        ii.setIsadd(true);
        choseImage.add(choseImage.size(),ii);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.linear_return_back:
                finish();
                break;
            case R.id.linear_save:
                Intent intent1=new Intent("android.intent.action.INSPECTIONAPPROVEACTIVITY");
                intent1.putExtra("procInsId",procInsid);
                intent1.putExtra("options",options);
                intent1.putExtra("id",id);
                intent1.putExtra("taskId",taskId);
                intent1.putExtra("url", ConnectionURL.STR_SPECIAL_SAVEAUDIT);
                startActivity(intent1);
                break;
            case R.id.btn_maintenance:
                if(checkData()){
                    Map<String,String> map=new HashMap<String, String>();
                    map.put("title",title);
                    map.put("description", content);
                    map.put("specialMaintenanceMain.id",id);
                    map.put("specialMaintenanceMain.act.taskId",taskId);
                    map.put("specialMaintenanceMain.act.procInsId",procInsid);
                    map.put("specialMaintenanceMain.act.taskDefKey",taskDefKey);
                    map.put("specialMaintenanceMain.act.comment","维修上报");
                    map.put("specialMaintenanceMain.act.flag","yes");
                    for (int i=0; i < hiddenId.size(); i++) {
                        map.put("specialMaintenanceReportDetailList["+i+"].num",hiddennum.get(i));
                        map.put("specialMaintenanceReportDetailList["+i+"].unit.id",hiddenId.get(i));
                    }
                    if(!upLoadId.equals("")){
                        map.put("id",task.getTaskId());
                    }
                    KLog.d(map.toString());
//                    modlue.saveSpecialTaskUpload(map);
                }
                break;
            case R.id.linear_deaily_add_maintenance:
                Intent intent2=new Intent("android.intent.action.CHOSEHIDDENTYPEACTIVITY");
                Bundle bundle=new Bundle();
                bundle.putSerializable("hasChose",hiddenList);
                intent2.putExtras(bundle);
                startActivityForResult(intent2,INT_ADD_TROUBLE_REQUESTCODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && data!=null){
            if(requestCode==INT_CHOSE_IMAGE_REQUEST_CODE){
                List<String> list = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                for(String path:list){
                    if(!isHasExist(path)){
                        ImageItem ii=new ImageItem();
                        ii.setImagePath(path);
                        ii.setIsadd(false);
                        choseImage.add(choseImage.size()-1,ii);
                    }
                }
                if(choseImage.size()>INT_MAX_CHOSED_NUMBER){
                    choseImage.remove(choseImage.size()-1);
                }
                adapter.updata(choseImage);
            }else if(requestCode==INT_SHOW_IMAGE_REQUEST_CODE){
                if(show) return;
                List<String> list = data.getStringArrayListExtra("data");
                choseImage.clear();
                for (String str:list){
                    ImageItem ii=new ImageItem();
                    ii.setImagePath(str);
                    ii.setIsadd(false);
                    choseImage.add(ii);
                }
                if(choseImage.size()<INT_MAX_CHOSED_NUMBER){
                    showAddItem();
                }
                adapter.updata(choseImage);
            }else if(requestCode==INT_ADD_TROUBLE_REQUESTCODE){
                Bundle bundle=data.getExtras();
                hiddenList=(ArrayList<HiddenType>)bundle.getSerializable("hiddentype");
                KLog.d((hiddenList==null)+"");
//                addHiddenType(hiddenList);
                addAdapter.updata(hiddenList);
//                ScreenUtils.setListViewHeightBasedOnChildren(this,mlvTroubleList);
            }
        }
    }

    /**
     * 判断该路径的图片是否已经被选择
     * @param path
     * @return
     */
    private boolean isHasExist(String path){
        if(choseImage==null||choseImage.size()<=1) return false;
        boolean h=false;
        for(int i=0;i<choseImage.size()-1;i++){
            if(choseImage.get(i).getImagePath().equals(String.valueOf(path)))
                h= true;
        }
        return h;
    }

    private boolean checkData(){
        if(CheckUntil.checkEditext(medtContent.getText()).equals("")){
            Toast.makeText(this,"请填写任务描述",Toast.LENGTH_SHORT).show();
            return false;
        }
        content=medtContent.getText().toString();
        date=mtvDate.getText().toString();
        if(hiddenList!=null&&hiddenList.size()>0){
            hiddennum.clear();
            hiddenId.clear();
            for (int i = 0; i < hiddenList.size(); i++) {
                HiddenTroublesAddAdapter.Holder holder=(HiddenTroublesAddAdapter.Holder)mlvTroubleList.getChildAt(i).getTag();
                if(CheckUntil.checkEditext(holder.content.getText()).equals("")){
                    Toast.makeText(this,"请填写"+holder.name.getText()+"的具体数量",Toast.LENGTH_SHORT).show();
                    return false;
                }
                String num=holder.content.getText().toString();
                hiddennum.add(num);
                HiddenType ht=(HiddenType)addAdapter.getItem(i);
                hiddenId.add(ht.getTypeId());
            }
        }
        return true;
    }


    @Override
    public void resultData(Message msg) {
        super.resultData(msg);
        switch (msg.what){
            case ApproveModule.INT_COMMIT_APPROVE_SUCCESS:
                Toast.makeText(this,"专项维修上报成功",Toast.LENGTH_SHORT).show();
                finish();
                break;
            case TaskModlue.INT_SPECIAL_UPLOAD_SUCCESS:
                imageLoadId=msg.obj.toString();
                KLog.d(choseImage.size()+"");
                if(choseImage.size()>1){
                    fileupload();
                }else {
                    specialApprove();
                }
                break;
            case TaskModlue.INT_SPECIAL_UPLOAD_FILED:
                toLoginActivity(msg);
                break;
            case FileUploadModule.INT_UPLOAD_FILES_SUCCESS:
                specialApprove();
                break;
            case FileUploadModule.INT_UPLOAD_FILES_FILED:
                AlertDialog.Builder tuichuDialog = new AlertDialog.Builder(this).setMessage("图片上传失败");
                tuichuDialog.setPositiveButton("重新上传",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                fileupload();
                            }
                        });
                tuichuDialog.setNegativeButton("取消",null);
                tuichuDialog.show();
                break;
            case TaskModlue.INT_SPECIAL_HAS_REPORTED_TRUE:
                task=(Task)msg.obj;
                showData();
                break;
            case TaskModlue.INT_SPECIAL_HAS_REPORTED_FALSE:
                if(msg.arg1==-1){
                    showAddItem();
                    show=false;
                    adapter.updata(choseImage);
                }else {
                    toLoginActivity(msg);
                }
                break;

        }
    }


    private void specialApprove(){
        Toast.makeText(this,"专项维修上报成功",Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(this,TodoActivity.class);
        startActivity(intent);
//        finish();
    }

    private void fileupload(){
        List<String> path=new ArrayList<String>();
        for(int i=0;i<choseImage.size();i++){
            if(!choseImage.get(i).isadd){
                path.add(choseImage.get(i).getImagePath());
            }
        }
        if(path.size()>0) {
            try {
                FileUploadModule.uploadFiles(this, handler,
                        ConnectionURL.STR_SAVE_SPECIAL_TASK_IMAGE + MyApplication.getStringSharedPreferences("JSESSIONID", "") + ConnectionURL.HTTP_SERVER_END
                        , path, imageLoadId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            specialApprove();
        }
    }

    private void showData() {
        upLoadId=task.getTaskId();
        if(!isTodo) {
            medtContent.setText(task.getTaskContent());
            mtvDate.setText(task.getDate());
            mtvTitle.setText("上报详情");
//        if(!isTodo) {
//            medtContent.setFocusable(false);
//            mlinearAddMain.setVisibility(View.GONE);
//            mbtnCommit.setVisibility(View.GONE);
//        }else if(isTodo) {
//            mbtnCommit.setVisibility(View.VISIBLE);
//        }
            if (task.getFujian() != null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < task.getFujian().size(); i++) {
                            ImageItem ii = new ImageItem();
                            ii.setIsadd(false);
                            ii.setImagePath(FileUtil.saveIntenetImage(SpecialMaintenanceUploadActivity.this, task.getFujian().get(i)));
                            choseImage.add(ii);
                            handler.post(getReportedImage);
                        }
                        if (isTodo) {
                            showAddItem();
                            handler.post(getReportedImage);
                        }

                    }
                }).start();
            } else {
//            if(isTodo) {
//                showAddItem();
//                adapter.updata(choseImage);
//            }
            }

            addAdapter.updata(task.getHidden());
//        ScreenUtils.setListViewHeightBasedOnChildren(this,mlvTroubleList);
        }

    }

    Runnable getReportedImage=new Runnable() {
        @Override
        public void run() {
            adapter.updata(choseImage);
        }
    };
}
