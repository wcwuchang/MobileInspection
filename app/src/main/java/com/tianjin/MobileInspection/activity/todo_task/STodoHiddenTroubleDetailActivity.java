package com.tianjin.MobileInspection.activity.todo_task;

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
import com.tianjin.MobileInspection.entity.HiddenTroubleDetail;
import com.tianjin.MobileInspection.entity.HiddenType;
import com.tianjin.MobileInspection.entity.ImageItem;
import com.tianjin.MobileInspection.entity.Todo;
import com.tianjin.MobileInspection.main.BaseActivity;
import com.tianjin.MobileInspection.module.TodoModule;
import com.tianjin.MobileInspection.until.ConnectionURL;
import com.tianjin.MobileInspection.until.FileUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wuchang on 2016-12-12.
 *
 * 待办隐患详情
 *
 * 废弃
 */
public class STodoHiddenTroubleDetailActivity extends BaseActivity{
    private static final int INT_CHOSE_IMAGE_REQUEST_CODE=0x890;
    private static final int INT_SHOW_IMAGE_REQUEST_CODE=0x891;

    private LinearLayout mlinearBack;
    private LinearLayout mlineatSave;
    private TextView mtvTitle;
    private TextView mtvCommit;
    private RelativeLayout mrelatLocation;
//    private Button mbtnNewDetail;
    private LinearLayout mlinearHiddenTypeList;
    private EditText medtTroubleName;
    private EditText medtContent;
    private TextView mtvContact;
    private TextView mtvDate;
    private TextView mtvLocation;
    private LinearLayout mlinearLocation;

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
    private String title;
    private String content;

    private String inspectionId;
    private Todo todo;
    private TodoModule module;
    private HiddenTroubleDetail detail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_new_hidden_trouble);
        initView();
        initData();
    }

    private void initData() {
        addAdapter=new HiddenTroublesAddAdapter(this);
        adapter=new ChoseImageAdapter(this);
        hiddenList=new ArrayList<HiddenType>();
        choseImage=new ArrayList<ImageItem>();
        hiddenName=new ArrayList<String>();
        hiddennum=new ArrayList<String>();

        mgvTroubleImage.setAdapter(adapter);
//        mlvTroublelist.setAdapter(addAdapter);
        module=new TodoModule(handler,this);

        long time=System.currentTimeMillis();
        Date date=new Date(time);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        mtvDate.setText(sdf.format(date));
        mtvContact.setText(MyApplication.getStringSharedPreferences("name",""));
        intent=getIntent();

        if(latitude!=0&&lonitude!=0){
            getAddressForLatLng(latitude,lonitude);
        }

        mgvTroubleImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ArrayList<String> paths=new ArrayList<String>();
                    for(ImageItem ii:choseImage){
                            paths.add(ii.getImagePath());
                    }
                    Intent intent=new Intent("android.intent.action.SHOWIMAGEINTNETDACTIVITY");
                    intent.putStringArrayListExtra("data",paths);
                    intent.putExtra("id",position);
                    intent.putExtra("show",true);
                    startActivityForResult(intent,INT_SHOW_IMAGE_REQUEST_CODE);
            }
        });

        boolean isGET=intent.getBooleanExtra("GET",false);
        if(isGET){
//            mlineatSave.setVisibility(View.VISIBLE);
            mtvCommit.setText("审批");
            todo=(Todo)intent.getSerializableExtra("todo");
            if(todo.getStatus()==-1){
                mlineatSave.setVisibility(View.GONE);
            }else {
                mlineatSave.setVisibility(View.VISIBLE);
            }
            module.getGormUrl(todo.getTaskId(),todo.getTaskName(),todo.getTaskDefKey(),todo.getProcInsId(),todo.getProcDefId());
        }else {
            mlineatSave.setVisibility(View.GONE);
            detail=(HiddenTroubleDetail)intent.getSerializableExtra("detail");
            showData();
        }
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

//        mbtnNewDetail=(Button)findViewById(R.id.btn_add_new_detail);
        mlinearHiddenTypeList=(LinearLayout)findViewById(R.id.linear_hidden_type_list);
        mlvTroublelist=(ScrollListView)findViewById(R.id.lv_yihuan_maintenance_trouble);
        mlinearLocation=(LinearLayout)findViewById(R.id.linear_hidden_trouble_location);
        mlinearLocation.setOnClickListener(this);

        medtTroubleName.setFocusable(false);
        medtContent.setFocusable(false);
        mtvTitle.setText("隐患详情");
        mlinearBack.setOnClickListener(this);
        mlineatSave.setOnClickListener(this);
//        mbtnNewDetail.setVisibility(View.GONE);
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
                intent1.putExtra("procInsId",detail.getProcInsId());
                intent1.putExtra("options",detail.getOption());
                intent1.putExtra("id",detail.getTroubleId());
                intent1.putExtra("taskId",detail.getTaskId());
                intent1.putExtra("url", ConnectionURL.STR_HIDDEN_TROUBLE_SAVEAUDIT);
                startActivity(intent1);
                break;
            case R.id.linear_hidden_trouble_location:
                Intent iten=new Intent("android.intent.action.SHOWLOCATIONBYLATLONACTIVITY");
                iten.putExtra("latitude",latitude);
                iten.putExtra("longitude",lonitude);
                startActivity(iten);
                break;
        }
    }

    public void getAddressForLatLng(double latitude,double lonitude){
        LatLng latlon=new LatLng(latitude,lonitude);
        KLog.d(latlon.toString());
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
            case TodoModule.INT_GET_FORM_URL_FILED:
            case TodoModule.INT_GET_TODO_DETAIL_FILED:
                toLoginActivity(msg);
                break;

        }
    }

    private  void showData(){
        if(detail==null) return;
        StringBuffer sb1=new StringBuffer();
        medtTroubleName.setText(detail.getTitle());
        medtContent.setText(detail.getContent());
        mtvDate.setText(detail.getDate());
        mtvContact.setText(detail.getInspectionName());
        latitude=detail.getLatitude();
        lonitude=detail.getLongitude();
        getAddressForLatLng(latitude,lonitude);
        addAdapter.updata(detail.getHiddenTypes());
//        ScreenUtils.setListViewHeightBasedOnChildren(getApplicationContext(),mlvTroublelist);
        final List<String> path=detail.getImage();
        if(path!=null&&path.size()>0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i=0;i<path.size();i++){
                        ImageItem imageItem=new ImageItem();
                        imageItem.setImagePath(FileUtil.saveIntenetImage(STodoHiddenTroubleDetailActivity.this,path.get(i)));
                        KLog.d(imageItem.getImagePath());
                        choseImage.add(imageItem);
                        handler.post(runnable);
                    }
                }
            }).start();
        }
    }

    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            adapter.updata(choseImage);
        }
    };

}
