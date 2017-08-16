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
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
import com.tianjin.MobileInspection.entity.Approve;
import com.tianjin.MobileInspection.entity.HiddenTroubleDetail;
import com.tianjin.MobileInspection.entity.HiddenType;
import com.tianjin.MobileInspection.entity.Task;
import com.tianjin.MobileInspection.entity.Todo;
import com.tianjin.MobileInspection.main.BaseActivity;
import com.tianjin.MobileInspection.module.ApproveModule;
import com.tianjin.MobileInspection.module.TaskModlue;
import com.tianjin.MobileInspection.module.TodoModule;
import com.tianjin.MobileInspection.until.CheckUntil;
import com.tianjin.MobileInspection.until.ConnectionURL;
import com.tianjin.MobileInspection.until.DownLoadImageTask;
import com.tianjin.MobileInspection.until.FileUtil;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wuchang on 2016-12-16.
 *
 * 日常详情
 *
 * 废弃
 */
public class STodoReportTaskDetailActivity extends BaseActivity{

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
    private ScrollListView mslvHidden;

    private LinearLayout mlinearJL;
    private TextView mtvJlM;

    private LinearLayout mlinearFinish;

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

    private ArrayList<HiddenType> hiddenTypes;
    private TodoModule module;
    private String taskDefKey="";
    private String commit;
    private String flag="";
    private ApproveModule approveModule;
    private Button mbtnNo;
    private Button mbtnYes;
    private Todo todo;

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
    private ArrayList<HiddenType> jltroubles;
    private Task trouble;
    private TaskModlue taskModlue;
    private LinearLayout mlinearDStoack;
    private TextView mtvDStockName;
    private EditText medtDStockNum;
    private TextView mtvDStockUnit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_report_task_detail);
        initView();
        initData();
    }

    private void initData() {
        approveModule=new ApproveModule(handler,this);
        modlue=new TaskModlue(handler,this);
        intent=getIntent();
        todo=(Todo)intent.getExtras().getSerializable("todo");
        KLog.d((todo==null)+"");
        module=new TodoModule(handler,this);
        hiddenTypes=new ArrayList<HiddenType>();
        module.getGormUrl(todo.getTaskId(),todo.getTaskName(),todo.getTaskDefKey(),todo.getProcInsId(),todo.getProcDefId());

        addadapter=new HiddenTroublesAddAdapter(this);
        mslvHidden.setAdapter(addadapter);
//        mlvFujian.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                download(position);
//            }
//        });
        downLoadDialog=new CustomDialog(this,"正在下载");
        downLoadDialog.setCancelable(false);
        downLoadDialog.setCanceledOnTouchOutside(false);
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

        mslvHidden=(ScrollListView)findViewById(R.id.slv_hidden_list);
        mlinearFinish=(LinearLayout)findViewById(R.id.linear_deaily_detail_location);

        mbtnNo=(Button)findViewById(R.id.btn_no);
        mbtnYes=(Button)findViewById(R.id.btn_yes);

        mlinearSave.setVisibility(View.VISIBLE);
        mtvSave.setText("方案");
        mtvTitle.setText("机动维修任务详情");
        mlinearBack.setOnClickListener(this);
        mlinearTrouble.setOnClickListener(this);
        mlinearTrouble.setVisibility(View.VISIBLE);
        mbtnNo.setOnClickListener(this);
        mbtnYes.setOnClickListener(this);
        mlinearSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.linear_return_back:
                finish();
                break;
            case R.id.linear_save:
                if(task.getMaintenanceState()==2) {
                    HiddenTroubleDetail hidden=task.getHiddenTroubleDetail();
                    Intent intent1=new Intent(this, TodoReportApproveActivity.class);
                    intent1.putExtra("stock",hidden.getStockName());
                    intent1.putExtra("quantity",hidden.getQuantity());
                    intent1.putExtra("unit",hidden.getUnit());
                    intent1.putExtra("report",task.getReportContent());
                    intent1.putStringArrayListExtra("fujian",task.getFujian());
                    startActivity(intent1);
                }else if(task.getMaintenanceState()==1||task.getMaintenanceState()==3) {
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
                }
                break;
            case R.id.linear_deilay_trouble:
                if(task!=null) {
                    Intent trouble = new Intent("android.intent.action.MAINTANENCEHIDDENDETAILACTIVITY");
                    trouble.putExtra("id", task.getHiddenTroubleDetail().getTroubleId());
                    startActivity(trouble);
                }
                break;
            case R.id.btn_no:
                commit="不通过";
                flag="no";
                getgetApproveList();
                break;
            case R.id.btn_yes:
                commit="通过";
                flag="yes";
//                if((todo.getTaskName()).equals("计划管理员")&&(task.getMaintenanceState()==1||task.getMaintenanceState()==3)){
//                    initDialog();
//                }else {
                    getgetApproveList();
//                }
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
        if(task.getMaintenanceState()==2) {
            approveModule.approveCommit(ConnectionURL.STR_MAINTENANCE_APPROVE, mapr);
        }else {
            approveModule.approveCommit(ConnectionURL.STR_MAINTENANCE_MAIN_APPROVE, mapr);
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
            case TaskModlue.INT_DEILAY_DETAIL_FILED:
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
            case TodoModule.COMMIT_METERAGE_SUCCESS:
                dialog.cancel();
                getgetApproveList();
                break;
            case TodoModule.COMMIT_METERAGE_FILED:
                dialog.cancel();
                toLoginActivity(msg);
                break;
            case TaskModlue.INT_DEILAY_HAS_REPORTED_TRUE:
                trouble=(Task)msg.obj;
                jltroubles=trouble.getHidden();
                if(jltroubles!=null){
                    for(int i=0;i<jltroubles.size();i++){
                        jltroubles.get(i).setShow(false);
                        KLog.d(jltroubles.get(i).getTypeId());
                        if(hiddenTypes.size()>0){
                            for(int t=0;t<hiddenTypes.size();t++){
                                if(hiddenTypes.get(t).getTypeId().equals(jltroubles.get(i).getTypeId())){
                                    jltroubles.get(i).setTypeName(hiddenTypes.get(t).getTypeName());
                                    jltroubles.get(i).setTypeUnit(hiddenTypes.get(t).getTypeUnit());
                                    break;
                                }
                            }
                        }else {
                            if(task.getDate()!=null&&!task.getDate().equals("")){
                                jltroubles.get(0).setTypeName(task.getHiddenTroubleDetail().getStockName());
                                jltroubles.get(0).setTypeUnit(task.getHiddenTroubleDetail().getUnit());
                            }
                        }
                    }
                    troubleAdapter.updata(jltroubles);
                }
                break;
            case TaskModlue.INT_DEILAY_HAS_REPORTED_FALSE:
                Toast.makeText(this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void showData() {
        if(task==null) return;
        mtvDate.setText(task.getDate());
        mtvDetailContent.setText(task.getTaskContent());
        mtvContractName.setText(task.getContractName());
        mtvTroubleName.setText(task.getHiddenTroubleDetail().getTitle());
        mtvContact.setText(task.getContactName());

        HiddenTroubleDetail hidden=task.getHiddenTroubleDetail();
            mlinearStoack.setVisibility(View.VISIBLE);
            mtvStockName.setText(hidden.getStockName());
            mtvStockNum.setText(hidden.getQuantity());
            mtvStockUnit.setText(hidden.getUnit());
            mtvTroubleName.setText(hidden.getTypeName()+"-"+hidden.getName()+"-"+hidden.getStockName());
        hiddenTypes=task.getHidden();
        addadapter.updata(hiddenTypes);

        if(task.getYearMonth()!=0) {
            mtvJlM.setText(task.getYearMonth() + "");
        }

        if(task.getMaintenanceState()==2) {
            mtvSave.setText("方案");
        }else if(task.getMaintenanceState()==1||task.getMaintenanceState()==3) {
            mtvSave.setText("维修详情");
            mtvSave.setTextSize(12);
        }else if(task.getMaintenanceState()==4) {
            mtvSave.setText("维修详情");
            mtvSave.setTextSize(12);
            mlinearJL.setVisibility(View.GONE);
            mlinearStoack.setVisibility(View.GONE);
            mlinearTrouble.setVisibility(View.GONE);
        }

        if(task.getDate()==null||task.getDate().equals("")){
            mlinearStoack.setVisibility(View.GONE);
            mlinearJL.setVisibility(View.GONE);
            mlinearTrouble.setVisibility(View.GONE);
            mlinearFinish.setVisibility(View.GONE);
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
                            DownLoadImageTask task = new DownLoadImageTask(STodoReportTaskDetailActivity.this, handler, filename);
                            StringBuffer sb = new StringBuffer();
                            sb.append(ConnectionURL.HTTP_SERVER_IMAGE).append(fujianadapter.getItem(position).toString());
                            task.execute(sb.toString());
                        }
                    });
            tuichuDialog.setNegativeButton("取消", null);
            tuichuDialog.show();
        }
    }


    //计量dialog初始化
    private void initDialog(){
        initDialogView();
        initDialogData();
        dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void initDialogData() {
        troubleAdapter=new HiddenTroublesAddAdapter(this);
        taskModlue=new TaskModlue(handler,this);
        taskModlue.getDeilayHasReported(task.getTaskId());
        mlvJLTroublelist.setAdapter(troubleAdapter);
        KLog.d(hiddenTypes.size()+"");
    }

    private void initDialogView() {
        dialogView= LayoutInflater.from(this).inflate(R.layout.dialog_jiliang,null);
        mtvJLtitle=(TextView)dialogView.findViewById(R.id.tv_jl_dialog_title);
        mbtnJLturndown=(Button)dialogView.findViewById(R.id.btn_jl_dialog_turndown);
        mbtnJLcancel=(Button)dialogView.findViewById(R.id.btn_jl_dialog_cancel);
        mbtnJLcommit=(Button)dialogView.findViewById(R.id.btn_jl_dialog_commit);
        mbtnJLupdetail=(Button)dialogView.findViewById(R.id.btn_jl_dialog_updetail);
        mlvJLTroublelist=(ListView)dialogView.findViewById(R.id.lv_jl_dialog_types);

        mlinearDStoack=(LinearLayout)dialogView.findViewById(R.id.linear_stock);
        mtvDStockName=(TextView)dialogView.findViewById(R.id.tv_stoack);
        medtDStockNum=(EditText)dialogView.findViewById(R.id.edt_stock_num);
        mtvDStockUnit=(TextView)dialogView.findViewById(R.id.tv_stock_unit);

        mtvJLtitle.setText("计量");
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
                        module.commitDeliayMeterage(task.getTaskId(),task.getActtaskId(),task.getActprocInsId(),task.getActtaskDefKey(),"计量确认","yes",trouble.getTaskId(),jltroubleId,jltroubleContent);
                    }
                    break;
                case R.id.btn_jl_dialog_turndown:
                    module.commitDeliayMeterage(task.getTaskId(),task.getActtaskId(),task.getActprocInsId(),task.getActtaskDefKey(),"驳回","no",trouble.getTaskId(),null,null);
                    break;
                case R.id.btn_jl_dialog_updetail:
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
            }
        }
    };

    private boolean checkJLCommitData(){
        jltroubleId=new ArrayList<String>();
        jltroubleContent=new ArrayList<String>();
        if(jltroubles==null||jltroubles.size()==0){
            Toast.makeText(this,"无数据提交",Toast.LENGTH_SHORT).show();
            dialog.cancel();
            return false;
        }else {
            for (int i = 0; i < jltroubles.size(); i++) {
                HiddenTroublesAddAdapter.Holder holder = (HiddenTroublesAddAdapter.Holder) mlvJLTroublelist.getChildAt(i).getTag();
                if (CheckUntil.checkEditext(holder.content.getText()).equals("")) {
                    Toast.makeText(this, "请填写" + holder.name.getText() + "的具体数量", Toast.LENGTH_SHORT).show();
                    return false;
                }
                String num = holder.content.getText().toString();
                jltroubleContent.add(num);
                HiddenType ht = (HiddenType) troubleAdapter.getItem(i);
                KLog.d(ht.toString());
                jltroubleId.add(ht.getTypeId());
            }
        }
        return true;
    }


}
