package com.tianjin.MobileInspection.activity.hiddentrouble;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
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
import com.tianjin.MobileInspection.entity.HiddenSpinner;
import com.tianjin.MobileInspection.entity.HiddenType;
import com.tianjin.MobileInspection.entity.ImageItem;
import com.tianjin.MobileInspection.entity.Road;
import com.tianjin.MobileInspection.main.BaseActivity;
import com.tianjin.MobileInspection.module.ConfigModule;
import com.tianjin.MobileInspection.module.FileUploadModule;
import com.tianjin.MobileInspection.module.InspectionModule;
import com.tianjin.MobileInspection.until.CheckUntil;
import com.tianjin.MobileInspection.until.ConnectionURL;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by wuchang on 2016-12-12.
 *
 * 新建隐患
 */
public class NewHiddenTroubleActivity extends BaseActivity{
    private static final int INT_CHOSE_IMAGE_REQUEST_CODE=0x890;
    private static final int INT_SHOW_IMAGE_REQUEST_CODE=0x891;
    private static final int INT_CHOSE_LOCATION_REQUEST_CODE=0x892;
    private static final int INT_CHOSE_HIDDEN_TYPE_REQUEST_CODE=0x893;
    private static final int INT_GET_EXPERT=0x894;

    private int INT_MAX_CHOSED_NUMBER=9;
    private LinearLayout mlinearBack;
    private LinearLayout mlineatSave;
    private TextView mtvTitle;
    private TextView mtvCommit;
    private RelativeLayout mrelatLocation;
    private LinearLayout mlinearHiddenTypeList;
    private LinearLayout mlinearHiddenNum;
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
    private HiddenTroublesAddAdapter addAdapter;

    private Spinner mspType;
    private Spinner mspHiddenType;
    private Spinner mspHiddenName;
    private Spinner mspHiddenStock;
    private Spinner mspRoad;
    private Spinner mspRoadLight;

    private HiddenSpinnerAdapter adapterType;
    private HiddenSpinnerAdapter adapterHiddenType;
    private HiddenSpinnerAdapter adapterHiddenName;
    private HiddenSpinnerAdapter adapterHiddenStock;
    private HiddenSpinnerAdapter adapterRoad;
    private HiddenSpinnerAdapter adapterRoadLight;

    private List<HiddenSpinner> listType;
    private List<HiddenSpinner> listHidden;
    private List<HiddenSpinner> listName;
    private List<HiddenSpinner> listStock;
    private List<HiddenSpinner> listRoad;
    private List<HiddenSpinner> listLight;

    private TextView mtvStockType;
    private TextView mtvStockUnit;
    private EditText medtStockNum;

    private int hiddenEmergent=0;
    private int hiddenType=0;
    private int road=0;
    private int roadnum=0;

    private double latitude;
    private double lonitude;
    private String address;
    private Intent intent;
    private String content;

    private String inspectionId;
    private InspectionModule module;
    private boolean uploadImage=false;

    private String strEmergentId;
    private String strHiddenTypeId;
    private String strHiddenNameId;
    private String strHiddenStockId;
    private String strRoadId;
    private String strLightId;
    private String unit;

    private boolean init=true;
    private List<Road> roads=new ArrayList<Road>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_new_hidden_trouble);
        initView();
        initData();
    }

    private void initData() {
//        addAdapter=new HiddenTroublesAddAdapter(this);
        hiddenList=new ArrayList<HiddenType>();
        choseImage=new ArrayList<ImageItem>();
        hiddenName=new ArrayList<String>();
        hiddennum=new ArrayList<String>();
        adapter=new ChoseImageAdapter(this);
        mgvTroubleImage.setAdapter(adapter);
        showAddItem();
        adapter.updata(choseImage);
        module=new InspectionModule(handler,this);

        long time=System.currentTimeMillis();
        Date date=new Date(time);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        mtvDate.setText(sdf.format(date));
        mtvContact.setText(MyApplication.getStringSharedPreferences("name",""));
        intent=getIntent();
        latitude=intent.getDoubleExtra("latitude",0);
        lonitude=intent.getDoubleExtra("lonitude",0);
        inspectionId=intent.getStringExtra("inspectionid");
        KLog.d("latitude:"+latitude+"   lonitude:"+lonitude);
        initSpinner();
        initSpinnerEvent();
        ConfigModule.getEmergencyType(this,handler);

        if(latitude!=0&&lonitude!=0){
            getAddressForLatLng(latitude,lonitude);
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
                        }
                    }
                    Intent intent=new Intent("android.intent.action.SHOWCHOSEDACTIVITY");
                    intent.putStringArrayListExtra("data",paths);
                    intent.putExtra("id",position);
                    intent.putExtra("show",false);
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
        medtTroubleName=(EditText)findViewById(R.id.edt_hidden_trouble_name);

        mtvContact=(TextView)findViewById(R.id.tv_hidden_trouble_contact);
        mtvDate=(TextView)findViewById(R.id.tv_hidden_trouble_date);
        mtvLocation=(TextView)findViewById(R.id.tv_hidden_trouble_location);

        mgvTroubleImage=(NobarGridView)findViewById(R.id.gv_chose_image);

        mrelatLocation=(RelativeLayout)findViewById(R.id.relat_hidden_chose_location);

        mlinearHiddenTypeList=(LinearLayout)findViewById(R.id.linear_hidden_type_list);

        mspType=(Spinner)findViewById(R.id.sp_hidden_trouble_type);
        mspRoad=(Spinner) findViewById(R.id.sp_road);
        mspRoadLight=(Spinner)findViewById(R.id.sp_road_light);
        mspHiddenType=(Spinner)findViewById(R.id.sp_hidden_type);

        mspHiddenName=(Spinner)findViewById(R.id.sp_hidden_trouble_name);
        mspHiddenStock=(Spinner)findViewById(R.id.sp_hidden_trouble_stock);

        mlinearHiddenNum=(LinearLayout)findViewById(R.id.linear_hidden_trouble_num);

        mtvStockType=(TextView)findViewById(R.id.tv_yh_detail_name);
        mtvStockUnit=(TextView)findViewById(R.id.tv_yh_detail_unit);
        medtStockNum=(EditText)findViewById(R.id.edt_yh_detail_size);

        medtContent.setFocusableInTouchMode(false);

        mlineatSave.setVisibility(View.VISIBLE);
        mtvCommit.setText("提交");
        mtvTitle.setText("新增隐患");
        mlinearBack.setOnClickListener(this);
        mlineatSave.setOnClickListener(this);
        mrelatLocation.setOnClickListener(this);
    }

    private void initSpinnerEvent() {
        //紧急类型
        mspType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hiddenEmergent=position;
                HiddenSpinner sp=(HiddenSpinner)adapterType.getItem(position);
                strEmergentId=sp.getSpid();
                KLog.d(strEmergentId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //设施类别
        mspHiddenType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hiddenType=position;
                if(listHidden.size()>position) {
                    HiddenSpinner sp=(HiddenSpinner)adapterHiddenType.getItem(position);
                    strHiddenTypeId=sp.getSpid();
                    KLog.d(strHiddenTypeId);
                    listName = MyApplication.DBM.gettHiddenSpinner().query("parentId", sp.getSpid());
                    adapterHiddenName.updata(listName);
                    initSpStock((HiddenSpinner)adapterHiddenName.getItem(0),0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                if(listHidden!=null&&listHidden.size()>0){
//                    listName=MyApplication.DBM.gettHiddenSpinner().query("parentId",((HiddenSpinner)adapterHiddenType.getItem(0)).getSpid());
//                    adapterHiddenName.updata(listName);
//                }
            }
        });
        //设施名称
        mspHiddenName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                KLog.d(position+"");
                HiddenSpinner sp=(HiddenSpinner)adapterHiddenName.getItem(position);
                initSpStock(sp,position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                if(listStock!=null){
//                    listStock=MyApplication.DBM.gettHiddenSpinner().query("parentId",((HiddenSpinner)adapterHiddenName.getItem(0)).getSpid());
//                    adapterHiddenStock.updata(listStock);
//                }
            }
        });
        //病害库
        mspHiddenStock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hiddenType=position;
                if(listStock!=null && listStock.size()>0 && !(strHiddenNameId.equals("其它")||strHiddenNameId.equals("其他"))){
                    HiddenSpinner hs=(HiddenSpinner)adapterHiddenStock.getItem(position);
                    strHiddenStockId=hs.getSpid();
                    unit=hs.getUnit();
                    mtvStockType.setText(hs.getName());
                    mtvStockUnit.setText(hs.getUnit());
                    medtContent.setText(hs.getName());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if(listStock!=null && listStock.size()>0 && !(strHiddenNameId.equals("其它")||strHiddenNameId.equals("其他"))){
                    mtvStockType.setText(((HiddenSpinner)adapterHiddenStock.getItem(0)).getName());
                    mtvStockUnit.setText(((HiddenSpinner)adapterHiddenStock.getItem(0)).getUnit());
                }
            }
        });
        //道路名称
        mspRoad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strRoadId=((HiddenSpinner)adapterRoad.getItem(position)).getSpid();
                listLight.clear();
                int max=listRoad.get(position).getId();
                for(int i=1;i<=max;i++){
                    HiddenSpinner dh=new HiddenSpinner();
                    String s;
                    if(i<10) {
                        s="00"+i;
                    }else if(i<100){
                        s="0"+i;
                    }else {
                        s=""+i;
                    }
                    dh.setName(s);
                    dh.setSpid(s);
                    listLight.add(dh);
                }
                mspRoadLight.setAdapter(adapterRoadLight);
                adapterRoadLight.updata(listLight);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //路灯号
        mspRoadLight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strLightId=((HiddenSpinner)adapterRoadLight.getItem(position)).getSpid();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void initSpStock(HiddenSpinner sp,int position){
        strHiddenNameId=sp.getSpid();
        listStock=MyApplication.DBM.gettHiddenSpinner().query("parentId",((HiddenSpinner)adapterHiddenName.getItem(position)).getSpid());
        adapterHiddenStock.updata(listStock);
        if(sp.getName().equals("其它")||sp.getName().equals("其他")){
            medtContent.setHint("请输入隐患描述");
            medtContent.setText("");
            medtContent.setFocusableInTouchMode(true);
            medtContent.setFocusable(true);
            medtContent.requestFocus();
            mlinearHiddenNum.setVisibility(View.VISIBLE);
            strHiddenStockId="";
            mtvStockUnit.setText("");
        }else {
            mlinearHiddenNum.setVisibility(View.VISIBLE);
            medtContent.setFocusable(false);
            medtContent.setFocusableInTouchMode(false);
            medtContent.setHint("");
        }
        if(listStock!=null && listStock.size()>0){
            HiddenSpinner hs=(HiddenSpinner)adapterHiddenStock.getItem(0);
            mtvStockType.setText(hs.getName());
            mtvStockUnit.setText(hs.getUnit());
            medtContent.setText(hs.getName());
            strHiddenStockId=hs.getSpid();
            unit=hs.getUnit();
        }
    }

    private void initSpinner() {
        adapterType=new HiddenSpinnerAdapter(this);
        adapterHiddenType=new HiddenSpinnerAdapter(this);
        adapterHiddenName=new HiddenSpinnerAdapter(this);
        adapterHiddenStock=new HiddenSpinnerAdapter(this);
        adapterRoad=new HiddenSpinnerAdapter(this);
        adapterRoadLight=new HiddenSpinnerAdapter(this);

        mspType.setAdapter(adapterType);
        mspHiddenType.setAdapter(adapterHiddenType);
        mspHiddenName.setAdapter(adapterHiddenName);
        mspHiddenStock.setAdapter(adapterHiddenStock);
        mspRoad.setAdapter(adapterRoad);
        mspRoadLight.setAdapter(adapterRoadLight);

        listType=new ArrayList<HiddenSpinner>();
        listHidden=new ArrayList<HiddenSpinner>();
        listName=new ArrayList<HiddenSpinner>();
        listStock=new ArrayList<HiddenSpinner>();
        listRoad=new ArrayList<HiddenSpinner>();
        listLight=new ArrayList<HiddenSpinner>();

        listHidden=MyApplication.DBM.gettHiddenSpinner().query("parentId","0");
        adapterHiddenType.updata(listHidden);

//        String[] emer=getResources().getStringArray(R.array.inspection_type);
//        for(int i=0;i<emer.length;i++){
//            HiddenSpinner hiddenSpinner=new HiddenSpinner();
//            hiddenSpinner.setName(emer[i]);
//            hiddenSpinner.setSpid("00"+i);
//            listType.add(hiddenSpinner);
//        }
        mspType.setAdapter(adapterType);

        roads=(List<Road>)intent.getSerializableExtra("roads");

        for(int i=0;i<roads.size();i++){
            HiddenSpinner asp=new HiddenSpinner();
            asp.setSpid(roads.get(i).getRoadId());
            asp.setName(roads.get(i).getRoadName());
            asp.setId(roads.get(i).getLightCount());
            listRoad.add(asp);
        }
        mspRoad.setAdapter(adapterRoad);
        adapterRoad.updata(listRoad);

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
            case R.id.relat_hidden_chose_location:
                Intent intent1=new Intent("android.intent.action.CHOSELOCATIONACTIVITY");
                intent1.putExtra("latitude",latitude);
                intent1.putExtra("lonitude",lonitude);
                intent1.putExtra("address",address);
                intent1.putExtra("isChose",true);
                startActivityForResult(intent1,INT_CHOSE_LOCATION_REQUEST_CODE);
                break;
//            case R.id.btn_add_new_detail:
//                Intent intent2=new Intent("android.intent.action.CHOSEHIDDENTYPEACTIVITY");
//                Bundle bundle=new Bundle();
//                bundle.putSerializable("hasChose",hiddenList);
//                intent2.putExtras(bundle);
//                startActivityForResult(intent2,INT_CHOSE_HIDDEN_TYPE_REQUEST_CODE);
//                break;
            case R.id.linear_save:
                if(uploadImage){
                    fileload();
                }else {
                    commitData();
                }
                break;
//            case R.id.tv_disease:
//                Intent iten=new Intent("android.intent.action.DISEASEDATEBASEACTIVITY");
//                iten.putExtra("dis",true);
//                startActivityForResult(iten,INT_GET_EXPERT);
//                break;
        }
    }

    private void commitData() {
        if(checkDataNotNull()){
            module.commitHiddenTrouble(content,String.valueOf(lonitude),String.valueOf(latitude),inspectionId
            ,strEmergentId,strHiddenTypeId,strRoadId,strLightId,medtStockNum.getText().toString(),strHiddenNameId,strHiddenStockId,unit);
        }
    }

    private boolean checkDataNotNull() {
        boolean notNull=false;
        if(CheckUntil.checkEditext(medtContent.getText()).equals("")){
            Toast.makeText(this,"请输入隐患描述",Toast.LENGTH_SHORT).show();
            return notNull;
        }
        if(latitude==0||lonitude==0){
            Toast.makeText(this,"请选择隐患位置信息",Toast.LENGTH_SHORT).show();
            return notNull;
        }
        content=medtContent.getText().toString();
        if(CheckUntil.checkEditext(medtStockNum.getText()).equals("")){
            Toast.makeText(this,"请填写具体数值",Toast.LENGTH_SHORT).show();
            return notNull;
        }
        return true;
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
            }else if(requestCode==INT_CHOSE_LOCATION_REQUEST_CODE){
                latitude=data.getDoubleExtra("latitude",0);
                lonitude=data.getDoubleExtra("lonitude",0);
                address=data.getStringExtra("address");
                mtvLocation.setText(address);
            }else if(requestCode==INT_CHOSE_HIDDEN_TYPE_REQUEST_CODE){
//                Bundle bundle=data.getExtras();
//                ArrayList<HiddenType> getList=(ArrayList<HiddenType>)bundle.getSerializable("hiddentype");
//                for(int i=0;i<getList.size();i++){
//                    for(int j=0;j<hiddenList.size();j++){
//                        if(hiddenList.get(j).getTypeId().equals(getList.get(i).getTypeId())){
//                            getList.remove(i);
//                            i--;
//                            break;
//                        }
//                    }
//                }
//                hiddenList.addAll(getList);
//                addAdapter.updata(hiddenList);
//                if(hiddenList.size()>0){
//                    mbtnNewDetail.setVisibility(View.GONE);
//                }
            }else if(requestCode==INT_GET_EXPERT){
//                String bku=data.getStringExtra("dis");
//                medtContent.setText(bku);
            }
            //
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

    public void getAddressForLatLng(double latitude,double lonitude){
        LatLng latlon=new LatLng(latitude,lonitude);

        //实例化一个地理编码查询对象
        GeoCoder geoCoder = GeoCoder.newInstance();
//        //设置反地理编码位置坐标
//        ReverseGeoCodeOption op = new ReverseGeoCodeOption();
//        op.location(latlon);
//        //发起反地理编码请求(经纬度->地址信息)
//        geoCoder.reverseGeoCode(op);
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
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                .location(latlon));
    }
    private String fileId;
    @Override
    public void resultData(Message msg) {
        super.resultData(msg);
        KLog.d(msg.what+"");
        switch (msg.what){
            case InspectionModule.INT_COMMIT_HIDDEN_TROUBLE_SUCCESS:
                if(choseImage==null||choseImage.size()==1){
                    Toast.makeText(this,"提交隐患成功",Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                fileId=msg.obj.toString();
                fileload();
                break;
            case InspectionModule.INT_COMMIT_HIDDEN_TROUBLE_FILED:
                toLoginActivity(msg);
                break;
            case FileUploadModule.INT_UPLOAD_FILES_SUCCESS:
                Toast.makeText(this,"提交隐患成功",Toast.LENGTH_SHORT).show();
                finish();
                break;
            case FileUploadModule.INT_UPLOAD_FILES_FILED:
                AlertDialog.Builder tuichuDialog = new AlertDialog.Builder(this).setMessage("图片上传失败");
                tuichuDialog.setPositiveButton("重新上传",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                fileload();
                            }
                        });
                tuichuDialog.setNegativeButton("取消",null);
                tuichuDialog.show();
                break;
            case ConfigModule.GET_EMERGENCY_SUCCESS:
                listType=(List<HiddenSpinner>)msg.obj;
                adapterType.updata(listType);
                break;

        }
    }

    private void fileload(){
        List<String> path=new ArrayList<String>();
        for(int i=0;i<choseImage.size();i++){
            if(!choseImage.get(i).isadd) {
                path.add(choseImage.get(i).getImagePath());
            }
        }
        try {
            FileUploadModule.uploadFiles(this,handler,
                    ConnectionURL.STR_HIDDEN_TROUBLE_IMAGE_COMMIT+MyApplication.getStringSharedPreferences("JSESSIONID", "")+ ConnectionURL.HTTP_SERVER_END
                    ,path,fileId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
