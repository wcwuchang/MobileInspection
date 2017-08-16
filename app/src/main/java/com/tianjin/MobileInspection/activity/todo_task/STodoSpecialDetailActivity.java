package com.tianjin.MobileInspection.activity.todo_task;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
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
import com.tianjin.MobileInspection.customview.NobarGridView;
import com.tianjin.MobileInspection.customview.ScrollListView;
import com.tianjin.MobileInspection.entity.HiddenType;
import com.tianjin.MobileInspection.entity.Task;
import com.tianjin.MobileInspection.entity.Todo;
import com.tianjin.MobileInspection.main.BaseActivity;
import com.tianjin.MobileInspection.module.TaskModlue;
import com.tianjin.MobileInspection.module.TodoModule;
import com.tianjin.MobileInspection.until.CheckUntil;
import com.tianjin.MobileInspection.until.ConnectionURL;
import com.tianjin.MobileInspection.until.DownLoadImageTask;
import com.tianjin.MobileInspection.until.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuchang on 2016-12-16.
 *
 * 待办专项任务详情
 */
public class STodoSpecialDetailActivity extends BaseActivity{

    private final int INT_WORKER_NAME_REQUEST = 0x85;
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
    private LinearLayout mlinearMaintain;
    private LinearLayout mlinearContract;
    private TextView mtvMaintain;

    private NobarGridView mgvDeailyImg;
    private ScrollListView mlvTroubles;
    private ScrollListView mlvFujian;

    private Intent intent;
    private TodoModule modlue;
    private TaskModlue taskModlue;

    private HiddenTroublesAddAdapter addadapter;
    private FujianAdapter fujianadapter;
    private Todo todo;
    private Task task;
    private List<String> contact;
    private String contactId="";

    //计量弹框
    private Dialog dialog;
    private View dialogView;
    private TextView mtvJLtitle;
    private Button mbtnJLturndown;
    private Button mbtnJLcancel;
    private Button mbtnJLcommit;
    private Button mbtnJLupdetail;
    private ListView mlvJLTroublelist;
    private ArrayList<String> jltroubleId;
    private ArrayList<String> jltroubleContent;
    private HiddenTroublesAddAdapter troubleAdapter;
    private List<HiddenType> jltroubles;
    private Task trouble;

    //是否计量
    private boolean isMeter=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deaily_detail);
        initView();
        initData();
    }

    private void initData() {
        modlue=new TodoModule(handler,this);
        contact=new ArrayList<String>();
        intent=getIntent();
        todo=(Todo)intent.getExtras().getSerializable("todo");
        boolean finish=intent.getBooleanExtra("finish",false);

        mlinearSave.setOnClickListener(this);

        addadapter=new HiddenTroublesAddAdapter(this);
        fujianadapter=new FujianAdapter(this);
//        mlvTroubles.setAdapter(addadapter);
//        mlvFujian.setAdapter(fujianadapter);

        mlvFujian.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                download(position);
            }
        });
        modlue.getGormUrl(todo.getTaskId(),todo.getTaskName(),todo.getTaskDefKey(),todo.getProcInsId(),todo.getProcDefId());
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
        mlinearMaintain=(LinearLayout)findViewById(R.id.linear_maintain);
        mtvMaintain=(TextView)findViewById(R.id.tv_deaily_maintain_name);
        mlinearContract=(LinearLayout)findViewById(R.id.linear_special_contract) ;

        mlinearContract.setVisibility(View.GONE);
        mlinearMaintain.setVisibility(View.VISIBLE);
        mtvTitle.setText("专项任务详情");
        mtvSave.setText("");
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
                if(task.getStatus()==4){
                    initDialog();
                }else {
                    doApproveOrUpload();
                }
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
            case R.id.linear_maintain:
                Intent intent2=new Intent("android.intent.action.CONTACTCHOICEACTIVITY");
                intent2.putExtra("id",contactId);
                startActivityForResult(intent2,INT_WORKER_NAME_REQUEST);
                break;

        }
    }

    private void doApproveOrUpload(){
        if(task.getStatus()==2) {
            if (contactId.equals("")) {
                Toast.makeText(this, "请选择维修人", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Intent intent1 = new Intent();
        if(task.getStatus()==3){
            intent1.setAction("android.intent.action.SPECIALMAINTENANCEUPLOADACTIVITY");
            intent1.putExtra("taskDefKey",todo.getTaskDefKey());
            intent1.putExtra("title",task.getTaskName());
            intent1.putExtra("todo",true);
        }else {
            intent1.setAction("android.intent.action.INSPECTIONAPPROVEACTIVITY");
            intent1.putExtra("url", ConnectionURL.STR_SPECIAL_SAVEAUDIT);
            intent1.putExtra("maintain", contactId);
        }
        intent1.putExtra("id", task.getTaskId());
        intent1.putExtra("procInsId", task.getActprocInsId());
        intent1.putExtra("options", task.getOption());
        intent1.putExtra("taskId", task.getActtaskId());
        startActivity(intent1);
    }

    @Override
    public void resultData(Message msg) {
        super.resultData(msg);
        switch (msg.what){
//            case FileUploadModule.DOWNLOAD_SUCCESS:
//                Toast.makeText(this,"下载成功",Toast.LENGTH_SHORT).show();
//                break;
//            case FileUploadModule.DOWNLOAD_FILED:
//                Toast.makeText(this,"下载失败",Toast.LENGTH_SHORT).show();
//                break;
            case TodoModule.INT_GET_FORM_URL_SUCCESS:
                String jsessionid = MyApplication.getStringSharedPreferences("JSESSIONID", "");
                KLog.d(jsessionid);
                String url=msg.obj.toString();
                String[] str=url.split("\\?");
                StringBuffer sb=new StringBuffer();
                sb.append(str[0]).append(";JSESSIONID=").append(jsessionid).append("?_ajax&").append(str[1]);
                KLog.d(url);
                modlue.getSpecialDetail(sb.toString());
                break;
            case TodoModule.INT_GET_SPECIAL_DETAIL_SUCCESS:
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
            case TaskModlue.INT_SPECIAL_HAS_REPORTED_TRUE:
                trouble=(Task)msg.obj;
                jltroubles=trouble.getHidden();
                if(jltroubles!=null){
                    for(int i=0;i<jltroubles.size();i++){
                        jltroubles.get(i).setShow(false);
                        KLog.d(jltroubles.get(i).getTypeId());
                    }
                    troubleAdapter.updata(jltroubles);
                }
                break;
            case TodoModule.COMMIT_METERAGE_SUCCESS:
//                if(isMeter) {
//                    Toast.makeText(this, "计量确认成功", Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(this, "上报结果已驳回", Toast.LENGTH_SHORT).show();
//                }
                dialog.cancel();
                finish();
                break;
            case TodoModule.COMMIT_METERAGE_FILED:
                dialog.cancel();
                toLoginActivity(msg);
                break;
        }
    }

    private void showData() {
        if(task==null) return;
        mtvDate.setText(task.getDate());
        mtvDetailName.setText(task.getTaskName());
        mtvDetailContent.setText(task.getTaskContent());
//        mtvContractName.setText(task.getContractName());
        if(CheckUntil.checkEditext(task.getTroubleName()).equals("")){
            mlinearTrouble.setVisibility(View.GONE);
        }else {
            mlinearTrouble.setVisibility(View.VISIBLE);
            mtvTroubleName.setText(task.getTroubleName());
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
        if(todo.getStatus()==-1){
            mlinearSave.setVisibility(View.GONE);
        }else {
            mlinearSave.setVisibility(View.VISIBLE);
        }
        if(task.getStatus()==2){
            mlinearMaintain.setOnClickListener(this);
        }else {
            mtvMaintain.setText(MyApplication.getStringSharedPreferences("name",""));
        }

        if(task.getStatus()==3){
            mtvSave.setText("上报");
        }else if(task.getStatus()==4){
            mtvSave.setText("计量");
        }else {
            mtvSave.setText("审批");
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
                            DownLoadImageTask task = new DownLoadImageTask(STodoSpecialDetailActivity.this,
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK&&data!=null){
            if(requestCode==INT_WORKER_NAME_REQUEST){
                List<String> id = data.getStringArrayListExtra("id");
                if (id != null && id.size() > 0) {
                    contact.clear();
                    contact.addAll(id);
                    List<String> name = data.getStringArrayListExtra("name");
                    StringBuffer sb = new StringBuffer();
                    StringBuffer sbid = new StringBuffer();
                    for (int i = 0; i < name.size(); i++) {
                        sb.append(name.get(i));
                        sbid.append(id.get(i));
                        if (i != name.size() - 1) {
                            sb.append(",");
                            sbid.append(",");
                        }
                    }
                    contactId = sbid.toString();
                    mtvMaintain.setText(sb.toString());
                }

            }
        }
    }

    //计量dialog初始化
    private void initDialog(){
        initDialogView();
        initDialogData();
        dialog=new Dialog(this);
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void initDialogData() {
        troubleAdapter=new HiddenTroublesAddAdapter(this);
        taskModlue=new TaskModlue(handler,this);
        taskModlue.getSpecialHasReported(task.getTaskId());
        mlvJLTroublelist.setAdapter(troubleAdapter);
    }

    private void initDialogView() {
        dialogView= LayoutInflater.from(this).inflate(R.layout.dialog_jiliang,null);
        mtvJLtitle=(TextView)dialogView.findViewById(R.id.tv_jl_dialog_title);
        mbtnJLturndown=(Button)dialogView.findViewById(R.id.btn_jl_dialog_turndown);
        mbtnJLcancel=(Button)dialogView.findViewById(R.id.btn_jl_dialog_cancel);
        mbtnJLcommit=(Button)dialogView.findViewById(R.id.btn_jl_dialog_commit);
        mbtnJLupdetail=(Button)dialogView.findViewById(R.id.btn_jl_dialog_updetail);
        mlvJLTroublelist=(ListView)dialogView.findViewById(R.id.lv_jl_dialog_types);

        mtvJLtitle.setText(task.getTaskName()+"计量");
        mbtnJLcommit.setOnClickListener(jlDialogListener);
        mbtnJLcancel.setOnClickListener(jlDialogListener);
        mbtnJLturndown.setOnClickListener(jlDialogListener);
        mbtnJLupdetail.setOnClickListener(jlDialogListener);
    }

    private View.OnClickListener jlDialogListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_jl_dialog_cancel:
                    dialog.cancel();
                    break;
                case R.id.btn_jl_dialog_commit:
                    if(checkJLCommitData()){
                        isMeter=true;
                        modlue.commitSpecialMeterage(task.getTaskId(),task.getActtaskId(),task.getActprocInsId(),task.getActtaskDefKey(),"计量确认","yes",trouble.getTaskId(),jltroubleId,jltroubleContent);
                    }
                    break;
                case R.id.btn_jl_dialog_turndown:
                    isMeter=false;
                    modlue.commitSpecialMeterage(task.getTaskId(),task.getActtaskId(),task.getActprocInsId(),task.getActtaskDefKey(),"驳回","no",trouble.getTaskId(),null,null);
                    break;
                case R.id.btn_jl_dialog_updetail:
                    Intent intent1=new Intent();
                    intent1.setAction("android.intent.action.SPECIALMAINTENANCEUPLOADACTIVITY");
                    intent1.putExtra("id",task.getTaskId());
                    intent1.putExtra("title",task.getTaskName());
                    intent1.putExtra("procInsId", task.getActprocInsId());
                    intent1.putExtra("options", task.getOption());
                    intent1.putExtra("taskId", task.getActtaskId());
                    intent1.putExtra("taskDefKey",task.getActtaskDefKey());
                    intent1.putExtra("todo",false);
                    startActivity(intent1);
                    break;
            }
        }
    };

    private boolean checkJLCommitData(){
        if(jltroubles==null||jltroubles.size()==0){
            Toast.makeText(this,"无数据提交",Toast.LENGTH_SHORT).show();
            dialog.cancel();
            return false;
        }
        jltroubleId=new ArrayList<String>();
        jltroubleContent=new ArrayList<String>();
        for(int i=0;i<jltroubles.size();i++){
            HiddenTroublesAddAdapter.Holder holder=(HiddenTroublesAddAdapter.Holder)mlvJLTroublelist.getChildAt(i).getTag();
            if(CheckUntil.checkEditext(holder.content.getText()).equals("")){
                Toast.makeText(this,"请填写"+holder.name.getText()+"的具体数量",Toast.LENGTH_SHORT).show();
                return false;
            }
            String num=holder.content.getText().toString();
            jltroubleContent.add(num);
            HiddenType ht=(HiddenType)troubleAdapter.getItem(i);
            KLog.d(ht.toString());
            jltroubleId.add(ht.getTypeId());
        }
        return true;
    }

}
