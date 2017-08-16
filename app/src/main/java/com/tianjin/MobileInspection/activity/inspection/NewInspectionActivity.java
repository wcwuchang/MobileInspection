package com.tianjin.MobileInspection.activity.inspection;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.socks.library.KLog;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.customview.dialog.DateTimeDialog;
import com.tianjin.MobileInspection.entity.ContractManager;
import com.tianjin.MobileInspection.entity.Road;
import com.tianjin.MobileInspection.main.BaseActivity;
import com.tianjin.MobileInspection.module.ContractModule;
import com.tianjin.MobileInspection.module.InspectionModule;
import com.tianjin.MobileInspection.until.CheckUntil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by wuchang on 2016-12-9.
 *
 * 新建巡检
 */
public class NewInspectionActivity extends BaseActivity implements DateTimeDialog.MyOnDateSetListener{

    private final int INT_CONTRACT_NAME_REQUEST = 0x89;
    private final int INT_UNIT_NAME_REQUEST = 0x88;
    private final int INT_ROAD_NAME_REQUEST = 0x87;
    private final int INT_TRAFFIC_NAME_REQUEST = 0x86;
    private final int INT_WORKER_NAME_REQUEST = 0x85;

    private LinearLayout mlinearBack;
    private LinearLayout mlinearSave;
    private TextView mtvTitle;
    private ImageView mivSave;

    private RelativeLayout mrelatContractName;
    private RelativeLayout mrelatUnitName;
    private RelativeLayout mrelatInspectionSize;
    private RelativeLayout mrelatTraffic;
    private RelativeLayout mrelatWorker;
    private RelativeLayout mrelatInspectionDate;
    private TextView mtvContractName;
    private TextView mtvUntileName;
    private TextView mtvInspectionSize;
    private TextView mtvTraffic;
    private TextView mtvWorker;
    private TextView mtvInspectionDate;

    private EditText medtContent;
    private InspectionModule module;
    private EditText medtInspectionName;

    final Calendar c = Calendar.getInstance();
    private AlertDialog.Builder tuichuDialog;
    private ArrayList<String> roads = new ArrayList<String>();
    private List<String> contact = new ArrayList<String>();
    private String contactId = "";
    private String unitId = "";
    private String trafficId = "";
    private String contractId = "";
    private Date date = null;

    private ContractModule contractmodule;
    private ArrayList<Road> roadlist;

    private DateTimeDialog dateTimeDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_new_inspection);
        initView();
        initData();
    }

    private void initData() {
        module = new InspectionModule(handler,this);
        contractmodule=new ContractModule(handler,this);
        dateTimeDialog = new DateTimeDialog(this, null, this);
    }

    private void initView() {
        mlinearBack = (LinearLayout) findViewById(R.id.linear_return_back);
        mlinearSave = (LinearLayout) findViewById(R.id.linear_add);
        mivSave = (ImageView) findViewById(R.id.iv_add);
        mtvTitle = (TextView) findViewById(R.id.tv_activity_title);

        mrelatContractName = (RelativeLayout) findViewById(R.id.relat_new_inspection_contract_name);
        mrelatUnitName = (RelativeLayout) findViewById(R.id.relat_new_inspection_unitname);
        mrelatInspectionSize = (RelativeLayout) findViewById(R.id.relat_new_inspection_size);
        mrelatTraffic = (RelativeLayout) findViewById(R.id.relat_new_inspection_traffic);
        mrelatWorker = (RelativeLayout) findViewById(R.id.relat_new_inspection_worker);
        mrelatInspectionDate = (RelativeLayout) findViewById(R.id.relat_new_inspection_date);

        mtvContractName = (TextView) findViewById(R.id.tv_new_inspection_contract_name);
        mtvUntileName = (TextView) findViewById(R.id.tv_new_inspection_unitname);
        mtvInspectionSize = (TextView) findViewById(R.id.tv_new_inspection_size);
        mtvTraffic = (TextView) findViewById(R.id.tv_new_inspection_traffic);
        mtvWorker = (TextView) findViewById(R.id.tv_new_inspection_worker);
        mtvInspectionDate = (TextView) findViewById(R.id.tv_new_inspection_date);

        medtContent = (EditText) findViewById(R.id.edt_new_inspection_content);
        medtInspectionName=(EditText)findViewById(R.id.edt_new_inspection_name);

        mlinearSave.setVisibility(View.VISIBLE);
        mivSave.setImageResource(R.drawable.list_ico_save);
        mtvTitle.setText("新建巡检");

        mlinearBack.setOnClickListener(this);
        mrelatInspectionDate.setOnClickListener(this);
        mrelatWorker.setOnClickListener(this);
        mrelatTraffic.setOnClickListener(this);
        mrelatInspectionSize.setOnClickListener(this);
        mrelatContractName.setOnClickListener(this);
        mrelatUnitName.setOnClickListener(this);
        mlinearSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.linear_return_back:
                finish();
                break;
            case R.id.relat_new_inspection_contract_name:
                toActivity("android.intent.action.NEWINSPECTIONCHOSEACTIVITY", INT_CONTRACT_NAME_REQUEST, 1);
                break;
            case R.id.relat_new_inspection_unitname:
//                toActivity("android.intent.action.NEWINSPECTIONCHOSEACTIVITY", INT_UNIT_NAME_REQUEST, 2);
                break;
            case R.id.relat_new_inspection_size:
                toActivity("android.intent.action.NEWINSPECTIONCHOSEACTIVITY", INT_ROAD_NAME_REQUEST, 3);
                break;
            case R.id.relat_new_inspection_traffic:
                toActivity("android.intent.action.NEWINSPECTIONCHOSEACTIVITY", INT_TRAFFIC_NAME_REQUEST, 4);
                break;
            case R.id.relat_new_inspection_worker:
                toActivity("android.intent.action.CONTACTCHOICEACTIVITY", INT_WORKER_NAME_REQUEST, 5);
                break;
            case R.id.relat_new_inspection_date:
                dateTimeDialog.hideOrShow();
//                date();
                break;
            case R.id.linear_add:
                saveNewInspection();
                break;
        }
    }

    private void saveNewInspection() {
        if(CheckUntil.checkEditext(medtInspectionName.getText()).equals("")){
            Toast.makeText(this, "请填写巡检名称", Toast.LENGTH_LONG).show();
            return;
        }
        if (contractId.equals("")) {
            Toast.makeText(this, "请选择合同", Toast.LENGTH_LONG).show();
            return;
        }
        if (unitId.equals("")) {
            Toast.makeText(this, "请选择巡检单位", Toast.LENGTH_LONG).show();
            return;
        }
        if (roads.size() == 0) {
            Toast.makeText(this, "请选择巡检范围", Toast.LENGTH_LONG).show();
            return;
        }
        if (trafficId.equals("")) {
            Toast.makeText(this, "请选择巡检方式", Toast.LENGTH_LONG).show();
            return;
        }
        if (CheckUntil.checkEditext(medtContent.getText()).equals("")) {
            Toast.makeText(this, "请填写巡检内容", Toast.LENGTH_LONG).show();
            return;
        }
        if (contact.size() == 0) {
            Toast.makeText(this, "请选择巡检人", Toast.LENGTH_LONG).show();
            return;
        }
        if (CheckUntil.checkEditext(mtvInspectionDate.getText()).equals("")) {
            Toast.makeText(this, "请选择巡检日期", Toast.LENGTH_LONG).show();
            return;
        }
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");

        module.saveNewInspection(medtInspectionName.getText().toString(),contractId, unitId, roads, trafficId, medtContent.getText().toString(), contactId, sdf.format(date));
    }

    private void toActivity(String action, int requestcode, int type) {
        Intent in = new Intent(action);
        if(type==3){
            Bundle bundle=new Bundle();
            bundle.putSerializable("roads",roadlist);
            bundle.putStringArrayList("roadsId",roads);
            in.putExtras(bundle);
        }else if(type==1){
            in.putExtra("id",contractId);
        }else if(type==4){
            in.putExtra("id",trafficId);
        }else if(type==5){
            in.putExtra("id",contactId);
        }
        in.putExtra("type", type);
        startActivityForResult(in, requestcode);
    }

    @Override
    public void resultData(Message msg) {
        super.resultData(msg);
        switch (msg.what) {
            case InspectionModule.INT_SAVE_NEW_INSPECTION_SUCCESS:
                Toast.makeText(this, msg.obj.toString(), Toast.LENGTH_LONG).show();
                finish();
                break;
            case ContractModule.INT_CONTRACT_DETAIL_FILED:
            case InspectionModule.INT_SAVE_NEW_INSPECTION_FILED:
                toLoginActivity(msg);
                break;
            case ContractModule.INT_CONTRACT_DETAIL_SUCCESS:
                ContractManager cm=(ContractManager)msg.obj;
                mtvUntileName.setText(cm.getUnitName());
                unitId=cm.getUnitId();
                roadlist=cm.getRoad();
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == INT_ROAD_NAME_REQUEST && data != null) {
                List<String> id = data.getStringArrayListExtra("id");
                if (id != null && id.size() > 0) {
                    List<String> name = data.getStringArrayListExtra("name");
                    roads.clear();
                    roads.addAll(id);
                    StringBuffer sb = new StringBuffer();
                    StringBuffer sb1=new StringBuffer();
                    JSONArray jsonArray=new JSONArray();
                    for (int i = 0; i < name.size(); i++) {
                        sb.append(name.get(i));
                        JSONObject jb=new JSONObject();
                        JSONObject jb1=new JSONObject();
                        try {
                            jb.put("id",id.get(i));
                            jb1.put("road",jb);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        jsonArray.put(jb1);
                        if (i != name.size() - 1) {
                            sb.append(",");
                        }
                    }
                    mtvInspectionSize.setText(sb.toString());
                }
            } else if (requestCode == INT_CONTRACT_NAME_REQUEST && data != null) {
                if (!data.getExtras().getString("id").equals("")) {
                    contractId = data.getExtras().getString("id");
                }
                KLog.d(contractId);
                contractmodule.getContractDetail(contractId);
                mtvContractName.setText(data.getExtras().getString("name"));
            } else if (requestCode == INT_WORKER_NAME_REQUEST && data != null) {
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
                    mtvWorker.setText(sb.toString());
                }
            } else if (requestCode == INT_TRAFFIC_NAME_REQUEST && data != null) {
                if (!data.getExtras().getString("id").equals("")) {
                    trafficId = data.getExtras().getString("id");
                }
                KLog.d(contractId);
                mtvTraffic.setText(data.getExtras().getString("name"));
            } else if (requestCode == INT_UNIT_NAME_REQUEST && data != null) {
                if (!data.getExtras().getString("id").equals("")) {
                    unitId = data.getExtras().getString("id");
                }
                KLog.d(contractId);
                mtvUntileName.setText(data.getExtras().getString("name"));
            }

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

    @Override
    public void onDateSet(Date date) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        java.util.Date currentdate = new java.util.Date();
        if(date.getTime()<currentdate.getTime()){
            datebijiao("新建巡检时间不能早于当前时间");
        }else {
            KLog.d(sdf.format(date));
            this.date=date;
            mtvInspectionDate.setText(sdf.format(date));
        }
    }

}
