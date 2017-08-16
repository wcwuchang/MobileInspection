package com.tianjin.MobileInspection.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.socks.library.KLog;
import com.tianjin.MobileInspection.until.PushUntil;

/**
 * Created by wuchang on 2017-7-28.
 */
public class PushService extends Service{

    public final static String SERVICE_ACTION="stopService";
    private StopPushReceiver receiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        receiver=new StopPushReceiver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        PushUntil.addAlias();
        KLog.d("pushservice","推送服务已启动");
        IntentFilter filter=new IntentFilter();
        filter.addAction(SERVICE_ACTION);
        registerReceiver(receiver,filter);
        return super.onStartCommand(intent, flags, startId);
    }

    private class StopPushReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(SERVICE_ACTION)){
                stopSelf();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        PushUntil.removeAlias();
        KLog.d("pushservice","推送服务已终止");
        unregisterReceiver(receiver);
    }
}
