package com.tianjin.MobileInspection.module;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * Created by wuchang on 2016-12-6.
 */
public class BaseModule {

    public Context context;
//    public Object resultMsg = new Object();

    public void resultMessage(Handler handler, int resultCode, Object obj, int errorcode) {
        Message msg = handler.obtainMessage();
        msg.what = resultCode;
        msg.obj = obj;
        msg.arg1 = errorcode;
        handler.sendMessage(msg);
    }




}
