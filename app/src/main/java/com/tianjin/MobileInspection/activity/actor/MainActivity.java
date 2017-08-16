package com.tianjin.MobileInspection.activity.actor;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.socks.library.KLog;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.adapter.IndexOptionAdapter;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.customview.CircleImg;
import com.tianjin.MobileInspection.customview.SlidingMenu;
import com.tianjin.MobileInspection.entity.IndexOptionEntity;
import com.tianjin.MobileInspection.entity.Weather;
import com.tianjin.MobileInspection.main.LoginActivity;
import com.tianjin.MobileInspection.module.ConfigModule;
import com.tianjin.MobileInspection.module.FileUploadModule;
import com.tianjin.MobileInspection.module.InspectionModule;
import com.tianjin.MobileInspection.module.LoginModule;
import com.tianjin.MobileInspection.module.UserInfoModule;
import com.tianjin.MobileInspection.service.PushService;
import com.tianjin.MobileInspection.until.FileUtil;
import com.umeng.message.UTrack;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final static int INT_USERINFO_EDIT = 0x768;
    private TextView mtvUser;
    private SlidingMenu menu;
    private CircleImg mciUserHead;
    private LinearLayout mlinearSetup;
    private LinearLayout mlinearClear;
    private LinearLayout mlinearHidden;

    private TextView mtvLogout;
    private GridView mgvOption;
    private List<IndexOptionEntity> data;
    private IndexOptionAdapter adapter;
    private LinearLayout mlinearStart;
    private TextView mtvStart;

    private List<String> actionList;

    private long keyDownTime = 0;
    private LoginModule module;
    private UserInfoModule userInfoModule;
    private InspectionModule inspectionModule;
    private Bitmap bit;
    private int config;
    private boolean isUserInfo = false;

    private TextView mtvCity;
    private TextView mtvAlams;
    private TextView mtvTemp;
    private TextView mtvFlee;
    private TextView mtvHum;
    private TextView mtvWind;
    private ImageView mivTmp;

    private MapView mapView;

    private Runnable run = new Runnable() {
        @Override
        public void run() {
            mciUserHead.setImageBitmap(bit);
            try {
                saveBitmap(bit, MyApplication.getStringSharedPreferences(MyApplication.USER_INFO_PHOTO_NAME, "a"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private Runnable error = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(MainActivity.this, "头像获取失败", Toast.LENGTH_SHORT).show();
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            resultData(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(false);//true:半透明的黑色；false全透明的
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_index);
        initView();
        initData();
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    private void initView() {
        // TODO Auto-generated method stub
        mgvOption = (GridView) findViewById(R.id.gv_functions);
        mtvUser = (TextView) findViewById(R.id.tv_login_user);
        menu = (SlidingMenu) findViewById(R.id.id_menu);
        mciUserHead = (CircleImg) findViewById(R.id.ci_user_header);
        mlinearSetup = (LinearLayout) findViewById(R.id.linear_setup);
        mlinearClear = (LinearLayout) findViewById(R.id.linear_clear);
        mlinearHidden=(LinearLayout)findViewById(R.id.linear_hidden);

        mtvLogout = (TextView) findViewById(R.id.tv_zhuxiao);
        mlinearStart = (LinearLayout) findViewById(R.id.linear_start);
        mtvStart = (TextView) findViewById(R.id.tv_start);

        mtvLogout.setOnClickListener(this);
        mlinearStart.setOnClickListener(this);
        mtvStart.setOnClickListener(this);
        mlinearSetup.setOnClickListener(this);
        mlinearClear.setOnClickListener(this);
        mlinearHidden.setVisibility(View.GONE);
//        mlinearHidden.setOnClickListener(this);

        LinearLayout main = (LinearLayout) findViewById(R.id.main_fragment);
        main.setOnClickListener(this);

        mtvCity=(TextView)findViewById(R.id.tv_weather_city);
        mtvAlams=(TextView)findViewById(R.id.tv_weather_alarms);
        mtvTemp=(TextView)findViewById(R.id.tv_weather_temp);
        mtvFlee=(TextView)findViewById(R.id.tv_weather_flee);
        mtvHum=(TextView)findViewById(R.id.tv_weather_hum);
        mtvWind=(TextView)findViewById(R.id.tv_weather_wind);
        mivTmp=(ImageView)findViewById(R.id.iv_tmp);
        findViewById(R.id.linear_start_inspection).setOnClickListener(this);
        getLocation();
    }

    private void initData() {
        // TODO Auto-generated method stub
        module = new LoginModule(handler, this);
        userInfoModule = new UserInfoModule(handler, this);
        inspectionModule = new InspectionModule(handler, this);
        data = new ArrayList<IndexOptionEntity>();
        ArrayList<Bitmap> lb = new ArrayList<>();
        Bitmap bp1 = BitmapFactory.decodeResource(getResources(), R.drawable.home_group_1);
        Bitmap bp2 = BitmapFactory.decodeResource(getResources(), R.drawable.home_group_2);
        Bitmap bp3 = BitmapFactory.decodeResource(getResources(), R.drawable.home_group_3);
        Bitmap bp4 = BitmapFactory.decodeResource(getResources(), R.drawable.home_group_4);
        Bitmap bp5 = BitmapFactory.decodeResource(getResources(), R.drawable.home_group_5);
        Bitmap bp6 = BitmapFactory.decodeResource(getResources(), R.drawable.home_group_6);
        Bitmap bp7 = BitmapFactory.decodeResource(getResources(), R.drawable.home_group_7);
        Bitmap bp8 = BitmapFactory.decodeResource(getResources(), R.drawable.home_group_8);
        Bitmap bp9 = BitmapFactory.decodeResource(getResources(), R.drawable.home_group_9);
        lb.add(bp1);
        lb.add(bp2);
        lb.add(bp3);
        lb.add(bp7);
        lb.add(bp8);
        lb.add(bp5);
        lb.add(bp4);
        lb.add(bp6);
        lb.add(bp9);
        ArrayList<String> as = new ArrayList<String>();
        as.add(getString(R.string.str_index_option1));
        as.add(getString(R.string.str_index_option2));
        as.add(getString(R.string.str_index_option3));
        as.add(getString(R.string.str_index_option7));
        as.add(getString(R.string.str_index_option8));
        as.add(getString(R.string.str_index_option5));
        as.add(getString(R.string.str_index_option4));
        as.add(getString(R.string.str_index_option6));
//        as.add(getString(R.string.str_index_option9));
        as.add(getString(R.string.str_index_option9_1));
        for (int i = 0; i < as.size(); i++) {
            IndexOptionEntity entity = new IndexOptionEntity();
            entity.setOptionBmp(lb.get(i));
            entity.setOptionName(as.get(i));
            entity.setOptionId(i);
            data.add(entity);
        }
        adapter = new IndexOptionAdapter(this, data, this);
        mgvOption.setAdapter(adapter);

        mtvUser.setText(MyApplication.getStringSharedPreferences("name", ""));

        //跳转列表的action
        actionList = new ArrayList<>();
        actionList.add("android.intent.action.INSPECTIONLISTACTIVITY");//巡检列表
        actionList.add("android.intent.action.TODOACTIVITY");//待办事项
        actionList.add("android.intent.action.CONTACTACTIVITY");//通讯录
        actionList.add("android.intent.action.SPECIALLISTACTIVITY");//专项列表
        actionList.add("android.intent.action.DEAILYLISTACTIVITY");//日常列表
        actionList.add("android.intent.action.CONTRACTMANAGEACTIVITY");//合同管理
        actionList.add("android.intent.action.EVALUATEMANAGEACTIVITY");//评价管理
        actionList.add("android.intent.action.STANDINGBOOKMANAGEACTIVITY");//台账管理
//        actionList.add("android.intent.action.PLANMANAGEACTIVITY");//计划管理
        actionList.add("android.intent.action.HIDDENLISTACTIVITY");//隐患
        userInfoModule.getInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(MyApplication.DBM.gettHiddenSpinner().query()==null
                ||MyApplication.DBM.gettHiddenSpinner().query().size()==0) {
            ConfigModule.getConfiguration(this);
        }else {
            KLog.d(MyApplication.DBM.gettHiddenSpinner().query().size()+"");
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.tv_zhuxiao:
                module.loginout();
                break;
            case R.id.linear_start:
            case R.id.linear_start_inspection:
            case R.id.tv_start:
//                inspectionModule.getHasDoingInspection();//判断是否存在正在巡检中的任务
                Intent in = new Intent("android.intent.action.INSPECTIONCHOOSEACTIVITY");
                startActivity(in);
                break;
            case R.id.linear_setup:
                Intent info = new Intent("android.intent.action.USERINFOACTOVOTY");
                startActivityForResult(info, INT_USERINFO_EDIT);
                break;
            case R.id.main_fragment:
//                config++;
//                if (config == 4) {
//                    Intent irnt = new Intent("android.intent.action.CONFIGUREACTIVITY");
//                    startActivity(irnt);
//                    config = 1;
//                }
                break;
            case R.id.linear_clear:
                AlertDialog.Builder tuichuDialog = new AlertDialog.Builder(this).setMessage("清空缓存？");
                tuichuDialog.setPositiveButton("清空",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                File file = new File(MyApplication.CATCH_FILE);
                                if (file.exists()) {
                                    FileUtil.clearFile(file);
                                    if (FileUtil.isClearSuccess(file)) {
                                        Toast.makeText(MainActivity.this, "已清理所有缓存文件！", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MainActivity.this, "部分文件无法删除，请手动清理！" + "\n" + file.getPath(), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, "无缓存文件", Toast.LENGTH_SHORT).show();
                                }
                                FileUtil.creatFile();
                            }
                        });
                tuichuDialog.setNegativeButton("取消", null);
                tuichuDialog.show();
                break;
            case R.id.linear_hidden:
                Intent intent=new Intent("android.intent.action.HIDDENLISTACTIVITY");
//                Intent intent=new Intent("android.intent.action.NEWHIDDENTROUBLEACTIVITY");
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    public void optionDo(int postion) {
        String action = actionList.get(postion);
        Intent in = new Intent(action);
        startActivity(in);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (System.currentTimeMillis() - keyDownTime > 2000) {
            keyDownTime = System.currentTimeMillis();
            Toast.makeText(this, "再次点击退出程序", Toast.LENGTH_SHORT).show();
        } else {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
        }
        return false;
    }

    private void showUserInfo() {
        String headname = MyApplication.getStringSharedPreferences(MyApplication.USER_INFO_PHOTO_NAME, "a");
        if(headname==null || headname.equals("null")) return;
        File file = new File(MyApplication.IMAGEFILEPATH, headname);
        KLog.d(file.getPath());
        if (file.exists()) {
            mciUserHead.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    bit = FileUploadModule.getBitmapFromUrl(MyApplication.getStringSharedPreferences(MyApplication.USER_INFO_PHOTO_PATH, "a"));
                    if (bit != null) {
                        handler.post(run);
                    } else {
                        handler.post(error);
                    }
                }
            }).start();
        }
    }

    private void saveBitmap(Bitmap bit, String filename) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //compress方法是把一个位图写到一个OutputStream中,参数一是位图对象，二是格式,三是压缩的质量，四是输出流
        bit.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        //输出流转成输入流
        InputStream inputimage = new ByteArrayInputStream(baos.toByteArray());
        File file = new File(MyApplication.IMAGEFILEPATH, filename);
        FileOutputStream out = new FileOutputStream(file);
        byte[] b = new byte[512];
        int len = 0;
        while ((len = inputimage.read(b)) != -1) {
            out.write(b, 0, len);
        }
        out.flush();
        out.close();
    }

    public void resultData(Message msg) {
        switch (msg.what) {
            case LoginModule.INT_LOGIN_SUCCESS:
                if (isUserInfo) {
                    userInfoModule.getInfo();
                } else {
//                    inspectionModule.getHasDoingInspection();
                }
                break;
            case LoginModule.INT_LOGIN_FILED:
                Toast.makeText(this, "登录超时，请重新登录", Toast.LENGTH_SHORT).show();
                retuenTologin();
                break;
            case UserInfoModule.INT_GET_USERINFO_SUCCESS:
                showUserInfo();
                break;
//            case InspectionModule.INT_HAS_DOING_TRUE_SUCCESS:
//                InspectionChoose insp = (InspectionChoose) msg.obj;
//                Intent inm = new Intent("android.intent.action.LOACTIONTRACKACTIVITY");
//                inm.putExtra("id", insp.getInspectionId());
//                inm.putExtra("trafficId", insp.getTrafficId());
//                inm.putExtra("taskId", insp.getTaskId());
//                inm.putExtra("taskDefKey", insp.getTaskDefKey());
//                inm.putExtra("procInsId", insp.getProcInsId());
//                inm.putExtra("procDefKey", insp.getProcDefKey());
//                inm.putExtra("beginDate", insp.getDate());
//                startActivity(inm);
//                break;
//            case InspectionModule.INT_HAS_DOING_FALSE_SUCCESS:
//                Intent in = new Intent("android.intent.action.INSPECTIONCHOOSEACTIVITY");
//                startActivity(in);
//                break;
            case UserInfoModule.INT_GET_USERINFO_FILED:
                isUserInfo = true;
                toLoginActivity(msg);
                break;
            case InspectionModule.INT_HAS_DOING_FILED:
                isUserInfo = false;
                toLoginActivity(msg);
                break;
            case LoginModule.INT_LOGINOUT_SUCCESS:
                MyApplication.getmPushAgent().removeAlias(MyApplication.getStringSharedPreferences("userId", ""),
                        "MobileInspection", new UTrack.ICallBack() {
                            @Override
                            public void onMessage(boolean b, String s) {
                                KLog.d(s);
                                retuenTologin();
                            }
                        });
                break;
            case LoginModule.INT_LOGINOUT_FILED:
                retuenTologin();
                break;
            default:
                break;
        }
    }

    public void toLoginActivity(Message msg) {
        if (msg.arg1 == 0) {
            LoginModule module = new LoginModule(handler, this);
            module.login(MyApplication.getStringSharedPreferences("username", ""),
                    MyApplication.getStringSharedPreferences("password", ""));
        } else {
            Toast.makeText(this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void retuenTologin() {
        MyApplication.setStringSharedPreferences("password", "");
        MyApplication.setStringSharedPreferences("JSESSIONID", "");
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == INT_USERINFO_EDIT) {
                userInfoModule.getInfo();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent=new Intent();
        intent.setAction(PushService.SERVICE_ACTION);
        sendBroadcast(intent);
        if(client!=null&&client.isStarted()){
            client.stop();
        }
    }

    private LocationClient client;
    private void getLocation(){
        KLog.d("ss");
        client=new LocationClient(getApplicationContext());
        MyLocationListener listener=new MyLocationListener();
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        int span=3000;
        option.setScanSpan(span);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIsNeedLocationDescribe(true);
        option.setIsNeedLocationPoiList(true);
        option.setIgnoreKillProcess(false);
        option.SetIgnoreCacheException(false);
        option.setEnableSimulateGps(false);
        client.setLocOption(option);
        client.registerLocationListener(listener);
        client.start();
    }

    class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation.getLocType() == BDLocation.TypeNetWorkException
                    ||bdLocation.getLocType() == BDLocation.TypeCriteriaException) {
                Toast.makeText(MainActivity.this,"获取当前定位失败，无法获取天气信息，请检查网络是否通畅",Toast.LENGTH_SHORT).show();
            }else {
                StringBuffer sb = new StringBuffer();
                sb.append(bdLocation.getCity());
                sb.append(bdLocation.getAddress().address);
                double longitude=bdLocation.getLongitude();
                double latitude=bdLocation.getLatitude();
                initMapView(longitude,latitude);
                KLog.d(sb.toString());
                mtvCity.setText(bdLocation.getCity());
                ConfigModule.getWeatherInfo(weHandler, bdLocation.getCity());
            }
        }
    }

    private Handler weHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x324:
                    final Weather weather=(Weather)msg.obj;
                    String str=weather.getShishi();
                    mtvTemp.setText(str.substring(0,9));
//                    mtvFlee.setText(str.substring(14,17));
//                    mtvFlee.setTextSize(30f);
//                    String[] ta=da[1].split("\\:");
//                    String[] te=ta[1].split("\\)");
//                    mtvHum.setText(te[0]);
//                    mtvHum.setTextSize(25f);
                    mtvFlee.setText(weather.getWeather());
                    mtvHum.setText("pm2.5:"+weather.getPm25());
                    mtvWind.setText(weather.getWind());
                    mtvCity.setText(str.substring(14,17));
                    mtvCity.setTextSize(30f);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            webit=FileUploadModule.getBitmapFromUrlT(weather.getDayPictureUrl());
                            handler.post(runnable);
                        }
                    }).start();

                    break;
            }
        }
    };
    private Bitmap webit;
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            if(webit!=null) {
                mivTmp.setImageBitmap(webit);
            }
        }
    };

    private void initMapView(double longitude, double latitude) {
        mapView=(MapView)findViewById(R.id.mv_location);
        mapView.setClickable(false);
        BaiduMap mBaiduMap=mapView.getMap();
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

        MyLocationData locData = new MyLocationData.Builder().accuracy(40f) .direction(100).latitude(latitude).longitude(longitude).build();
        mBaiduMap.setMyLocationData(locData);
        LatLng latLng=new LatLng(latitude,longitude);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.animateMapStatus(u);
    }


}
