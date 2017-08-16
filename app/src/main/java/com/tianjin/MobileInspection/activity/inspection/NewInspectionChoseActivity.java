package com.tianjin.MobileInspection.activity.inspection;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.socks.library.KLog;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.adapter.NewInspectionChoseAdapter;
import com.tianjin.MobileInspection.customview.pullReflashView.PullToRefreshLayout;
import com.tianjin.MobileInspection.customview.pullReflashView.PullableListView;
import com.tianjin.MobileInspection.entity.NewInspectionChose;
import com.tianjin.MobileInspection.entity.Road;
import com.tianjin.MobileInspection.main.BaseActivity;
import com.tianjin.MobileInspection.module.ContactsModule;
import com.tianjin.MobileInspection.module.ContractModule;
import com.tianjin.MobileInspection.module.InspectionModule;
import com.tianjin.MobileInspection.module.NewInspectionChoseModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuchang on 2016-12-9.
 *
 * 新建选择巡检
 */
public class NewInspectionChoseActivity extends BaseActivity{

    private PullToRefreshLayout pullToRefreshLayout;
    private PullableListView mPLVContractList;

    private LinearLayout mlinearBack;
    private LinearLayout mlinearSave;
    private TextView mtvTitle;
    private ImageView mivSave;

    private EditText medtSeach;
    private ImageView mivSeach;
    private TextView mtvNoData;

    private Intent intent;
    private ArrayList<NewInspectionChose> alldata;
    private ArrayList<NewInspectionChose> showdata;
    private ArrayList<NewInspectionChose> getdata;

    private int pageNo=1;
    private int pageSize=20;

    private int type=0;//1、合同名称 2、巡检单位 3、巡检范围 4、交通工具
    private boolean isRefrash=false;
    private boolean isLoadmore=false;
    private int choseIndex=-1;

    private NewInspectionChoseModule conModule;
    private NewInspectionChoseAdapter adapter;

    private String choseid=null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_inspection_chose);
        initView();
        initData();
    }

    private void initData() {
        mlinearSave.setVisibility(View.VISIBLE);
        intent=getIntent();
        type=intent.getExtras().getInt("type");
        choseid=intent.getExtras().getString("id");
        alldata=new ArrayList<NewInspectionChose>();
        showdata=new ArrayList<NewInspectionChose>();
        getdata=new ArrayList<NewInspectionChose>();
        settitle(type);
        adapter=new NewInspectionChoseAdapter(this);
        mPLVContractList.setAdapter(adapter);
        conModule=new NewInspectionChoseModule(handler);
        getData(type);

        mPLVContractList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(showdata.get(position).getIsChose()) {
                        showdata.get(position).setIsChose(false);
                        choseid="";
                    }else {
                        showdata.get(position).setIsChose(true);
                        choseid=showdata.get(position).getId();
                    }
                    suerChosed();
                    adapter.updata(showdata);
            }
        });

        medtSeach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                seachChose(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initView() {
        mlinearBack=(LinearLayout)findViewById(R.id.linear_return_back);
        mlinearSave=(LinearLayout)findViewById(R.id.linear_add);
        mivSave=(ImageView)findViewById(R.id.iv_add);
        mtvTitle=(TextView)findViewById(R.id.tv_activity_title);

        medtSeach=(EditText)findViewById(R.id.edt_seach);
        mivSeach=(ImageView) findViewById(R.id.iv_seach_show);
        mtvNoData=(TextView) findViewById(R.id.tv_no_seach);

        pullToRefreshLayout=(PullToRefreshLayout)findViewById(R.id.prelat_layout);
        mPLVContractList=(PullableListView)findViewById(R.id.lv_pullreflash);
        pullToRefreshLayout.setOnRefreshListener(new MyListener());

        mivSave.setImageResource(R.drawable.list_ico_save);
        mlinearBack.setOnClickListener(this);
        mlinearSave.setOnClickListener(this);
    }

    /**
     * 根据选择的类型设置标题
     * @param type
     */
    private void settitle(int type){
        switch (type){
            case 1:
                mtvTitle.setText("选择合同");
                break;
            case 2:
                mtvTitle.setText("选择巡检单位");
                break;
            case 3:
                mtvTitle.setText("选择巡检范围");
                break;
            case 4:
                mtvTitle.setText("选择巡检方式");
                break;
            case 6:
                mtvTitle.setText("选择隐患");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.linear_return_back:
                finish();
                break;
            case R.id.linear_add:
                if(type==3) {
                    ArrayList<String> id = new ArrayList<String>();
                    ArrayList<String> name = new ArrayList<String>();
                    for (int i = 0; i < showdata.size(); i++) {
                        if (showdata.get(i).getIsChose()) {
                            id.add(showdata.get(i).getId());
                            name.add(showdata.get(i).getName());
                        }
                    }
                    intent.putStringArrayListExtra("id", id);
                    intent.putStringArrayListExtra("name", name);
                }else{
                    String id="";
                    String name="";
                    String data="";
                    for (int i = 0; i < showdata.size(); i++) {
                        if (showdata.get(i).getIsChose()) {
                            id=showdata.get(i).getId();
                            name=showdata.get(i).getName();
                            if(type==6){
                                data=showdata.get(i).getMap().toString();
                                intent.putExtra("data",data);
                            }
                        }
                    }
                    intent.putExtra("id",id);
                    intent.putExtra("name",name);
                    KLog.d(id);
                }
                setResult(RESULT_OK,intent);
                finish();
                break;
        }
    }

    //根据输入的数据搜索数据
    private void seachChose(String str){
        showdata.clear();
        if(str.equals("")){
            showdata.addAll(alldata);
            mivSeach.setVisibility(View.VISIBLE);
        }else{
            mivSeach.setVisibility(View.GONE);
            for(int i=0;i<alldata.size();i++){
                if(alldata.get(i).getName().indexOf(str)>=0
                        || str.indexOf(alldata.get(i).getName())>=0){
                    showdata.add(alldata.get(i));
                }
            }
        }
        suerChosed();
        adapter.updata(showdata);
    }


    private void suerChosed(){
        /**
         * 除了巡检范围以外的都是单选
         */
        if(type!=3 ) {
            for (int i = 0; i < showdata.size(); i++) {
                if (showdata.get(i).getId().equals(choseid)) {
                    showdata.get(i).setIsChose(true);
                } else {
                    showdata.get(i).setIsChose(false);
                }
            }
        }
    }

    private void getData(int type){
        switch (type){
            case 1:
                choseid=intent.getStringExtra("id");
                conModule.getContracts(this,pageNo,pageSize);
                break;
            case 2:

                break;
            case 3:
                List<Road> roads=(List<Road>)intent.getSerializableExtra("roads");
                List<String> ids=intent.getStringArrayListExtra("roadsId");
                getdata.clear();
                if(roads!=null) {
                    for (int i = 0; i < roads.size(); i++) {
                        NewInspectionChose nic = new NewInspectionChose();
                        nic.setName(roads.get(i).getRoadName());
                        nic.setId(roads.get(i).getRoadId());
                        KLog.d(nic.getId());
                        for (int j = 0; j < ids.size(); j++) {
                            KLog.d(ids.get(j));
                            if (ids.get(j).equals(nic.getId())) {
                                nic.setIsChose(true);
                            }
                        }
                        getdata.add(nic);
                    }
                    showData();
                }
                break;
            case 4:
                choseid=intent.getStringExtra("id");
                NewInspectionChose new1=new NewInspectionChose();
                new1.setId("1");
                new1.setName("步行");
                new1.setIsChose(false);
                NewInspectionChose new2=new NewInspectionChose();
                new2.setId("2");
                new2.setName("开车");
                new2.setIsChose(false);
                ArrayList<NewInspectionChose> data1=new ArrayList<NewInspectionChose>();
                data1.add(new1);
                data1.add(new2);
                Message msg1=new Message();
                msg1.what=45;
                msg1.obj=data1;
                handler.sendMessage(msg1);
                break;
            case 6:
                conModule.getTroubles(this,pageNo,pageSize);
                break;

        }
    }

    @Override
    public void resultData(Message msg) {
        super.resultData(msg);
        switch (msg.what){
            case 45:
            case ContractModule.INT_CONTRACT_LIST_SUCCESS:
            case InspectionModule.INT_GET_OFFICE_LIST_SUCCESS:
            case InspectionModule.INT_GET_ROAD_SUCCESS:
            case InspectionModule.INT_GET_TROUBLES_LIST_SUCCESS:
                getdata=(ArrayList<NewInspectionChose>)msg.obj;
                showData();
                break;
            case InspectionModule.INT_GET_ROAD_FILED:
            case InspectionModule.INT_GET_OFFICE_LIST_FILED:
            case ContactsModule.INT_GET_CONTACTS_FILEAD:
                toLoginActivity(msg);
                stopRefreshOrloadmoreFiled();
                break;
            case 748:
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                break;
            case 749:
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                break;
        }

    }

    private void showData() {
        KLog.d(getdata.size()+"");
            if(pageNo==1) {
                alldata.clear();
                showdata.clear();
            }
            alldata.addAll(getdata);
            showdata.addAll(alldata);
            if(choseid!=null) {
                for (int i = 0; i < showdata.size(); i++) {
                    if (choseid.equals(showdata.get(i).getId())) {
                        showdata.get(i).setIsChose(true);
                        choseIndex = i;
                        break;
                    }
                }
            }
            adapter.updata(showdata);
        stopRefreshOrloadmoreSuccess();
    }

    private void stopRefreshOrloadmoreSuccess(){
        if(isRefrash){
            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            isRefrash=false;
        }
        if(isLoadmore){
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            isLoadmore=false;
        }
    }
    private void stopRefreshOrloadmoreFiled(){
        if(isRefrash){
            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
            isRefrash=false;
        }
        if(isLoadmore){
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
            isLoadmore=false;
        }
    }

    //PullableListView下拉上拉监听
    public class MyListener implements PullToRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
            isRefrash=true;
            pageNo=1;
            switch (type){
                case 1:
                    conModule.getContracts(NewInspectionChoseActivity.this,pageNo,pageSize);
                    break;
                case 2:
                case 3:
                case 4:
                    handler.sendEmptyMessageDelayed(749,1000);
                    break;
                case 6:
                    conModule.getTroubles(NewInspectionChoseActivity.this,pageNo,pageSize);
                    break;

            }

        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            isLoadmore=true;
            if(getdata.size()<pageSize){
                Toast.makeText(NewInspectionChoseActivity.this,"已加载所有数据",Toast.LENGTH_LONG).show();
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }else {
                pageNo++;
                switch (type){
                    case 1:
                        conModule.getContracts(NewInspectionChoseActivity.this,pageNo,pageSize);
                        break;
                    case 2:
                    case 3:
                    case 4:
                        handler.sendEmptyMessageDelayed(748,1000);
                        break;
                    case 6:
                        conModule.getTroubles(NewInspectionChoseActivity.this,pageNo,pageSize);
                        break;

                }
            }
        }

    }
}
