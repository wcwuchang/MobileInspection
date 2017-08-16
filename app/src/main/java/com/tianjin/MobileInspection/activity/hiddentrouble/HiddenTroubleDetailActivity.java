package com.tianjin.MobileInspection.activity.hiddentrouble;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.socks.library.KLog;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.adapter.ChoseImageAdapter;
import com.tianjin.MobileInspection.adapter.HiddenTroublesAddAdapter;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.customview.NobarGridView;
import com.tianjin.MobileInspection.customview.ScrollListView;
import com.tianjin.MobileInspection.entity.HiddenSpinner;
import com.tianjin.MobileInspection.entity.HiddenTroubleDetail;
import com.tianjin.MobileInspection.entity.HiddenType;
import com.tianjin.MobileInspection.entity.ImageItem;
import com.tianjin.MobileInspection.entity.Road;
import com.tianjin.MobileInspection.main.BaseActivity;
import com.tianjin.MobileInspection.module.ConfigModule;
import com.tianjin.MobileInspection.module.InspectionModule;
import com.tianjin.MobileInspection.until.FileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuchang on 2016-12-12.
 *
 * 隐患详情
 */
public class HiddenTroubleDetailActivity extends BaseActivity{
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

    private InspectionModule module;
    private HiddenTroubleDetail detail;

    private TextView mtxEmergencyType;
    private TextView mtxHiddenType;
    private TextView mtxHiddenName;
    private TextView mtxStock;
    private TextView mtxRoad;
    private TextView mtxRoadNum;

    private List<Road> roads;

    private LinearLayout mlinearStockNum;
    private TextView mtvStockName;
    private EditText medtStockNum;
    private TextView mtvUnit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_hidden_trouble_detail);
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
        troubleId=intent.getStringExtra("id");
        roads=(List<Road>)intent.getSerializableExtra("roads");
        module=new InspectionModule(handler,this);
        module.getHiddenTroubleDetail(troubleId);

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

        mtvTitle.setText("隐患详情");
        mlinearBack.setOnClickListener(this);
        mrelatLocation.setOnClickListener(this);
        medtStockNum.setFocusable(false);
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
        }
    }


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
    private String fileId;
    @Override
    public void resultData(Message msg) {
        super.resultData(msg);
        switch (msg.what){
            case InspectionModule.INT_GET_HIDDEN_TROUBLE_SUCCESS:
                detail=(HiddenTroubleDetail)msg.obj;
                showData();
                break;
            case InspectionModule.INT_GET_HIDDEN_TROUBLE_FILED:
                toLoginActivity(msg);
                break;
            case ConfigModule.GET_EMERGENCY_SUCCESS:
                List<HiddenSpinner> emer=(List<HiddenSpinner>)msg.obj;
                for(int i=0;i<emer.size();i++){
                    if(detail.getEmergencyType().equals(emer.get(i).getSpid())){
                        mtxEmergencyType.setText(emer.get(i).getName());
                    }
                }
                break;

        }
    }

    private void showData() {
        if(detail==null) return;
//        medtTroubleName.setText(detail.getTitle());
        KLog.d(detail.toString());
        medtContent.setText(detail.getContent());
        mtvContact.setText(detail.getInspectionName());
        mtvDate.setText(detail.getDate());
        latitude=detail.getLatitude();
        lonitude=detail.getLongitude();
        getAddressForLatLng(latitude,lonitude);
        addAdapter.updata(detail.getHiddenTypes());
//        ScreenUtils.setListViewHeightBasedOnChildren(getApplicationContext(),mlvTroublelist);
        final List<String> path=detail.getImage();
        if(path!=null&&path.size()>0){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i=0;i<path.size();i++){
                        ImageItem imageItem=new ImageItem();
                        imageItem.setImagePath(FileUtil.saveIntenetImage(HiddenTroubleDetailActivity.this,path.get(i)));
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
        if(name!=null) {
            mtxHiddenName.setText(name.get(0).getName());
            if(name.get(0).getName().equals("其它")||name.get(0).getName().equals("其他")){
                mtxStock.setText("其它");
                mlinearStockNum.setVisibility(View.GONE);
            }else {
                mtvStockName.setText(detail.getContent());
                mtxStock.setText(detail.getContent());
                mlinearStockNum.setVisibility(View.VISIBLE);
                mtvUnit.setText(detail.getUnit());
                medtStockNum.setText(detail.getQuantity());
            }
        }
        if(roads!=null) {
            for(int i=0;i<roads.size();i++) {
                if(roads.get(i).getRoadId().equals(detail.getRoadId())) {
                    mtxRoad.setText(roads.get(i).getRoadName());
                }
            }
        }
        mtxRoadNum.setText(detail.getLightId());


    }

    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            adapter.updata(choseImage);
        }
    };


}
