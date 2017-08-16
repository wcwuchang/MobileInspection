package com.tianjin.MobileInspection.activity.map;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.main.BaseActivity;

/**
 * Created by wuchang on 2016-12-16.
 *
 * 根据坐标展示在地图上的具体位置
 */
public class ShowLocationByLatlonActivity extends BaseActivity{

    private LinearLayout mlinearBack;
    private TextView mtvTitle;

    private MapView mapView;
    private BaiduMap mBaiduMap;
    private double latitude=0;
    private double longitude=0;
    private Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_latlon);
        initView();
        initData();
    }

    private void initData() {
        intent=getIntent();
        latitude=intent.getDoubleExtra("latitude",31.93);
        longitude=intent.getDoubleExtra("longitude",118.87);
        MyLocationData locData = new MyLocationData.Builder().accuracy(40f) .direction(100).latitude(latitude).longitude(longitude).build();
        mBaiduMap.setMyLocationData(locData);
        LatLng latLng=new LatLng(latitude,longitude);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.animateMapStatus(u);
    }

    private void initView() {
        mlinearBack=(LinearLayout)findViewById(R.id.linear_return_back);
        mtvTitle=(TextView)findViewById(R.id.tv_activity_title);
        mapView=(MapView)findViewById(R.id.mv_map);
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

        mlinearBack.setOnClickListener(this);
        mtvTitle.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.linear_return_back:
                finish();
                break;
        }
    }
}
