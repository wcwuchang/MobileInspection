package com.tianjin.MobileInspection.activity.inspection;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
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
import com.baidu.trace.api.track.LatestPoint;
import com.baidu.trace.api.track.LatestPointRequest;
import com.baidu.trace.api.track.LatestPointResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.api.track.TrackPoint;
import com.baidu.trace.model.ProcessOption;
import com.baidu.trace.model.StatusCodes;
import com.baidu.trace.model.TransportMode;
import com.socks.library.KLog;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.activity.hiddentrouble.HiddenTroubleDetailActivity;
import com.tianjin.MobileInspection.adapter.InspectionDetailSizeAdapter;
import com.tianjin.MobileInspection.adapter.InspectionDetailYinhuanAdapter;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.customview.CircleImg;
import com.tianjin.MobileInspection.customview.ScrollListView;
import com.tianjin.MobileInspection.entity.Road;
import com.tianjin.MobileInspection.entity.Yinhuan;
import com.tianjin.MobileInspection.main.BaseActivity;
import com.tianjin.MobileInspection.module.InspectionModule;
import com.tianjin.MobileInspection.until.DateUtils;
import com.tianjin.MobileInspection.until.FlowUtil;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 巡检详情
 * Created by wuchang on 2016-12-7.
 */
@SuppressLint("NewApi")
public class InspectionDetailActivity extends BaseActivity{

    private LinearLayout mlinearBack;
    private LinearLayout mlinearShenpi;
    private TextView mtvTitle;
    private TextView mtvShenpi;
    private ImageView mivBack;

    private TextView mtvContractName;
    private TextView mtvTraffic;
    private TextView mtvContractDetail;
    private TextView mtvShowMore;

    private ScrollListView mlvInspectionSize;
    private InspectionDetailSizeAdapter sizeAdapter;
    private ScrollListView mlvInspectionYinhuan;
    private InspectionDetailYinhuanAdapter yhAdapter;
    private CircleImg mciHistoryLine;

    private boolean isFinished=false;
    private boolean isRefresh=false;

    private MapView mapview;
    private BaiduMap mBaiduMap;
    private LBSTraceClient client;
    private String inspectionId;

    private String entityName;
    private int startTime = 0;
    private int endTime = 0;
    private int simpleReturn = 0;// 是否返回精简的结果（0 : 否，1 : 是）
    private long serviceId= MyApplication.BAIDU_TRACK_SERVICE_ID;
    private int pageSize = 1000;// 分页大小
    private int pageIndex = 1;// 分页索引
    private int processed=1;// 是否返回纠偏后轨迹（0 : 否，1 : 是）
    private String processOption = "need_denoise=1,need_vacuate=1";// 纠偏选项

    private List<LatLng> historyPoints;
    private BitmapDescriptor mbpStart;
    private BitmapDescriptor mbpEnd;
    private MarkerOptions mstartMark;
    private MarkerOptions mendMark;
    private PolylineOptions mPolyline;
    private MapStatusUpdate msUpdate = null;
    private Intent intent;

    private List<Road> roads;
    private List<Road> tests;
    private List<Yinhuan> testYHs;
    private boolean testshowAll=false;
    private boolean isStartRefresh=false;
    private InspectionModule module;
    private com.tianjin.MobileInspection.entity.InspectionDetail detail;
    private BitmapDescriptor mbpAttend;
    private List<OverlayOptions> attendList;
    private LocationThread locationThread;
    private HistoryTrackRequest historyTrackRequest;
    private LatestPointRequest mRequest;
    private AtomicInteger mSequenceGenerator = new AtomicInteger();
    private Overlay pointsLineOver;
    private List<Overlay> attendOver;

    /**
     * 获取历史轨迹
     */
//    OnTrackListener onTrackListener=new OnTrackListener() {
//
//        @Override
//        public void onRequestFailedCallback(String s) {
//            KLog.d(s);
//        }
//
////        @Override
////        public void onQueryHistoryTrackCallback(String s) {
////            super.onQueryHistoryTrackCallback(s);
////            KLog.json(s);
////            KLog.d(""+startTime+"   "+endTime);
////            showHistoryLine(s);
////        }
//    };

    private List<TrackPoint> eachQueryPoints=new ArrayList<TrackPoint>();
    private List<TrackPoint> recordPoints=new ArrayList<TrackPoint>();

    OnTrackListener trackListener=new OnTrackListener() {
        @Override
        public void onHistoryTrackCallback(HistoryTrackResponse historyTrackResponse) {
            super.onHistoryTrackCallback(historyTrackResponse);
            List<TrackPoint> points=historyTrackResponse.getTrackPoints();
            if(points==null){
                KLog.d(""+(points==null));
                getHistoryLine();
                return;
            }
            eachQueryPoints.addAll(points);
            if(eachQueryPoints.size()<historyTrackResponse.getTotal()){
                pageIndex++;
                getHistoryLine();
            }else {
                List<LatLng> history = new ArrayList<LatLng>();
                int direction = 0;
                KLog.d(eachQueryPoints.size()+"");
                if(eachQueryPoints.size()>0){
                    recordPoints.addAll(eachQueryPoints);
                    for (int i = 0; i < eachQueryPoints.size(); i++) {
                        LatLng latLng = new LatLng(eachQueryPoints.get(i).getLocation().getLatitude(), eachQueryPoints.get(i).getLocation().getLongitude());
                        direction=eachQueryPoints.get(i).getDirection();
                        KLog.d(String.format("时间 = %s ,纬度 = %s ,经度 = %s ",String.valueOf(eachQueryPoints.get(i).getLocTime())
                                ,String.valueOf(latLng.latitude),String.valueOf(latLng.longitude)));
                        if (((latLng.latitude !=0) || (latLng.longitude !=0))) {
                            history.add(latLng);
                        }
                    }
                    if(history.size()>0) {
                        historyPoints.addAll(history);
                    }
                }
                eachQueryPoints.clear();
                if(!isFinished) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pageIndex=1;
                            isRefresh=true;
                            if(!locationThread.isAlive()){
                                locationThread.start();
                            }
                        }
                    });
                }
                drawHistoryPoints(historyPoints,direction);
            }
        }

        @Override
        public void onLatestPointCallback(LatestPointResponse response) {
            super.onLatestPointCallback(response);
            KLog.d(response.toString());
            if (StatusCodes.SUCCESS != response.getStatus()) {
                return;
            }
            LatestPoint point = response.getLatestPoint();
            if (null == point || (point.getLocation().getLatitude()==0&& point.getLocation().getLongitude()==0)) {
                return;
            }
            LatLng currentLatLng = new LatLng(point.getLocation().getLatitude(),point.getLocation().getLongitude());
            if (null == currentLatLng) {
                return;
            }
            historyPoints.add(currentLatLng);
            drawHistoryPoints(historyPoints,point.getDirection());
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_detail);
        initView();
        initData();
    }

    private void initData() {
        tests=new ArrayList<Road>();
        attendList=new ArrayList<OverlayOptions>();
        client=new LBSTraceClient(getApplicationContext());
        historyPoints=new ArrayList<LatLng>();
        mbpStart = BitmapDescriptorFactory.fromResource(R.drawable.icon_qidian);
        mbpEnd = BitmapDescriptorFactory.fromResource(R.drawable.icon_zhongdian);
        sizeAdapter=new InspectionDetailSizeAdapter(this);
        mlvInspectionSize.setAdapter(sizeAdapter);
        testYHs=new ArrayList<Yinhuan>();
        yhAdapter=new InspectionDetailYinhuanAdapter(this);
        mlvInspectionYinhuan.setAdapter(yhAdapter);

        intent=getIntent();//
        inspectionId=intent.getExtras().getString("inspectionId");
        entityName=inspectionId;
        KLog.d(entityName);
        historyTrackRequest=new HistoryTrackRequest(1, serviceId, entityName);
        mRequest = new LatestPointRequest(mSequenceGenerator.incrementAndGet(), serviceId, entityName);
//        client.queryLatestPoint(mRequest,trackListener);
//        getHistoryLine();
        module=new InspectionModule(handler,this);
        module.getInspectionDetail(inspectionId);

        mlvInspectionYinhuan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(InspectionDetailActivity.this,HiddenTroubleDetailActivity.class);
                intent.putExtra("id",testYHs.get(position).getYhId());
                Bundle bundle=new Bundle();
                bundle.putSerializable("roads",(Serializable)roads);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
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
        if(detail.getTraffic().equals("1")){
            processOption.setTransportMode(TransportMode.walking);
        }else if(detail.getTraffic().equals("2")){
            processOption.setTransportMode(TransportMode.driving);
        }
        // 设置纠偏选项
        historyTrackRequest.setProcessOption(processOption);
//        // 设置里程填充方式为驾车
//        historyTrackRequest.setSupplementMode(SupplementMode.driving);
    }
    private void initView() {
        mlinearBack=(LinearLayout)findViewById(R.id.linear_return_back);
        mlinearShenpi=(LinearLayout)findViewById(R.id.linear_save);
        mtvTitle=(TextView)findViewById(R.id.tv_activity_title);
        mtvShenpi=(TextView)findViewById(R.id.tv_activity_mun);
        mivBack=(ImageView)findViewById(R.id.iv_return_back);

        mtvContractName=(TextView)findViewById(R.id.tv_inspection_contract_name);
        mtvTraffic=(TextView)findViewById(R.id.tv_inspection_contract_traffic);
        mtvContractDetail=(TextView)findViewById(R.id.tv_inspection_contract_detail);
        mtvShowMore=(TextView)findViewById(R.id.tv_inspection_detail_show_more);
        mciHistoryLine=(CircleImg)findViewById(R.id.ci_inspection_show_history_line);

        mlvInspectionSize=(ScrollListView)findViewById(R.id.lv_inspection_size);
        mlvInspectionYinhuan=(ScrollListView)findViewById(R.id.lv_inspection_yinhuan);
        mtvShowMore.setVisibility(View.GONE);
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
        mtvTitle.setText("巡检详情");
        mtvShenpi.setText("审批");
        mlinearBack.setOnClickListener(this);
        mtvShowMore.setOnClickListener(this);
        mlinearShenpi.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isStartRefresh=true;
        if(!isFinished && isRefresh){
            if(!locationThread.isAlive()){
                locationThread.start();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isStartRefresh=false;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.linear_return_back:
                finish();
                break;
            case R.id.tv_inspection_detail_show_more:
                if(testshowAll){
                    tests.clear();
                    tests.add(roads.get(0));
                    tests.add(roads.get(roads.size()-1));
                    sizeAdapter.updata(tests);
                    mtvShowMore.setText("更多");
                    testshowAll=false;
                }else {
                    testshowAll=true;
                    mtvShowMore.setText("收起");
                    tests.clear();
                    tests.addAll(roads);
                    sizeAdapter.updata(tests);
                }
                break;
            case R.id.linear_save:
                isStartRefresh=false;
                Intent intent=new Intent("android.intent.action.INSPECTIONAPPROVEACTIVITY");
                startActivity(intent);
                break;
            case R.id.ci_inspection_show_history_line:
                Intent rnt=new Intent("android.intent.action.TRACKPLAYBACKACTIVITY");
                rnt.putExtra("id",entityName);
                rnt.putExtra("starttime",startTime);
                rnt.putExtra("endtime",endTime);
                if(detail!=null){
                    rnt.putExtra("traffic",detail.getTraffic());
                }
                startActivity(rnt);
//                client.onDestroy();
                break;
        }
    }

    private void drawHistoryPoints(List<LatLng> historyPoints,float direction) {
        if (historyPoints == null || historyPoints.size() == 0) {
            Toast.makeText(this, "无历史轨迹", Toast.LENGTH_SHORT).show();
            return;
        }
        KLog.d(historyPoints.size() + "");
        if(mstartMark==null){
            //添加起点坐标
            mstartMark = new MarkerOptions().position(historyPoints.get(0)).icon(mbpStart).zIndex(4).draggable(true);
            mBaiduMap.addOverlay(mstartMark);
        }
        if (isFinished && mendMark==null) {
            // 添加终点图标
            mendMark = new MarkerOptions().position(historyPoints.get(historyPoints.size() - 1)).icon(mbpEnd).zIndex(4).draggable(true);
            mBaiduMap.addOverlay(mendMark);
        }
        if(attendOver==null){
            if(attendList!=null && attendList.size()>0){
                attendOver=mBaiduMap.addOverlays(attendList);
            }
        }

        if(isFinished) {
            //设置地图显示范围
            LatLngBounds bounds = new LatLngBounds.Builder().include(historyPoints.get(0)).include(historyPoints.get(historyPoints.size() - 1)).build();
            msUpdate = MapStatusUpdateFactory.newLatLngBounds(bounds);
        }else {
            LatLng loc = historyPoints.get(historyPoints.size() - 1);
            MapStatus mapStatus = new MapStatus.Builder().target(loc).zoom(16).build();
            msUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
            MyLocationData locData = new MyLocationData.Builder().accuracy(40f).direction(direction).latitude(loc.latitude).longitude(loc.longitude).build();
            mBaiduMap.setMyLocationData(locData);
        }
        if(msUpdate!=null) {
            mBaiduMap.animateMapStatus(msUpdate);
        }
        if(pointsLineOver!=null){
            pointsLineOver.remove();
        }
        //轨迹线
        if (historyPoints.size() > 2) {
            mPolyline = new PolylineOptions().width(10).color(Color.RED).points(historyPoints);
            pointsLineOver=mBaiduMap.addOverlay(mPolyline);
        }
    }

    /**
     * 获取历史轨迹
     */
    private void getHistoryLine(){
        KLog.d("starttime="+startTime);
        KLog.d("endTime="+endTime);
        historyTrackRequest.setPageIndex(pageIndex);
        historyTrackRequest.setPageSize(pageSize);
        historyTrackRequest.setStartTime(startTime);
        historyTrackRequest.setEndTime(endTime);
        client.queryHistoryTrack(historyTrackRequest,trackListener);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRefresh=false;
        mapview.onDestroy();
    }


    @Override
    public void resultData(Message msg) {
        super.resultData(msg);
        switch (msg.what){
            case InspectionModule.INT_GET_INSPECTION_SUCCESS:
                detail=(com.tianjin.MobileInspection.entity.InspectionDetail)msg.obj;
                showData();
                break;
            case InspectionModule.INT_GET_INSPECTION_FILED:
                toLoginActivity(msg);
                break;
        }
    }

    private void showData() {
        String st=detail.getStartTime();
        KLog.d(st);
        ArrayList<LatLng> list=detail.getClocllist();
        if(list!=null&&list.size()>0){
            mbpAttend= BitmapDescriptorFactory.fromResource(R.drawable.icon_daka);
            for(int i=0;i<list.size();i++){
                MarkerOptions mop=new MarkerOptions().position(list.get(i)).icon(mbpAttend).zIndex(9).draggable(true);
                attendList.add(mop);
            }
        }
        startTime=sdfBaiduTime(st);
        if(detail.getState().equals("3")){
            isFinished=true;
            String et=detail.getEndTime();
            KLog.d(et);
            endTime=sdfBaiduTime(et);
            mciHistoryLine.setVisibility(View.VISIBLE);
            mciHistoryLine.setOnClickListener(this);
        }else {
            isFinished=false;
            endTime = (int) (System.currentTimeMillis() / 1000);
            mciHistoryLine.setVisibility(View.GONE);
            locationThread=new LocationThread();
        }
        //判断时间间隔是否大于一天
        if(endTime-startTime>86400){
            endTime=startTime+86399;
        }
        mtvTraffic.setText(FlowUtil.traffic(detail.getTraffic()));
        initProcess();
        getHistoryLine();

        mtvContractDetail.setText(detail.getContent());
        mtvContractName.setText(detail.getContractName());
        roads =detail.getRoads();
        if(roads.size()>2){
            mtvShowMore.setVisibility(View.VISIBLE);
            mtvShowMore.setText("收起");
            testshowAll=true;
        }else {
            mtvShowMore.setVisibility(View.GONE);
        }
        sizeAdapter.updata(roads);
        testYHs=detail.getYinhuans();
        yhAdapter.updata(testYHs);
    }

    private class LocationThread extends Thread{
        @Override
        public void run() {
            super.run();
            while (isStartRefresh){
                pageIndex=1;
                startTime=endTime;
                endTime= Integer.parseInt(DateUtils.getBaiduCurrentTime());
                getHistoryLine();
                try {
                    sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private int sdfBaiduTime(String str){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        Date date=null;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒",
                Locale.CHINA);
        Date date1 = new Date();
        try {
            date=sdf.parse(str);
            date1 = sdf1.parse(sdf1.format(date));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String tmptime = String.valueOf(date.getTime()).substring(0, 10);
        return Integer.parseInt(tmptime);
    }


}
