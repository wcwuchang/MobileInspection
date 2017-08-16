package com.tianjin.MobileInspection.activity.maintenance_task;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.socks.library.KLog;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.adapter.HiddenSpinnerAdapter;
import com.tianjin.MobileInspection.customview.dialog.DateTimeDialog;
import com.tianjin.MobileInspection.entity.ContractManager;
import com.tianjin.MobileInspection.entity.HiddenSpinner;
import com.tianjin.MobileInspection.main.BaseActivity;
import com.tianjin.MobileInspection.module.ContractModule;
import com.tianjin.MobileInspection.module.MaintanenceModule;
import com.tianjin.MobileInspection.until.CheckUntil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by 吴昶 on 2017/6/9.
 *
 * 隐患审批之后的任务下发
 */
public class DailyMaintenanceActivity extends BaseActivity implements DateTimeDialog.MyOnDateSetListener{

    private final static int CHOSE_LEADING=0x453;

    private LinearLayout mlinearBack;
    private TextView mtvTitle;

    private Spinner mspContract;
    private TextView mtvleading;
    private TextView mtvFinishTime;
    private EditText medtContent;

    private ImageView mivLeading;
    private ImageView mivFinishTime;

    private Button mbtnCommit;
    private Button mbtnImmediately;

    private Spinner mspJiLiangDate;
    private HiddenSpinnerAdapter jilangAdapter;

    private HiddenSpinnerAdapter adapter;
    private ContractModule module;
    private DateTimeDialog dateTimeDialog;
    private AlertDialog.Builder tuichuDialog;

    private String contactId = "";
    private String contractId="";

    private String maintanenceId="";
    private Intent intent;

    private int currentMonth=1;
    private int choseMonth=currentMonth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_maintanence);
        initView();
        initData();
    }

    private void initData() {
        jilangAdapter=new HiddenSpinnerAdapter(this);
        adapter=new HiddenSpinnerAdapter(this);
        module=new ContractModule(handler,this);
        dateTimeDialog = new DateTimeDialog(this, null, this);
        module.getContractByType("2");

        mspContract.setAdapter(adapter);
        mspJiLiangDate.setAdapter(jilangAdapter);
        intent=getIntent();
        maintanenceId=intent.getStringExtra("id");

        Calendar calendar=Calendar.getInstance();
        currentMonth=calendar.get(Calendar.MONTH);
        choseMonth=currentMonth;

        String[] mItems=getResources().getStringArray(R.array.month);
        ArrayAdapter<String> sadapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems);
        sadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mspJiLiangDate.setAdapter(sadapter);
        mspJiLiangDate.setSelection(choseMonth);

        mspContract.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HiddenSpinner hs=(HiddenSpinner)adapter.getItem(position);
                if(hs!=null){
                    contractId=hs.getSpid();
                    KLog.d(contractId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mspJiLiangDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position+1<currentMonth){
                    choseMonth=currentMonth;
                    mspJiLiangDate.setSelection(currentMonth-1);
                }else {
                    choseMonth=position+1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initView() {
        mlinearBack=(LinearLayout)findViewById(R.id.linear_return_back);
        mtvTitle=(TextView)findViewById(R.id.tv_activity_title);
        mspContract=(Spinner)findViewById(R.id.sp_contract);
        mtvleading=(TextView) findViewById(R.id.tv_contact);
        mtvFinishTime=(TextView)findViewById(R.id.tv_finish_date);
        mivLeading=(ImageView)findViewById(R.id.iv_peple);
        mivFinishTime=(ImageView)findViewById(R.id.iv_date);
        medtContent=(EditText)findViewById(R.id.edt_maintenance_type);
        mbtnCommit=(Button)findViewById(R.id.btn_maintenance_commit);
        mbtnImmediately=(Button)findViewById(R.id.btn_immediately_maintenance);

        mspJiLiangDate=(Spinner)findViewById(R.id.sp_jiliang_date);

        mlinearBack.setVisibility(View.INVISIBLE);
        mtvTitle.setText("机动任务下发");

        mivLeading.setOnClickListener(this);
        mivFinishTime.setOnClickListener(this);
        mbtnCommit.setOnClickListener(this);
        mbtnImmediately.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.iv_peple:
                Intent intent=new Intent("android.intent.action.CONTACTCHOICEACTIVITY");
                intent.putExtra("id",contactId);
                startActivityForResult(intent, CHOSE_LEADING);
                break;
            case R.id.iv_date:
                dateTimeDialog.hideOrShow();
                break;
            case R.id.btn_maintenance_commit:
                if(checkData()) {
                    MaintanenceModule.newMaintenance(handler, maintanenceId, contractId,
                            CheckUntil.checkEditext(mtvFinishTime.getText()), contactId, CheckUntil.checkEditext(medtContent.getText()),"0",choseMonth);
                }
                break;
            case R.id.btn_immediately_maintenance:
                if(checkData()) {
                    MaintanenceModule.newMaintenance(handler, maintanenceId, contractId,
                            CheckUntil.checkEditext(mtvFinishTime.getText()), contactId, CheckUntil.checkEditext(medtContent.getText()),"1",choseMonth);
                }
                break;
        }
    }

    private boolean checkData(){
        if(contractId.equals("")){
            Toast.makeText(this,"请选择合同",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(contactId.equals("")){
            Toast.makeText(this,"请选择负责人",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(CheckUntil.checkEditext(mtvFinishTime.getText()).equals("")){
            Toast.makeText(this,"请选择完成时间",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(CheckUntil.checkEditext(medtContent.getText()).equals("")){
            Toast.makeText(this,"请填写维修方式",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(CheckUntil.checkEditext(mtvleading.getText()).equals("")){
            Toast.makeText(this,"请填写维修方式",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==CHOSE_LEADING && data!=null){
                List<String> id = data.getStringArrayListExtra("id");
                if (id != null && id.size() > 0) {
                    List<String> name = data.getStringArrayListExtra("name");
                    contactId = id.get(0);
                    mtvleading.setText(name.get(0));
                }
            }
        }
    }

    @Override
    public void resultData(Message msg) {
        super.resultData(msg);
        switch (msg.what){
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
                    adapter.updata(contract);
                }
                break;
            case MaintanenceModule.NEW_MAINTANENCE_SUCCESS:
                Toast.makeText(this,"任务下发成功",Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent("android.intent.action.TODOACTIVITY");
                startActivity(intent1);
                break;
            case MaintanenceModule.NEW_MAINTANENCE_FILED:
            case ContractModule.INT_CONTRACT_LIST_FILED:
                toLoginActivity(msg);
                break;
        }
    }

    @Override
    public void onDateSet(Date date) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        java.util.Date currentdate = new java.util.Date();
        if(date.getTime()<currentdate.getTime()){
            datebijiao("下发任务时间不能早于当前时间");
        }else {
            KLog.d(sdf.format(date));
            mtvFinishTime.setText(sdf.format(date));
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
}
