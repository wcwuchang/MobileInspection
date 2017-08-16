package com.tianjin.MobileInspection.until;

import com.socks.library.KLog;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.umeng.message.UTrack;

/**
 * Created by wuchang on 2017-6-30.
 */
public class PushUntil {

    public static void addAlias(){
        KLog.d(MyApplication.getStringSharedPreferences("userId", ""));
        KLog.d(MyApplication.DeviceToken);
        MyApplication.getmPushAgent().removeAlias(MyApplication.getStringSharedPreferences("userId", ""),
                "MobileInspection", new UTrack.ICallBack() {
                    @Override
                    public void onMessage(boolean b, String s) {
                        KLog.d(s);
                        MyApplication.getmPushAgent().addAlias(
                                MyApplication.getStringSharedPreferences("userId", ""),
                                "MobileInspection", new UTrack.ICallBack() {
                                    @Override
                                    public void onMessage(boolean b, String s) {
                                        KLog.d(s);
                                    }
                                }
                        );
                    }
                });
    }

    public static void removeAlias(){
        MyApplication.getmPushAgent().removeAlias(MyApplication.getStringSharedPreferences("userId", ""),
                "MobileInspection", new UTrack.ICallBack() {
                    @Override
                    public void onMessage(boolean b, String s) {
                        KLog.d(s);
                    }
                });
    }
}
