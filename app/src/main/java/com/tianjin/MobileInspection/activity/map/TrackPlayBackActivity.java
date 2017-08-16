package com.tianjin.MobileInspection.activity.map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.api.track.HistoryTrackRequest;
import com.baidu.trace.api.track.HistoryTrackResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.api.track.TrackPoint;
import com.baidu.trace.model.ProcessOption;
import com.baidu.trace.model.TransportMode;
import com.socks.library.KLog;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.entity.HistoryTrackData;
import com.tianjin.MobileInspection.main.BaseActivity;
import com.tianjin.MobileInspection.until.DateUtils;
import com.tianjin.MobileInspection.until.GsonService;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuchang on 2016-12-28.
 *
 * 轨迹回放界面
 */
public class TrackPlayBackActivity extends BaseActivity {
    private LinearLayout mlinearBack;
    private TextView mtvTitle;

    private MapView mapview;
    private BaiduMap mBaiduMap;
    private LBSTraceClient client1;

    private LinearLayout mlinearRateMnius;
    private ImageView mivTrackPlay;
    private LinearLayout mlinearRateAdd;

    private TextView mtvStayTime;//停留时间
    private TextView mtvPlayRate;//播放速率
    private TextView mtvDistance;//巡检距离
    private TextView mtvTime;//巡检用时

    private String entryname;
    private int starttime;
    private int endtime;
    private Intent intent;

    private List<LatLng> historyPoints;
    private List<HistoryTrackData.Points> pointsInfo;
    private BitmapDescriptor mbpStart;//起始点图标
    private BitmapDescriptor mbpEnd;//终点图标
    private MarkerOptions mstartMark;
    private MarkerOptions mendMark;
    private PolylineOptions mPolyline;
    private PolylineOptions mPolyHistory;
    private MapStatusUpdate msUpdate = null;
    private long serviceId= MyApplication.BAIDU_TRACK_SERVICE_ID;
    private int pageSize = 1000;// 分页大小
    private int pageIndex = 1;// 分页索引

    private boolean playback=false;//轨迹回放
    private List<LatLng> points;

    private int playRate=1;//轨迹播放的速率

    private int index=0;
    private long staytime=0;
    private double distance=0;
    private int historySize;

    private Overlay lineOlay;
    private Overlay historyOlay;

    private int traffic=2;

//    OnTrackListener onTrackListener1=new OnTrackListener() {
//
//        @Override
//        public void onRequestFailedCallback(String s) {
//            KLog.d(s);
//        }
//
//        @Override
//        public void onQueryHistoryTrackCallback(String s) {
//            super.onQueryHistoryTrackCallback(s);
//            KLog.json(s);
//            showHistoryLine(s);
//        }
//    };

    private List<TrackPoint> histroyPoints=new ArrayList<TrackPoint>();
    private HistoryTrackRequest historyTrackRequest;

    OnTrackListener trackListener=new OnTrackListener() {
        @Override
        public void onHistoryTrackCallback(HistoryTrackResponse historyTrackResponse) {
            super.onHistoryTrackCallback(historyTrackResponse);
            KLog.d(historyTrackResponse.toString());
            distance=historyTrackResponse.getDistance();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    DecimalFormat df=new DecimalFormat("0");
                    double m=Double.valueOf(df.format(distance));
                    if(m>1000) {
                        DecimalFormat df1=new DecimalFormat("0.00");
                        mtvDistance.setText(df1.format((m/1000))+"千米");
                    }else {
                        mtvDistance.setText(m+"米");
                    }
                }
            });
            List<TrackPoint> points=historyTrackResponse.getTrackPoints();
            if(points==null){
                getHistoryLine();
                return;
            }
            histroyPoints.addAll(points);
            if(histroyPoints.size()<historyTrackResponse.getTotal()){
                pageIndex++;
                getHistoryLine();
            }else {
                List<LatLng> history = new ArrayList<LatLng>();
                for (int i = 0; i < histroyPoints.size(); i++) {
                    LatLng latLng = new LatLng(histroyPoints.get(i).getLocation().getLatitude(), histroyPoints.get(i).getLocation().getLongitude());
                    if (((latLng.latitude !=0) || (latLng.longitude !=0))) {
                        history.add(latLng);
                    }
                }
                historyPoints.addAll(0,history);
                drawHistoryPoints(historyPoints);
            }
        }

    };

    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            drawPlayback(points);
            mtvStayTime.setText(DateUtils.longToStringTime(staytime));
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_play_back);
        initView();
        initData();
    }

    private void initData() {
        client1=new LBSTraceClient(getApplicationContext());
        mbpStart = BitmapDescriptorFactory.fromResource(R.drawable.icon_qidian);
        mbpEnd = BitmapDescriptorFactory.fromResource(R.drawable.icon_zhongdian);
        points=new ArrayList<LatLng>();
        historyPoints=new ArrayList<LatLng>();
        pointsInfo=new ArrayList<HistoryTrackData.Points>();
        intent=getIntent();
        entryname=intent.getStringExtra("id");
        starttime=intent.getIntExtra("starttime",0);
        endtime=intent.getIntExtra("endtime",0);
        if(intent.hasExtra("traffic")){
            traffic=Integer.valueOf(intent.getStringExtra("traffic"));
        }
        KLog.d(entryname);
        int time=endtime-starttime;
        mtvTime.setText(time/60+"分钟");
        historyTrackRequest=new HistoryTrackRequest(1,serviceId,entryname);
        historyTrackRequest.setStartTime(starttime);
        historyTrackRequest.setEndTime(endtime);
        initProcess();
        getHistoryLine();
    }

    // 设置需要纠偏
    private void initProcess(){
        historyTrackRequest.setProcessed(true);
        ProcessOption processOption = new ProcessOption();
        // 设置需要去噪
        processOption.setNeedDenoise(true);
        // 设置需要抽稀
        processOption.setNeedVacuate(true);
        // 设置需要绑路
        processOption.setNeedMapMatch(false);
        // 设置精度过滤值(定位精度大于100米的过滤掉)
        processOption.setRadiusThreshold(100);
        // 设置交通方式为驾车
        if(traffic==1){
            processOption.setTransportMode(TransportMode.walking);
        }else if(traffic==2){
            processOption.setTransportMode(TransportMode.driving);
        }
        // 设置纠偏选项
        historyTrackRequest.setProcessOption(processOption);
//        // 设置里程填充方式为驾车
//        historyTrackRequest.setSupplementMode(SupplementMode.driving);
    }

    private void initView() {
        mlinearBack=(LinearLayout)findViewById(R.id.linear_return_back);
        mtvTitle=(TextView)findViewById(R.id.tv_activity_title);

        mapview=(MapView)findViewById(R.id.mv_map);
        mBaiduMap=mapview.getMap();
        //初始化图层
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16));//设置默认放大级别16，比例尺为200米
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, null));


        //去掉百度的logo
        View child = mapview.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)){
            child.setVisibility(View.INVISIBLE);
        }
        mapview.showScaleControl(false);//地图上比例尺
        mapview.showZoomControls(false);// 隐藏缩放控件

        mlinearRateMnius=(LinearLayout) findViewById(R.id.linear_rate_minus);
        mivTrackPlay=(ImageView)findViewById(R.id.iv_track_play);
        mlinearRateAdd=(LinearLayout) findViewById(R.id.linear_rate_add);
        mtvStayTime=(TextView)findViewById(R.id.tv_stay_time);
        mtvPlayRate=(TextView)findViewById(R.id.tv_play_rate);
        mtvDistance=(TextView)findViewById(R.id.tv_inspection_distance);
        mtvTime=(TextView)findViewById(R.id.tv_inspection_time);

        mtvPlayRate.setText(String.valueOf(playRate));
        mtvStayTime.setText("0");
        mtvTitle.setText("轨迹回放");
        mlinearRateAdd.setOnClickListener(this);
        mivTrackPlay.setOnClickListener(this);
        mlinearRateMnius.setOnClickListener(this);
        mlinearBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.linear_return_back:
                finish();
                break;
            case R.id.linear_rate_minus:
                playRate=playRate/2;
                if(playRate<1){
                    playRate=1;
                }
                mtvPlayRate.setText(playRate+"");
                break;
            case R.id.iv_track_play:
                if(historyPoints==null||historyPoints.size()<2){
                    Toast.makeText(this,"当前无轨迹点",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(playback){
                    playback=false;
                    mivTrackPlay.setImageResource(R.drawable.list_icon_play3_down);
                }else {
                    playback=true;
                    historySize=historyPoints.size();
                    KLog.d(historySize+"");
                    KLog.d(pointsInfo.size()+"");
                    TrackPlayBack();
                    mivTrackPlay.setImageResource(R.drawable.list_icon_play2_down);
                }
                break;
            case R.id.linear_rate_add:
                playRate=playRate*2;
                if(playRate>=32) playRate=playRate/32;
                mtvPlayRate.setText(playRate+"");
                break;

        }
    }

    /**
     * 获取历史轨迹点
     */
    private void getHistoryLine(){
        historyTrackRequest.setPageIndex(pageIndex);
        historyTrackRequest.setPageSize(pageSize);

        client1.queryHistoryTrack(historyTrackRequest,trackListener);
//        client1.queryHistoryTrack(serviceId , entryname, simpleReturn, processed,
//                processOption, starttime, endtime, pageSize, pageIndex, onTrackListener1);
    }

    private void showHistoryLine(String s) {

        HistoryTrackData historyTrackData = GsonService.parseJson(s,HistoryTrackData.class);
        if (historyTrackData != null && historyTrackData.getStatus() == 0) {
            if (historyTrackData.getListPoints() != null) {
                historyPoints.addAll(historyTrackData.getListPoints());
                pointsInfo.addAll(historyTrackData.getPoints());
                distance=historyTrackData.distance;
                KLog.d(historyTrackData.size+"");
                KLog.d(historyTrackData.total+"");
                /**
                 * 已获取的坐标点数如果
                 */
                if(historyPoints.size()<historyTrackData.total){
                    pageIndex++;
                    getHistoryLine();
                }else {
                    List<LatLng> data=new ArrayList<LatLng>();
                    data.addAll(historyPoints);
                    List<HistoryTrackData.Points> points=new ArrayList<HistoryTrackData.Points>();
                    points.addAll(pointsInfo);
                    historyPoints.clear();
                    pointsInfo.clear();
                    int size=data.size()-1;
                    for(int i=0;i<data.size();i++){
                        historyPoints.add(data.get(size-i));
                        pointsInfo.add(points.get(size-i));
                    }
                    //地图的数据获取及绘制时，如果要修改UI返回主线程中操作
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            drawHistoryPoints(historyPoints);
                            DecimalFormat df=new DecimalFormat("0");
                            double m=Double.valueOf(df.format(distance));
                            if(m>1000) {
                                DecimalFormat df1=new DecimalFormat("0.00");
                                mtvDistance.setText(df1.format((m/1000))+"千米");
                            }else {
                                mtvDistance.setText(m+"米");
                            }
                        }
                    });
                }
            }
        }
    }

    private void drawHistoryPoints(List<LatLng> historyPoints) {
        if(historyPoints==null||historyPoints.size()==0){
            Toast.makeText(this,"无历史轨迹",Toast.LENGTH_SHORT).show();
            return;
        }
        KLog.d(historyPoints.size()+"");
        mBaiduMap.clear();
        //设置地图显示范围
        LatLngBounds bounds = new LatLngBounds.Builder().include(historyPoints.get(0)).include(historyPoints.get(historyPoints.size()-1)).build();
        msUpdate = MapStatusUpdateFactory.newLatLngBounds(bounds);
        //添加起点坐标
        mstartMark=new MarkerOptions().position(historyPoints.get(0)).icon(mbpStart).zIndex(9).draggable(true);
        // 添加终点图标
        mendMark = new MarkerOptions().position(historyPoints.get(historyPoints.size()-1)).icon(mbpEnd).zIndex(9).draggable(true);
        //轨迹线
        mPolyline=new PolylineOptions().width(10) .color(Color.RED).points(historyPoints);

        addtoMap();
    }


    private void addtoMap() {
        if(msUpdate!=null){
            mBaiduMap.animateMapStatus(msUpdate);
        }
        if(mstartMark!=null){
            mBaiduMap.addOverlay(mstartMark);
        }
        if(mendMark!=null){
            mBaiduMap.addOverlay(mendMark);
        }
        if(mPolyline!=null){
            historyOlay=mBaiduMap.addOverlay(mPolyline);
        }

    }

    private void TrackPlayBack() {
        if(historyOlay!=null){
            historyOlay.remove();
            mPolyHistory=new PolylineOptions().width(5) .color(Color.RED).points(historyPoints);
            historyOlay=mBaiduMap.addOverlay(mPolyHistory);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (playback && index<historySize){
                        LatLng latlng = historyPoints.get(index);
                        points.add(latlng);
                    try {
                        handler.post(runnable);
                        if(index<historySize-1){
//                            staytime=Integer.parseInt(pointsInfo.get(index+1).getLoc_time())-Integer.parseInt(pointsInfo.get(index).getLoc_time());
                            staytime=(int)histroyPoints.get(index+1).getLocTime()-(int)histroyPoints.get(index).getLocTime();

                            if(staytime<=0){
                                staytime=1;
                            }
//                            Thread.sleep((long)(staytime*1000/playRate));
                        }
                        Thread.sleep((long)(1000/playRate));
                        index++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(index==historySize) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mivTrackPlay.setImageResource(R.drawable.list_icon_play3_down);
                            Toast.makeText(TrackPlayBackActivity.this, "轨迹播放结束", Toast.LENGTH_SHORT).show();
                            mtvStayTime.setText("终点");
                            points.clear();
                        }
                    });
                    index = 0;
                    playback=false;
                }
            }
        }).start();
    }


    private void drawPlayback(List<LatLng> points){
        if(lineOlay!=null){
            lineOlay.remove();
        }
//        mBaiduMap.clear();
//        mPolyHistory=new PolylineOptions().width(5) .color(Color.RED).points(historyPoints);
//        mBaiduMap.addOverlay(mPolyHistory);
        if(points.size()>=2) {
            OverlayOptions mPolyline = new PolylineOptions().width(10).color(Color.GREEN).points(points);
            lineOlay=mBaiduMap.addOverlay(mPolyline);
        }
        //添加起点坐标
//        mstartMark = new MarkerOptions().position(historyPoints.get(0)).icon(mbpStart).zIndex(9).draggable(true);
//         添加终点图标
//        mendMark = new MarkerOptions().position(historyPoints.get(historyPoints.size() - 1)).icon(mbpEnd).zIndex(9).draggable(true);
//        mBaiduMap.addOverlay(mstartMark);
//        mBaiduMap.addOverlay(mendMark);
        MyLocationData locData = new MyLocationData.Builder().accuracy(40f).direction(histroyPoints.get(index).getDirection()).latitude(points.get(points.size() - 1).latitude).longitude(points.get(points.size() - 1).longitude).build();
        mBaiduMap.setMyLocationData(locData);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(points.get(points.size() - 1));
        mBaiduMap.animateMapStatus(u);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(18));//设置默认放大级别17，比例尺为50米
    }

    @Override
    protected void onPause() {
        super.onPause();
        playback=false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        client1.onDestroy();
        mapview.onDestroy();
    }
}
