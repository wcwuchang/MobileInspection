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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.socks.library.KLog;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.adapter.ChoseImageAdapter;
import com.tianjin.MobileInspection.adapter.HiddenSpinnerAdapter;
import com.tianjin.MobileInspection.adapter.HiddenTroublesAddAdapter;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.customview.NobarGridView;
import com.tianjin.MobileInspection.customview.ScrollListView;
import com.tianjin.MobileInspection.customview.dialog.ApproveCommitDialog;
import com.tianjin.MobileInspection.entity.Approve;
import com.tianjin.MobileInspection.entity.ContractManager;
import com.tianjin.MobileInspection.entity.HiddenSpinner;
import com.tianjin.MobileInspection.entity.HiddenTroubleDetail;
import com.tianjin.MobileInspection.entity.HiddenType;
import com.tianjin.MobileInspection.entity.ImageItem;
import com.tianjin.MobileInspection.entity.PlanManage;
import com.tianjin.MobileInspection.entity.Road;
import com.tianjin.MobileInspection.entity.Todo;
import com.tianjin.MobileInspection.main.BaseActivity;
import com.tianjin.MobileInspection.module.ApproveModule;
import com.tianjin.MobileInspection.module.ConfigModule;
import com.tianjin.MobileInspection.module.ContractModule;
import com.tianjin.MobileInspection.module.MaintanenceModule;
import com.tianjin.MobileInspection.module.PlanModule;
import com.tianjin.MobileInspection.module.TodoModule;
import com.tianjin.MobileInspection.until.CheckUntil;
import com.tianjin.MobileInspection.until.ConnectionURL;
import com.tianjin.MobileInspection.until.FileUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wuchang on 2016-12-12.
 *
 * 隐患详情
 */
public class TodoHiddenDetailActivity extends BaseActivity implements ApproveCommitDialog.ApproveCommitListener{
    private static final int INT_CHOSE_IMAGE_REQUEST_CODE=0x890;
    private static final int INT_SHOW_IMAGE_REQUEST_CODE=0x891;
    private static final int INT_CHOSE_LOCATION_REQUEST_CODE=0x892;
    private static final int INT_CHOSE_HIDDEN_TYPE_REQUEST_CODE=0x893;

    private int INT_MAX_CHOSED_NUMBER=9;
    private LinearLayout mlinearBack;
    private TextView mtvTitle;
    private RelativeLayout mrelatLocation;
    private LinearLayout mlinearHiddenTypeList;
    private EditText medtTroubleName;
    private EditText medtContent;
    private TextView mtvContact;
    private TextView mtvDate;
    private TextView mtvLocation;
    private NobarGridView mgvTroubleImage;
    private ArrayList<ImageItem> choseImage;
    private ChoseImageAdapter adapter;
    private ArrayList<HiddenType> hiddenList;
    private ArrayList<String> hiddenName;
    private ArrayList<String> hiddennum;
    private ScrollListView mlvTroublelist;
    private HiddenTroublesAddAdapter addAdapter;

    private double latitude;
    private double lonitude;
    private String address;
    private Intent intent;
    private String troubleId;

    private TodoModule module;
    private HiddenTroubleDetail detail;

    private TextView mtxEmergencyType;
    private TextView mtxHiddenType;
    private TextView mtxHiddenName;
    private TextView mtxStock;
    private TextView mtxRoad;
    private TextView mtxRoadNum;

    private LinearLayout mlinearStockNum;
    private TextView mtvStockName;
    private EditText medtStockNum;
    private TextView mtvUnit;

    private Todo todo;

    private RelativeLayout mrelatApprove;
    private LinearLayout mlinearApprove;
    private Button mbtnInvalid;
    private Button mbtnValid;
    private LinearLayout mlinearManage;
    private Button mbtnManage;
    private LinearLayout mlinearMaintence;
    private Button mbtnMaintence;

    private int status;//隐患的状态
    private String taskDefKey="";
    private ApproveModule approveModule;

    private String commit;
    private String flag="yes";

    //维修下发dialog
    private Dialog dialogMaintenance;
    private View dialogView;
    private Button mbtnLeaveLate;
    private Button mbtnNotMaintenance;
    private Button mbtnImmediatelyMaintenance;
    //管理结论性意见的dialog
    private Dialog dialogObservation;
    private View obserView;
    private EditText medtObservation;
    private Button mbtnObsCancel;
    private Button mbtnObsSure;

    private boolean isManageCommit=false;

    //延缓维修dialog
    private Dialog dialogLeave;
    private View leaveView;
    private Spinner mspContract;
    private Spinner mspMonth;
    private Button mbtnLeaveCancel;
    private Button mbtnLeaveCommit;
    private HiddenSpinnerAdapter contractAdapter;
    private HiddenSpinnerAdapter monthAdapter;
    private String planId;

    private ApproveCommitDialog approveCommitDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_hidden_trouble_approve);
        initView();
        initData();
    }

    private void initData() {
        addAdapter=new HiddenTroublesAddAdapter(this);
        hiddenList=new ArrayList<HiddenType>();
        choseImage=new ArrayList<ImageItem>();
        hiddenName=new ArrayList<String>();
        hiddennum=new ArrayList<String>();
        adapter=new ChoseImageAdapter(this);
        mgvTroubleImage.setAdapter(adapter);
        mlvTroublelist.setAdapter(addAdapter);
        intent=getIntent();
        module=new TodoModule(handler,this);
        approveModule=new ApproveModule(handler,this);
        todo=(Todo)intent.getSerializableExtra("todo");

        status=todo.getStatus();
        if(status==1){
            mrelatApprove.setVisibility(View.VISIBLE);
            mlinearManage.setVisibility(View.VISIBLE);
        }

        module.getGormUrl(todo.getTaskId(),todo.getTaskName(),todo.getTaskDefKey(),todo.getProcInsId(),todo.getProcDefId());

        mgvTroubleImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ArrayList<String> paths=new ArrayList<String>();
                    for(ImageItem ii:choseImage){
                        if(!ii.isadd()) {
                            paths.add(ii.getImagePath());
                        }
                    }
                    Intent intent=new Intent("android.intent.action.SHOWCHOSEDACTIVITY");
                    intent.putStringArrayListExtra("data",paths);
                    intent.putExtra("id",position);
                    intent.putExtra("show",true);
                    startActivityForResult(intent,INT_SHOW_IMAGE_REQUEST_CODE);
            }
        });



    }

    private void initView() {
        mlinearBack=(LinearLayout)findViewById(R.id.linear_return_back);
        mtvTitle=(TextView)findViewById(R.id.tv_activity_title);

        medtContent=(EditText)findViewById(R.id.edt_hidden_trouble_detail);
        medtTroubleName=(EditText)findViewById(R.id.edt_hidden_trouble_name);

        mtvContact=(TextView)findViewById(R.id.tv_hidden_trouble_contact);
        mtvDate=(TextView)findViewById(R.id.tv_hidden_trouble_date);
        mtvLocation=(TextView)findViewById(R.id.tv_hidden_trouble_location);

        mgvTroubleImage=(NobarGridView)findViewById(R.id.gv_chose_image);

        mrelatLocation=(RelativeLayout)findViewById(R.id.relat_hidden_chose_location);

        mlinearHiddenTypeList=(LinearLayout)findViewById(R.id.linear_hidden_type_list);
        mlvTroublelist=(ScrollListView)findViewById(R.id.lv_yihuan_maintenance_trouble);

        mtxEmergencyType=(TextView)findViewById(R.id.tx_emergency_type);
        mtxHiddenType=(TextView)findViewById(R.id.tx_hidden_type);
        mtxHiddenName=(TextView)findViewById(R.id.tx_hidden_name);
        mtxStock=(TextView)findViewById(R.id.tx_hidden_stock);
        mtxRoad=(TextView)findViewById(R.id.tx_road);
        mtxRoadNum=(TextView)findViewById(R.id.tx_road_light);

        mlinearStockNum=(LinearLayout)findViewById(R.id.linear_hidden_trouble_num);
        mtvStockName=(TextView)findViewById(R.id.tv_yh_detail_name);
        medtStockNum=(EditText)findViewById(R.id.edt_yh_detail_size);
        mtvUnit=(TextView)findViewById(R.id.tv_yh_detail_unit);

        mrelatApprove=(RelativeLayout)findViewById(R.id.relat_approve);
        mlinearApprove=(LinearLayout)findViewById(R.id.linear_approve);
        mbtnInvalid=(Button)findViewById(R.id.btn_invalid);
        mbtnValid=(Button)findViewById(R.id.btn_valid);
        mlinearManage=(LinearLayout)findViewById(R.id.linear_manage);
        mbtnManage=(Button)findViewById(R.id.btn_manage);
        mlinearMaintence=(LinearLayout)findViewById(R.id.linear_maintence);
        mbtnMaintence=(Button)findViewById(R.id.btn_maintence);

        mrelatApprove.setVisibility(View.GONE);
        mtvTitle.setText("隐患详情");
        mlinearBack.setOnClickListener(this);
        mrelatLocation.setOnClickListener(this);
        mbtnValid.setOnClickListener(this);
        mbtnInvalid.setOnClickListener(this);
        mbtnManage.setOnClickListener(this);
        mlinearMaintence.setOnClickListener(this);
        mbtnMaintence.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.linear_return_back:
                finish();
                break;
            case R.id.relat_hidden_chose_location:
                Intent intent1=new Intent("android.intent.action.CHOSELOCATIONACTIVITY");
                intent1.putExtra("latitude",latitude);
                intent1.putExtra("lonitude",lonitude);
                intent1.putExtra("address",address);
                intent1.putExtra("isChose",false);
                startActivityForResult(intent1,INT_CHOSE_LOCATION_REQUEST_CODE);
                break;
            case R.id.btn_invalid:
                if(approveCommitDialog==null){
                    approveCommitDialog=new ApproveCommitDialog(this);
                    approveCommitDialog.setListener(this);
                }
                approveCommitDialog.show();
                break;
            case R.id.btn_valid:
                commit="有效";
                flag="yes";
                getgetApproveList();
                break;
            case R.id.btn_manage:
                initObservationDialog();
                break;
            case R.id.btn_maintence:
                flag="yes";
                initMaintenanceDialog();
                break;
        }
    }


    private void getgetApproveList(){
        isManageCommit=false;
        if(!taskDefKey.equals("")){
            setHiddenApprove(commit,flag);
        }else {
            approveModule.getApproveList(detail.getProcInsId());
        }
    }


    /**
     * 根据坐标获取位置信息
     * @param latitude
     * @param lonitude
     */
    public void getAddressForLatLng(double latitude,double lonitude){
        LatLng latlon=new LatLng(latitude,lonitude);
        //实例化一个地理编码查询对象
        GeoCoder geoCoder = GeoCoder.newInstance();
        //设置反地理编码位置坐标
        ReverseGeoCodeOption op = new ReverseGeoCodeOption();
        op.location(latlon);
        //发起反地理编码请求(经纬度->地址信息)
        geoCoder.reverseGeoCode(op);
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
                //获取点击的坐标地址
                address = arg0.getAddress();
                KLog.d(address);
                mtvLocation.setText(address);
            }

            @Override
            public void onGetGeoCodeResult(GeoCodeResult arg0) {
            }
        });
    }


    @Override
    public void resultData(Message msg) {
        super.resultData(msg);
        switch (msg.what){
            case ConfigModule.GET_EMERGENCY_SUCCESS:
                List<HiddenSpinner> emer=(List<HiddenSpinner>)msg.obj;
                for(int i=0;i<emer.size();i++){
                    if(detail.getEmergencyType().equals(emer.get(i).getSpid())){
                        mtxEmergencyType.setText(emer.get(i).getName());
                        if(status==0){
                            mrelatApprove.setVisibility(View.VISIBLE);
                            if(emer.get(i).getId()==3){
                                mlinearManage.setVisibility(View.VISIBLE);
                            }else {
                                mlinearApprove.setVisibility(View.VISIBLE);
                            }
                        }
                        break;
                    }
                }
                break;
            case TodoModule.INT_GET_FORM_URL_SUCCESS:
                String jsessionid = MyApplication.getStringSharedPreferences("JSESSIONID", "");
                String url=msg.obj.toString();
                String[] str=url.split("\\?");
                StringBuffer sb=new StringBuffer();
                sb.append(str[0]).append(";JSESSIONID=").append(jsessionid).append("?_ajax&").append(str[1]);
                KLog.d(url);
                module.getTodoHiddenTroubleDetail(sb.toString());
                break;
            case TodoModule.INT_GET_TODO_DETAIL_SUCCESS:
                detail=(HiddenTroubleDetail)msg.obj;
                showData();
                break;
            case ConfigModule.INT_GET_ROAD_SUCCESS:
                Road road=(Road)msg.obj;
                mtxRoad.setText(road.getRoadName());
                break;
            case ApproveModule.INT_GET_APPROVE_SUCCESS:
                ArrayList<Approve> testdata=(ArrayList<Approve>)msg.obj;
                taskDefKey=testdata.get(testdata.size()-1).getApproveId();
                setHiddenApprove(commit,flag);
                break;
            case ApproveModule.INT_COMMIT_APPROVE_SUCCESS:
                if(isManageCommit){
                    Intent intent1 = new Intent("android.intent.action.TODOACTIVITY");
                    startActivity(intent1);
                }else {
                    if (flag.equals("yes")) {
                        mlinearApprove.setVisibility(View.GONE);
                        mlinearMaintence.setVisibility(View.VISIBLE);
                        initMaintenanceDialog();
                    } else {
                        Intent intent1 = new Intent("android.intent.action.TODOACTIVITY");
                        startActivity(intent1);
                    }
                }
                break;
            case ContractModule.INT_CONTRACT_LIST_FILED:
            case MaintanenceModule.INT_UPDATEMAINTENANCE_FILED:
            case ApproveModule.INT_COMMIT_APPROVE_FILED:
            case ApproveModule.INT_GET_APPROVE_FILED:
                toLoginActivity(msg);
                break;
            case MaintanenceModule.INT_UPDATEMAINTENANCE_SUCCESS:
                Intent intent1 = new Intent("android.intent.action.TODOACTIVITY");
                startActivity(intent1);
                break;
            case ContractModule.INT_CONTRACT_LIST_SUCCESS:
                List<ContractManager> data = (List<ContractManager>)msg.obj;
                if(data!=null && data.size()>0){
                    List<HiddenSpinner> contract = new ArrayList<HiddenSpinner>();
                    for (int i = 0; i < data.size(); i++) {
                        HiddenSpinner hs = new HiddenSpinner();
                        hs.setSpid(data.get(i).getContractId());
                        hs.setName(data.get(i).getName());
                        contract.add(hs);
                    }
                    contractAdapter.updata(contract);
                }
                break;
            case PlanModule.PLAN_LIST_SUCCESS:
                ArrayList<PlanManage> planManages=(ArrayList<PlanManage>)msg.obj;
                if(planManages!=null && planManages.size()>0){
                    List<HiddenSpinner> contract = new ArrayList<HiddenSpinner>();
                    for (int i = 0; i < planManages.size(); i++) {
                        HiddenSpinner hs = new HiddenSpinner();
                        hs.setSpid(planManages.get(i).getPlanId());
                        hs.setName(planManages.get(i).getPlanName());
                        contract.add(hs);
                    }
                    contractAdapter.updata(contract);
                }
                break;

        }
    }

    //初始化维修下发dialog
    private void initMaintenanceDialog() {
        initDialog();
        dialogMaintenance=new Dialog(this);
        dialogMaintenance.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMaintenance.setContentView(dialogView);
        dialogMaintenance.setCanceledOnTouchOutside(false);
        dialogMaintenance.setCancelable(false);
        if(!dialogMaintenance.isShowing()) {
            dialogMaintenance.show();
        }

    }

    private void initDialog() {
        dialogView= LayoutInflater.from(this).inflate(R.layout.dialog_maintanence_issued,null);
        mbtnLeaveLate=(Button)dialogView.findViewById(R.id.btn_leave_late);
        mbtnNotMaintenance=(Button)dialogView.findViewById(R.id.btn_not_maintenance);
        mbtnImmediatelyMaintenance=(Button)dialogView.findViewById(R.id.btn_immediately_maintenance);

        mbtnLeaveLate.setOnClickListener(maintenanceListener);
        mbtnNotMaintenance.setOnClickListener(maintenanceListener);
        mbtnImmediatelyMaintenance.setOnClickListener(maintenanceListener);
    }

    private void showData() {
        if(detail==null) return;
        medtContent.setText(detail.getContent());
        mtvContact.setText(detail.getInspectionName());
        mtvDate.setText(detail.getDate());
        latitude=detail.getLatitude();
        lonitude=detail.getLongitude();
        getAddressForLatLng(latitude,lonitude);
        addAdapter.updata(detail.getHiddenTypes());
        final List<String> path=detail.getImage();
        if(path!=null&&path.size()>0){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i=0;i<path.size();i++){
                        ImageItem imageItem=new ImageItem();
                        imageItem.setImagePath(FileUtil.saveIntenetImage(TodoHiddenDetailActivity.this,path.get(i)));
                        KLog.d(imageItem.getImagePath());
                        choseImage.add(imageItem);
                    }
                    handler.post(runnable);
                }
            }).start();
        }
        ConfigModule.getEmergencyType(this,handler);
        List<HiddenSpinner> type= MyApplication.DBM.gettHiddenSpinner().query("spid",detail.getType());
        if(type!=null) {
            mtxHiddenType.setText(type.get(0).getName());
        }
        List<HiddenSpinner> name= MyApplication.DBM.gettHiddenSpinner().query("spid",detail.getNameId());
//        List<HiddenSpinner> stock=MyApplication.DBM.gettHiddenSpinner().query("spid",detail.getStockId());
        if(name!=null&&name.size()>0) {
//            if(stock!=null&&stock.size()>0)
            mtxHiddenName.setText(name.get(0).getName());
            if(name.get(0).getName().equals("其它")||name.get(0).getName().equals("其他")){
                mtxStock.setText("其它");
                mlinearStockNum.setVisibility(View.GONE);
            }else {
                mtxStock.setText(detail.getContent());
                mlinearStockNum.setVisibility(View.VISIBLE);
                mtvStockName.setText(detail.getContent());
                mtvUnit.setText(detail.getUnit());
                medtStockNum.setText(detail.getQuantity());
            }
        }
        ConfigModule.getRoad(handler,detail.getRoadId());
        mtxRoadNum.setText(detail.getLightId());

        KLog.d(detail.getOption().toString());
    }

    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            adapter.updata(choseImage);
        }
    };


    private void setHiddenApprove(String msg,String flag){
        Map<String,String> mapr=new HashMap<String,String>();
        mapr.put("id",detail.getTroubleId());
        mapr.put("act.taskId",detail.getTaskId());
        mapr.put("act.procInsId",detail.getProcInsId());
        mapr.put("act.taskDefKey",taskDefKey);
        mapr.put("act.comment",msg);
        mapr.put("act.flag",flag);
        approveModule.approveCommit(ConnectionURL.STR_HIDDEN_TROUBLE_SAVEAUDIT,mapr);
    }


    //初始化管理 结论性意见dialog
    private void initObservationDialog(){
        obserView=LayoutInflater.from(this).inflate(R.layout.dialog_concluding_observations,null);
        medtObservation=(EditText)obserView.findViewById(R.id.edt_observation);
        mbtnObsCancel=(Button)obserView.findViewById(R.id.btn_cancel);
        mbtnObsSure=(Button)obserView.findViewById(R.id.btn_commit);

        mbtnObsCancel.setOnClickListener(observationListener);
        mbtnObsSure.setOnClickListener(observationListener);

        dialogObservation=new Dialog(this);
        dialogObservation.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogObservation.setCanceledOnTouchOutside(false);
        dialogObservation.setContentView(obserView);
        dialogObservation.show();
    }


    //初始化延缓维修的dialog
    private void initLeaveDialog(){
        if(dialogLeave==null) {
            leaveView = LayoutInflater.from(this).inflate(R.layout.dialog_leave_late, null);
            mspContract = (Spinner) leaveView.findViewById(R.id.sp_contract);
            mbtnLeaveCancel = (Button) leaveView.findViewById(R.id.btn_dialog_cancel);
            mbtnLeaveCommit = (Button) leaveView.findViewById(R.id.btn_dialog_commit);

            contractAdapter = new HiddenSpinnerAdapter(this);
            mspContract.setAdapter(contractAdapter);

            PlanModule planModule=new PlanModule(handler,this);
            planModule.getPlanTypeList();

            mbtnLeaveCommit.setOnClickListener(leaveListener);
            mbtnLeaveCancel.setOnClickListener(leaveListener);

            dialogLeave = new Dialog(this);
            dialogLeave.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogLeave.setCanceledOnTouchOutside(false);
            dialogLeave.setContentView(leaveView);

            mspContract.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    HiddenSpinner hs=(HiddenSpinner)contractAdapter.getItem(position);
                    planId=hs.getSpid();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        dialogLeave.show();

    }

    /**
     * 维修下发dialog的 listener
     */
    private View.OnClickListener maintenanceListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_leave_late:
                    initLeaveDialog();
                    break;
                case R.id.btn_not_maintenance:
                    AlertDialog.Builder tuichuDialog = new AlertDialog.Builder(TodoHiddenDetailActivity.this).setMessage("确定该隐患暂不维修？");
                    tuichuDialog.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    MaintanenceModule.updateMaintenance(handler,detail.getTroubleId(),"4");
                                }
                            });
                    tuichuDialog.setNegativeButton("取消",null);
                    tuichuDialog.show();
                    break;
                case R.id.btn_immediately_maintenance:
                    Intent intent=new Intent("android.intent.action.DAILYMAINTENANCEACTIVITY");
                    intent.putExtra("id",detail.getTroubleId());
                    startActivity(intent);
                    break;

            }
        }
    };

    /**
     * 管理性意见的监听
     */
    private View.OnClickListener observationListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_cancel:
                    dialogObservation.cancel();
                    break;
                case R.id.btn_commit:
                    isManageCommit=true;
                    if(CheckUntil.checkEditext(medtObservation.getText()).equals("")){
                        Toast.makeText(TodoHiddenDetailActivity.this,"请填写结论性意见",Toast.LENGTH_SHORT).show();
                    }else {
                        commit=CheckUntil.checkEditext(medtObservation.getText());
                        if (taskDefKey.equals("")) {
                            approveModule.getApproveList(detail.getProcInsId());
                        }else {
                            setHiddenApprove(commit,"yes");
                        }
                    }
                    break;
            }
        }
    };


    //延缓维修
    private View.OnClickListener leaveListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_dialog_cancel:
                    dialogLeave.cancel();
                    break;
                case R.id.btn_dialog_commit:
                    isManageCommit=true;
                    approveModule.deferredMaintenance(detail.getTroubleId(),planId);
                    break;

            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dialogMaintenance!=null&&dialogMaintenance.isShowing()){
            dialogMaintenance.cancel();
        }
        if(dialogLeave!=null&&dialogLeave.isShowing()){
            dialogLeave.cancel();
        }
        if(dialogObservation!=null&&dialogObservation.isShowing()){
            dialogObservation.cancel();
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
            commit=msg;
            flag="no";
            getgetApproveList();
        }
    }
}
