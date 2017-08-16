package com.tianjin.MobileInspection.activity.map;

import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
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
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.baidu.trace.api.entity.LocRequest;
import com.baidu.trace.api.entity.OnEntityListener;
import com.baidu.trace.api.track.HistoryTrackRequest;
import com.baidu.trace.api.track.HistoryTrackResponse;
import com.baidu.trace.api.track.LatestPoint;
import com.baidu.trace.api.track.LatestPointRequest;
import com.baidu.trace.api.track.LatestPointResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.api.track.TrackPoint;
import com.baidu.trace.model.LocationMode;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.ProcessOption;
import com.baidu.trace.model.PushMessage;
import com.baidu.trace.model.StatusCodes;
import com.baidu.trace.model.TraceLocation;
import com.socks.library.KLog;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.customview.CircleImg;
import com.tianjin.MobileInspection.entity.InspectionChoose;
import com.tianjin.MobileInspection.entity.Road;
import com.tianjin.MobileInspection.main.BaseActivity;
import com.tianjin.MobileInspection.module.InspectionModule;
import com.tianjin.MobileInspection.service.ScreenListener;
import com.tianjin.MobileInspection.until.DateUtils;
import com.tianjin.MobileInspection.until.FileUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wuchang on 2016-12-5.
 * <p>
 * 实时巡检
 * 实时轨迹页面
 */
public class LoactionTrackActivity extends BaseActivity {

    // 轨迹服务ID
    private long serviceId = MyApplication.BAIDU_TRACK_SERVICE_ID;
    // 设备名称
    private String entryName ;
    //轨迹服务类型（0 : 不建立socket长连接， 1 : 建立socket长连接但不上传位置数据，2 : 建立socket长连接并上传位置数据）
    private int traceType = 2;
    private Trace trace;//轨迹服务
    private LBSTraceClient client;//轨迹服务客户端
    int gatherInterval = 2;// 采集周期
    int packInterval = 10;// 打包周期
    int protocolType = 0;// http协议类型 0http,1 https
    private static MapStatusUpdate msUpdate = null;
    private static BitmapDescriptor realtimeBitmap;  //图标
    private static OverlayOptions overlay;  //覆盖物
    private static PolylineOptions polyline = null;  //路线覆盖物
    private RefreshThread refreshThread;

    private List<LatLng> pointList;//设备的轨迹点集合
    private List<LatLng> historypointList;//设备的轨迹点集合
    private MapView mapview;
    private BaiduMap mBaiduMap;

    private final static int INT_TWO_CODE_REQUESTCODE = 0x5623;
    private CircleImg mciYinhuan;
    private CircleImg mciTwoCode;
    private CircleImg mciFinished;
    private double latitude;//纬度
    private double lonitude;//经度
    private boolean isActivityShow = true;
    private String taskId="";
    private String taskDefKey="";
    private String procInsId="";
    private String procDefKey="";
    private String beginDate;
    //已走轨迹时间判断
    private int starttime;
    private int endtime;
    private InspectionModule module;

    private List<OverlayOptions> attendList;
    private BitmapDescriptor mbpAttend;
    private boolean isTraceStarted = false;
    private String inspectionId;

    private int simpleReturn = 0;// 是否返回精简的结果（0 : 否，1 : 是）
    private int processed = 1;// 是否返回纠偏后轨迹（0 : 否，1 : 是）
    private String processOption = "";// 纠偏选项
    private int pageSize = 5000;// 分页大小
    private int pageIndex = 1;// 分页索引

    private int state=0;

    private LocRequest mlocRequest=new LocRequest(serviceId);

    /**
     * 锁屏监听
     */
    private ScreenListener screenListener;

    /**
     * 是否锁屏
     */
    private boolean keyguardManager=false;

    private LatestPointRequest mRequest;

    private HistoryTrackRequest historyTrackRequest;

    private Intent intent;

    private List<TrackPoint> historypoints;

    private List<Road> roads=new ArrayList<Road>();

    private Handler trackHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            Toast.makeText(LoactionTrackActivity.this,(String)msg.obj,Toast.LENGTH_SHORT).show();
            switch (msg.what) {
                case 0:
                case 10006:
                case 10008:
                case 10009:
                    isTraceStarted = true;
                    break;

                case 1:
                case 10004:
                    isTraceStarted = false;
                    break;

                default:
                    break;
            }
            KLog.d("ss");
            client.startGather(traceListener);
        }
    };

    /**
     * 轨迹服务监听器
     */
    private OnTraceListener traceListener = new OnTraceListener() {
        @Override
        public void onStartTraceCallback(int i, String s) {
            if (StatusCodes.SUCCESS == i || StatusCodes.START_TRACE_NETWORK_CONNECT_FAILED <= i) {
                KLog.d(s);
                KLog.d(i + "");
                trackHandler.obtainMessage(i, "开启轨迹服务回调接口消息 [消息编码 : " + i + "，消息内容 : " + s + "]").sendToTarget();
                if (state==1) {
                    module.getHasDoingInspection(inspectionId);
                } else {
                    if(taskId.equals("")) {
                        taskId = intent.getStringExtra("taskId");
                        taskDefKey = intent.getStringExtra("taskDefKey");
                        procInsId = intent.getStringExtra("procInsId");
                        procDefKey = intent.getStringExtra("procDefKey");
                    }
                    beginDate = intent.getStringExtra("beginDate");
                    KLog.d("asdf", beginDate);
                    // 开始时间
                    starttime = Integer.parseInt(DateUtils.getTimeToStamp(beginDate));
                    endtime = Integer.parseInt(DateUtils.getBaiduCurrentTime());
                    if (endtime - starttime > MyApplication.ONE_DAY_TIME) {
                        starttime = endtime - MyApplication.ONE_DAY_TIME + 1000;
                    }
                    KLog.d(starttime + "");
                    KLog.d(endtime + "");
                    // 创建历史轨迹请求实例
                    historyTrackRequest = new HistoryTrackRequest(1, serviceId, entryName);
                    getHistoryLine();
                }
            }
        }

        @Override
        public void onStopTraceCallback(int i, String s) {
            if (StatusCodes.SUCCESS == i || StatusCodes.CACHE_TRACK_NOT_UPLOAD == i) {
                KLog.d("onStopTraceCallback", s);
                KLog.d("onStopTraceCallback", i + "");

            }
        }

        @Override
        public void onStartGatherCallback(int i, String s) {
            if (StatusCodes.SUCCESS == i || StatusCodes.GATHER_STARTED == i) {
                KLog.d("onStartGatherCallback", s);
                KLog.d("onStartGatherCallback", i + "");
            }
        }

        @Override
        public void onStopGatherCallback(int i, String s) {
            if (StatusCodes.SUCCESS == i || StatusCodes.GATHER_STOPPED == i) {
                KLog.d("onStopGatherCallback", s);
                KLog.d("onStopGatherCallback", i + "");
            }
        }

        @Override
        public void onPushCallback(byte b, PushMessage pushMessage) {

        }
    };
    /**
     * 轨迹监听器(用于接收纠偏后实时位置回调)
     */
    private OnTrackListener trackListener = new OnTrackListener() {

        @Override
        public void onHistoryTrackCallback(HistoryTrackResponse response) {
            super.onHistoryTrackCallback(response);
            //历史轨迹查询接口
            KLog.d("data",response.toString());
            if(response.getTrackPoints()!=null) {
                historypoints.addAll(response.getTrackPoints());
            }
            if(historypoints.size()<response.getTotal()){
                pageIndex++;
                getHistoryLine();
            }else {
                List<LatLng> history = new ArrayList<LatLng>();
                for (int i = 0; i < historypoints.size(); i++) {
                    LatLng latLng = new LatLng(historypoints.get(i).getLocation().getLatitude(), historypoints.get(i).getLocation().getLongitude());
                    KLog.d("data",latLng.latitude+"  "+latLng.longitude);
                    int size = history.size() - 1;
                    if (size >= 0) {
                        if (!((latLng.latitude == history.get(size).latitude) && (latLng.longitude == history.get(size).longitude))) {
                            history.add(latLng);
                        }
                    } else {
                        history.add(latLng);
                    }
                }
                KLog.d("data",history.size()+"");
                pointList.addAll(0,history);
//                drawRealtimePoint(pointList.get(pointList.size()-1), historypoints.get(historypoints.size()-1).getRadius(), historypoints.get(historypoints.size()-1).getDirection());
            }
        }

        @Override
        public void onLatestPointCallback(LatestPointResponse response) {
            super.onLatestPointCallback(response);
            //实时位置查询接口
            KLog.d(response.toString());
            if (StatusCodes.SUCCESS != response.getStatus()) {
                return;
            }
            LatestPoint point = response.getLatestPoint();
            if (null == point || (point.getLocation().getLatitude()==0&& point.getLocation().getLongitude()==0)){
                return;
            }
            LatLng currentLatLng = new LatLng(point.getLocation().getLatitude(),point.getLocation().getLongitude());
            if (null == currentLatLng) {
                return;
            }
            pointList.add(currentLatLng);
            if(isActivityShow) {
                drawRealtimePoint(currentLatLng, point.getRadius(), point.getDirection());
            }
        }
    };

    /**
     * Entity监听器(用于接收实时定位回调)
     */
    private OnEntityListener entityListener = new OnEntityListener() {
        @Override
        public void onReceiveLocation(TraceLocation location) {
            super.onReceiveLocation(location);
            KLog.d(location.toString());
            showRealtimeTrack(location);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_track);
        initview();
        initData();
    }

    private void initview() {
        mapview = (MapView) findViewById(R.id.mv_map);
        mBaiduMap = mapview.getMap();
        //初始化图层
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16));//设置默认放大级别16，比例尺为200米
        //设置为跟随模式
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, null));

        //去掉百度的logo
        View child = mapview.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }
        mapview.showScaleControl(false);//地图上比例尺
        mapview.showZoomControls(false);//隐藏缩放控件

        mciYinhuan = (CircleImg) findViewById(R.id.ci_yinhuan);
        mciTwoCode = (CircleImg) findViewById(R.id.ci_twocode);
        mciFinished = (CircleImg) findViewById(R.id.ci_finished);
        mciYinhuan.setOnClickListener(this);
        mciTwoCode.setOnClickListener(this);
        mciFinished.setOnClickListener(this);
    }

    private void initMap(){
        entryName=inspectionId;
        historyTrackRequest=new HistoryTrackRequest(1, serviceId, entryName);
        client = new LBSTraceClient(getApplicationContext());
        trace=new Trace(serviceId,entryName);
        KLog.d(entryName);
        client.setInterval(gatherInterval, packInterval);// 设置采集和打包周期
        client.setLocationMode(LocationMode.High_Accuracy);// 设置定位模式
        // 开启轨迹服务
        client.startTrace(trace,traceListener);
        mRequest = new LatestPointRequest(mSequenceGenerator.incrementAndGet(), serviceId, entryName);
        ProcessOption processOption = new ProcessOption();
        processOption.setNeedDenoise(true);
        processOption.setRadiusThreshold(100);
        mRequest.setProcessOption(processOption);
        //查询实时位置
        startRefreshThread(true);
        KLog.d(state+"");
        KLog.d(entryName);
    }

    private void initData() {
        historypoints=new ArrayList<TrackPoint>();
        historypointList = new ArrayList<LatLng>();
        pointList = new ArrayList<LatLng>();
        attendList = new ArrayList<OverlayOptions>();

        intent = getIntent();
        traceType = intent.getIntExtra("trafficId", 2);
//        taskId = in.getStringExtra("taskId");
        inspectionId = intent.getStringExtra("id");

        roads=(List<Road>)intent.getSerializableExtra("roads");

        state=intent.getIntExtra("state",1);
        mbpAttend = BitmapDescriptorFactory.fromResource(R.drawable.icon_daka);
        module = new InspectionModule(handler, this);
        initMap();

        /**
         * 锁屏监听
         */
        screenListener=new ScreenListener(this);
        screenListener.begin(new ScreenListener.ScreenStateListener() {
            @Override
            public void onScreenOn() {
                KLog.d("onScreenOn");
                keyguardManager=false;
            }

            @Override
            public void onScreenOff() {
                KLog.d("onScreenOff");
                KeyguardManager km= (KeyguardManager) getContext().getSystemService(Context.KEYGUARD_SERVICE);
                KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
                //解锁
                kl.disableKeyguard();
                //获取电源管理器对象
                PowerManager pm=(PowerManager) getContext().getSystemService(Context.POWER_SERVICE);
                //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
//                PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"bright");
                PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK,"bright");
//                点亮屏幕
                wl.acquire();
//                释放
                wl.release();
            }

            @Override
            public void onUserPresent() {
                KLog.d("onUserPresent");
            }
        });
    }


    private void getHistoryLine() {
        //设置轨迹查询起止时间
        historyTrackRequest.setStartTime(starttime);
        historyTrackRequest.setEndTime(endtime);
        historyTrackRequest.setPageIndex(pageIndex);
        historyTrackRequest.setPageSize(pageSize);
        // 查询历史轨迹
        client.queryHistoryTrack(historyTrackRequest, trackListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        keyguardManager=false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActivityShow = true;
    }

    private void showRealtimeTrack(TraceLocation location) {
        /**
         * 对象为空
         */
        if (location == null || (location.getLatitude() == 0 && location.getLongitude() == 0))
            return;
        if(keyguardManager) {
            FileUtil.saveLocationInfo(location.toString());
        }
        latitude = location.getLatitude();
        lonitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, lonitude);
        pointList.add(latLng);
        KLog.d(latLng.toString());
        drawRealtimePoint(latLng, location.getRadius(), location.getDirection());
    }

    /**
     * @param latLng
     * @param radius
     */
    private void drawRealtimePoint(LatLng latLng, double radius, float direction) {
        latitude=latLng.latitude;
        lonitude=latLng.longitude;
        KLog.d("lonitude:"+lonitude+"latitude:"+latitude);
        KLog.d(pointList.size()+"");
        mBaiduMap.clear();
        MyLocationData locData = new MyLocationData.Builder().accuracy((float) radius).direction(direction).latitude(latLng.latitude).longitude(latLng.longitude).build();
        mBaiduMap.setMyLocationData(locData);
        if (pointList.size() >= 2) {
            polyline = new PolylineOptions().width(10).color(Color.RED).points(pointList);
        }
        addMarker();
    }

    private void addMarker() {

        if (msUpdate != null) {
            mBaiduMap.setMapStatus(msUpdate);
        }

        if (polyline != null) {
            mBaiduMap.addOverlay(polyline);
        }

        if (attendList != null && attendList.size() > 0) {
            mBaiduMap.addOverlays(attendList);
        }

    }

    /**
     * 启动刷新线程
     *
     * @param isStart
     */
    private void startRefreshThread(boolean isStart) {
        if (refreshThread == null) {
            refreshThread = new RefreshThread();
        }
        refreshThread.refresh = isStart;
        if (isStart) {
            if (!refreshThread.isAlive()) {
                refreshThread.start();
            }
        } else {
            refreshThread = null;
        }

    }

    /**
     * 轨迹刷新线程
     *
     * @author BLYang
     */
    private class RefreshThread extends Thread {

        protected boolean refresh = true;

        public void run() {

            while (refresh) {
                // 轨迹服务开启成功后，调用queryEntityList()查询最新轨迹；
                // 未开启轨迹服务时，调用queryRealtimeLoc()进行实时定位。
                if (isTraceStarted) {
                    client.queryLatestPoint(mRequest,trackListener);
                } else {
                    client.queryRealTimeLoc(mlocRequest,entityListener);
                }
                try {
                    this.sleep(gatherInterval * 1000);
                } catch (InterruptedException e) {
                    System.out.println("线程休眠失败");
                }
            }

        }
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ci_twocode:
                Intent intent = new Intent("android.intent.action.ERCODESCANNERACTIVITY");
                startActivityForResult(intent, INT_TWO_CODE_REQUESTCODE);
                break;
            case R.id.ci_finished:
                AlertDialog.Builder tuichuDialog = new AlertDialog.Builder(this).setMessage("结束巡检？");
                tuichuDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                module.inspectionFinishCommint(inspectionId, taskId, taskDefKey, procInsId, "巡检结束", "yes");
                            }
                        });
                tuichuDialog.setNegativeButton("取消", null);
                tuichuDialog.show();
                break;
            case R.id.ci_yinhuan:
                Intent intent1 = new Intent("android.intent.action.NEWHIDDENTROUBLEACTIVITY");
                intent1.putExtra("inspectionid", inspectionId);
                intent1.putExtra("latitude", latitude);
                intent1.putExtra("lonitude", lonitude);
                Bundle bundle=new Bundle();
                bundle.putSerializable("roads",(Serializable)roads);
                intent1.putExtras(bundle);
                startActivity(intent1);
                break;
        }
    }

    @Override
    public void resultData(Message msg) {
        super.resultData(msg);
        switch (msg.what) {
            case InspectionModule.INT_HAS_DOING_TRUE_SUCCESS:
                InspectionChoose insp = (InspectionChoose) msg.obj;
                taskId = insp.getTaskId();
                taskDefKey = insp.getTaskDefKey();
                procInsId = insp.getProcInsId();
                procDefKey = insp.getProcDefKey();
                break;
            case InspectionModule.INT_SAVE_CLOCK_SUCCESS:
                Toast.makeText(this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                MarkerOptions mop = new MarkerOptions().position(new LatLng(latitude, lonitude)).icon(mbpAttend).zIndex(9).draggable(true);
                attendList.add(mop);
                mBaiduMap.addOverlay(mop);
                break;
            case InspectionModule.INT_FINISH_INSPECTION_SUCCESS:
                startRefreshThread(false);
                client.stopTrace(trace, traceListener);
                mapview.onDestroy();
                finish();
                break;
            case InspectionModule.INT_FINISH_INSPECTION_Filed:
            case InspectionModule.INT_SAVE_CLOCK_FILED:
            case InspectionModule.INT_HAS_DOING_FILED:
                toLoginActivity(msg);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == INT_TWO_CODE_REQUESTCODE) {
                String result = data.getExtras().getString("result");
                KLog.d(result);
                module.saveClock(inspectionId, result, String.valueOf(latitude), String.valueOf(lonitude));
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        KLog.d("后台运行");
        isActivityShow = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        screenListener.unregisterListener();
    }

    private AtomicInteger mSequenceGenerator = new AtomicInteger();
}
