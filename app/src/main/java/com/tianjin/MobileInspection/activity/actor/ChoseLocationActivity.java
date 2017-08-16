package com.tianjin.MobileInspection.activity.actor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.socks.library.KLog;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.main.BaseActivity;

/**
 * Created by wuchang on 2016-12-14.
 */
public class ChoseLocationActivity extends BaseActivity{

    private LinearLayout mlinearBack;
    private LinearLayout mlinearSure;
    private TextView mtvTitle;
    private TextView mtvSure;
    private TextView mtvAddress;
    private ImageView mivlocation;

    private MapView mapView;
    private BaiduMap mBaiduMap;

    private static OverlayOptions overlay;  //覆盖物
    private BitmapDescriptor locationBit;
    private Intent intent;
    private double latitude;
    private double lonitude;
    private String address;

    private boolean isChose=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_location);
        initView();
        initData();
    }

    private void initData() {
        intent=getIntent();
        latitude=intent.getDoubleExtra("latitude",0);
        lonitude=intent.getDoubleExtra("lonitude",0);
        address=intent.getStringExtra("address");
        isChose=intent.getBooleanExtra("isChose",false);
        mtvAddress.setText(address);
        KLog.d("latitude:"+latitude+"     lonitude:"+lonitude  );
        locationBit= BitmapDescriptorFactory.fromResource(R.drawable.worker_location);
        if(latitude!=0&&lonitude!=0){
            MyLocationData locData = new MyLocationData.Builder().direction(100).latitude(latitude).longitude(lonitude).build();
            mBaiduMap.setMyLocationData(locData);
            LatLng latlon=new LatLng(latitude,lonitude);
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latlon);
            mBaiduMap.animateMapStatus(u);

        }
        /**
         * 如果是位置选择模式则设置地图的移动监听
         */
        if (isChose) {
            mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
                @Override
                public void onMapStatusChangeStart(MapStatus mapStatus) {

                }

                @Override
                public void onMapStatusChange(MapStatus mapStatus) {
                    mBaiduMap.clear();
                }

                @Override
                public void onMapStatusChangeFinish(MapStatus mapStatus) {
                    LatLng latlon = mapStatus.target;
                    latitude = latlon.latitude;
                    lonitude = latlon.longitude;
                    KLog.d(latlon.toString());
                    getAddressForLatLng(latlon);
                }
            });
        }else {
            mivlocation.setVisibility(View.GONE);
            mlinearSure.setVisibility(View.GONE);
        }
    }

    private void initView() {
        mapView=(MapView)findViewById(R.id.map_map);
        mBaiduMap=mapView.getMap();
        //初始化图层
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16));//设置默认放大级别16，比例尺为200米

        //去掉百度的logo
        View child = mapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)){
            child.setVisibility(View.INVISIBLE);
        }
        mapView.showScaleControl(false);//地图上比例尺
        mapView.showZoomControls(false);// 隐藏缩放控件

        mlinearBack=(LinearLayout)findViewById(R.id.linear_return_back);
        mlinearSure=(LinearLayout)findViewById(R.id.linear_save);
        mtvTitle=(TextView)findViewById(R.id.tv_activity_title);
        mtvSure=(TextView)findViewById(R.id.tv_activity_mun);
        mtvAddress=(TextView)findViewById(R.id.tv_location_address);
        mivlocation=(ImageView)findViewById(R.id.iv_location_bj);

        mlinearSure.setVisibility(View.VISIBLE);
        mtvTitle.setText("校准位置");
        mtvSure.setText("确定");

        mlinearBack.setOnClickListener(this);
        mlinearSure.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.linear_return_back:
                finish();
                break;
            case R.id.linear_save:
                intent.putExtra("latitude",latitude);
                intent.putExtra("lonitude",lonitude);
                intent.putExtra("address",address);
                setResult(RESULT_OK,intent);
                finish();
                break;
        }
    }

    public void getAddressForLatLng(LatLng latlon){
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
                mtvAddress.setText(address);
            }

            @Override
            public void onGetGeoCodeResult(GeoCodeResult arg0) {
            }
        });
    }
}
