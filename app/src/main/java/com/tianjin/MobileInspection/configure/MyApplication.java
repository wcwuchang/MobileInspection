package com.tianjin.MobileInspection.configure;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.RemoteViews;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.socks.library.KLog;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.database.DatabaseManager;
import com.tianjin.MobileInspection.entity.Todo;
import com.tianjin.MobileInspection.until.SuperScriptUnit;
import com.tianjin.MobileInspection.until.VolleyUtil;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyApplication extends Application{

	public static final String PUSH_CAST="push_message";

	public static DatabaseManager DBM;
	public static SharedPreferences sharedPreferences;
	public static final String CATCH_FILE="/sdcard/MobileInspection/";
	public static final String IMAGEFILEPATH="/sdcard/MobileInspection/image/";
	public static final String ICACHE_PATH="/sdcard/MobileInspection/cache/";
	public static final String DOWNLOAD_PATH="/sdcard/MobileInspection/down/";
	public static final String CONTACT_PATH="/sdcard/MobileInspection/contactHead/";
	public static final long BAIDU_TRACK_SERVICE_ID=130650;
//	public static final long BAIDU_TRACK_SERVICE_ID=134932;
	public static final int CONNECTION_TIME_OUT=10000;
	public static final int ONE_DAY_TIME=86400;


	public static final String USER_INFO_PHONE="mobile";
	public static final String USER_INFO_EMAIL="email";
	public static final String USER_INFO_PHOTO_NAME="photo_name";
	public static final String USER_INFO_PHOTO_PATH="photo_path";

	//友盟推送
	public static String DeviceToken;
	public static PushAgent mPushAgent;

	@Override
	public void onCreate() {
		super.onCreate();
//        CrashHandler handler = CrashHandler.getInstance();
//        // 在Appliction里面设置我们的异常处理器为UncaughtExceptionHandler处理器
//        handler.init(getApplicationContext());
		//地图的sdk初始化
		SDKInitializer.initialize(getApplicationContext());
		SDKInitializer.setCoordType(CoordType.BD09LL);
		//初始化网络连接函数
		VolleyUtil.initialize(this);
		initSharedPreferences();
		initPushAgent();
		//初始化数据库
		DBM=new DatabaseManager(this);
//		DBM.gettInspection().create();
//		DBM.gettLocationInfo().create();
		DBM.gettPlanManage().create();
		DBM.gettHiddenSpinner().create();
		DBM.gettRoad().create();
		DBM.gettTodo().create();


	}

	private void initPushAgent(){
		//开启推送
		mPushAgent = PushAgent.getInstance(this);
		mPushAgent.onAppStart();
//		mPushAgent.enable(new IUmengCallback() {
//			@Override
//			public void onSuccess() {
//
//			}
//
//			@Override
//			public void onFailure(String s, String s1) {
//				KLog.d(s+"===="+s1);
//			}
//		});
		//注册推送服务，每次调用register方法都会回调该接口
		mPushAgent.register(new IUmengRegisterCallback() {

			@Override
			public void onSuccess(String deviceToken) {
				//注册成功会返回device token
				KLog.d(deviceToken);
				DeviceToken = deviceToken;
				mPushAgent.setMessageChannel(DeviceToken);

			}

			@Override
			public void onFailure(String s, String s1) {
				KLog.d(s+"===="+s1);
			}
		});

		UmengMessageHandler messageHandler = new UmengMessageHandler() {
			@Override
			public Notification getNotification(Context context, UMessage msg) {
				switch (msg.builder_id) {
					case 0:
						KLog.d(msg.title+"  "+msg.ticker+"  "+msg.text+"  "+msg.custom);
						Notification.Builder builder = new Notification.Builder(context);
						RemoteViews myNotificationView = new RemoteViews(context.getPackageName(),
								R.layout.layout_notification);
						myNotificationView.setTextViewText(R.id.tv_notification_title, msg.title);
						myNotificationView.setTextViewText(R.id.tv_notification_content, msg.text);
						long time=System.currentTimeMillis();
						Date date=new Date(time);
						SimpleDateFormat sdf=new SimpleDateFormat("HH:MM");
						myNotificationView.setTextViewText(R.id.tv_notification_time,sdf.format(date));
//						myNotificationView.setImageViewBitmap(R.id.notification_large_icon,
//								getLargeIcon(context, msg));
//						myNotificationView.setImageViewResource(R.id.notification_small_icon,
//								getSmallIconId(context, msg));
						builder.setContent(myNotificationView)
								.setSmallIcon(getSmallIconId(context, msg))
								.setTicker(msg.ticker)
								.setAutoCancel(true);

						Todo msg1=new Todo();
						msg1.setTodoId(msg.msg_id);
						msg1.setTaskName(msg.custom);
						msg1.setTitle(msg.title);
						msg1.setApply(msg.text);
						msg1.setProcDefKey(msg.ticker);
						SimpleDateFormat sdf1=new SimpleDateFormat("yyyy年mm月dd日 HH:MM");
						msg1.setTaskCreateDate(sdf1.format(date));
						DBM.gettTodo().insert(msg1);
						if(checkIsSupportedByVersion()){
							List<Todo> data=DBM.gettTodo().query();
							if(data!=null) {
//								handleBadge(data.size());
								SuperScriptUnit.resetBadgeCount(getApplicationContext(),data.size());
							}
						}
						return builder.getNotification();
					default:
						//默认为0，若填写的builder_id并不存在，也使用默认。
						return super.getNotification(context, msg);
				}
			}
		};
		mPushAgent.setMessageHandler(messageHandler);

		UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
			@Override
			public void dealWithCustomAction(Context context, UMessage msg) {
				Intent intent=new Intent();
				intent.setAction("android.intent.action.TODOACTIVITY");
				startActivity(intent);
			}
		};
		mPushAgent.setNotificationClickHandler(notificationClickHandler);
		//设置24小时提醒
		mPushAgent.setNoDisturbMode(0, 0, 0, 0);
		//设置铃声间隔时间（10秒类收到消息不会重复响铃）
		mPushAgent.setMuteDurationSeconds(10);
		mPushAgent.setDisplayNotificationNumber(10);

		mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SERVER); //声音
		mPushAgent.setNotificationPlayLights(MsgConstant.NOTIFICATION_PLAY_SERVER);//呼吸灯
		mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SERVER);//振动
	}

	public void initSharedPreferences(){
		if(sharedPreferences==null){
			sharedPreferences=this.getSharedPreferences("MobileInspection", Activity.MODE_PRIVATE);
		}
	}

	public static PushAgent getmPushAgent(){
		return mPushAgent;
	}

	/**
	 * sharedPreferences 写入字符串
	 * @param key
	 * @param value
	 */
	public static void setStringSharedPreferences(String key,String value){
		SharedPreferences.Editor editor=sharedPreferences.edit();
		editor.putString(key,value);
		editor.commit();
	}

	/**
	 * sharedPreferences boolean
	 * @param key
	 * @param value
     */
	public static void setBooleanSharedPreferences(String key,boolean value){
		SharedPreferences.Editor editor=sharedPreferences.edit();
		editor.putBoolean(key,value);
		editor.commit();
	}

	/**
	 *  sharedPreferences 写入字整数
	 * @param key
	 * @param value
	 */
	public static void setIntegerSharedPreferences(String key,int value){
		SharedPreferences.Editor editor=sharedPreferences.edit();
		editor.putInt(key,value);
		editor.commit();
	}

	/**
	 * sharedPreferences 获取string
	 * @param key
	 * @param value
	 */
	public static String getStringSharedPreferences(String key,String value){
		return sharedPreferences.getString(key,value);
	}

	/**
	 * 获取 Boolean
	 * @param key
	 * @param value
     * @return
     */
	public static boolean getBooleanSharedPreferences(String key,boolean value){
		return sharedPreferences.getBoolean(key,value);
	}

	/**
	 * sharedPreferences 获取int
	 * @param key
	 * @param value
	 */
	public static Integer getIntSharedPreferences(String key,int value){
		return sharedPreferences.getInt(key,value);
	}

	/**
	 * 获取应用版本号
	 *
	 * @param context
	 * @return
	 */
	public static String getAppVersion(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return info.versionCode + "";
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return "1.0";
	}

	public boolean checkIsSupportedByVersion(){
		try {
			PackageManager manager = getPackageManager();
			PackageInfo info = manager.getPackageInfo("com.huawei.android.launcher", 0);
			if(info.versionCode>=63029){
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

}
